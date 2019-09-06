package rugds.wacc

import akka.actor.ActorSystem
import akka.http.javadsl.model.headers.{AccessControlAllowHeaders, AccessControlAllowMethods, AccessControlAllowOrigin}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.headers.HttpOriginRange
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.{MethodRejection, RejectionHandler}
import akka.stream.scaladsl.Flow
import spray.json.DefaultJsonProtocol
import spray.json._
import DefaultJsonProtocol._
import StatusCodes._
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.io.StdIn
import scala.util.{Failure, Success}

object WebServer {
	implicit val system: ActorSystem = ActorSystem("my-system")
	implicit val materializer: ActorMaterializer = ActorMaterializer()
	implicit val executionContext: ExecutionContextExecutor = system.dispatcher

	final case class Title(title: String)
	final case class ChatMessage(name: String, content: String)

	implicit val titleFormat: RootJsonFormat[Title] = jsonFormat1(Title)
	implicit val messageFormat: RootJsonFormat[ChatMessage] = jsonFormat2(ChatMessage)

	val chatRoom: ChatRoom = ChatRoom()

	val websocketHeader: String = "Sec-WebSocket-Key".toLowerCase
	def extractWebsocketHeader: HttpHeader => Option[String] = {
		case HttpHeader(`websocketHeader`, value) => Some(value)
		case _  => None
	}

	def websocketHandler(user: String): Flow[Message, Message, Any] = Flow[Message].collect {
		case TextMessage.Strict(msg) ⇒ msg.parseJson.convertTo[ChatMessage]
	}
	.via(chatRoom.websocketFlow(user))
	.map( ( msg: ChatMessage ) ⇒ TextMessage.Strict(msg.toJson.compactPrint) )

	def main(args: Array[String]) {
		val mongoClient: MongoClient = MongoClient("mongodb://172.19.0.2")
		val database: MongoDatabase = mongoClient.getDatabase("wacchat")
		val collection: MongoCollection[Document] = database.getCollection("messages")

		// Needed to allow POST requests with CORS.
		implicit def rejectionHandler: RejectionHandler =
			RejectionHandler.newBuilder().handleAll[MethodRejection] { rejections =>
				val methods = rejections map (_.supported)
				lazy val names = methods map (_.name) mkString ", "

				respondWithHeaders(List(
					AccessControlAllowHeaders.create("Content-Type"),
					AccessControlAllowMethods.create(HttpMethods.GET, HttpMethods.POST, HttpMethods.OPTIONS),
					AccessControlAllowOrigin.create(HttpOriginRange.*)
				)) {
					options {
						complete(s"Supported methods : $names.")
					} ~
						complete(StatusCodes.MethodNotAllowed, s"HTTP method not allowed, supported methods: $names!")
				}
			}.result()

		// API routes.
		val route =
			pathPrefix("api") {
				respondWithHeaders(List(
					AccessControlAllowOrigin.create(HttpOriginRange.*)
				)) {
					path("messages") {
						get {
							onComplete( collection.find().toFuture() ) {
								case Success(null) => complete("")
								case Success(messages: Seq[Document]) => complete(messages.map( m => ChatMessage(m.get("name").get.asString().getValue, m.get("content").get.asString().getValue).toJson).toJson)
								case Failure(e) => complete((InternalServerError, e.getMessage))
							}
						} ~
						post {
							entity(as[ChatMessage]) { message =>
								println(message)

								val document: Document = Document.apply( message.toJson.compactPrint )
								onComplete(collection.insertOne( document ).head()) {
									case Success(c) => complete("")
									case Failure(e) => complete((InternalServerError, e.getMessage))
								}
							}
						}
					} ~
					pathEndOrSingleSlash {
						get {
							complete(Title("WacChat"))
						}
					}
				}
			} ~
			pathPrefix("socket.io") {
				pathEndOrSingleSlash {
					headerValue(extractWebsocketHeader) { websocketKey =>
						println(s"Key: $websocketKey")
						handleWebSocketMessages(websocketHandler(websocketKey))
					}
				}
			}

		// Start web server.
		val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(route, "0.0.0.0", 8080)

		println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
		StdIn.readLine() // let it run until user presses return
		bindingFuture
			.flatMap(_.unbind()) // trigger unbinding from the port
			.onComplete(_ => system.terminate()) // and shutdown when done
	}
}