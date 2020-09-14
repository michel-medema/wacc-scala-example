package rugds.wacc

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{HttpOriginRange, `Access-Control-Allow-Headers`, `Access-Control-Allow-Methods`, `Access-Control-Allow-Origin`}
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.stream.scaladsl.Flow
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase}
import rugds.wacc.chatroom.ChatRoom
import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

object WebServer {
	implicit val system: ActorSystem = ActorSystem("my-system")
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

	def websocketHandler(user: String): Flow[Message, TextMessage.Strict, NotUsed] =
		Flow[Message]
			.collect {
				case TextMessage.Strict(msg) ⇒ msg.parseJson.convertTo[ChatMessage]
			}
			.via(chatRoom.websocketFlow(user))
			.map( ( msg: ChatMessage ) ⇒ TextMessage.Strict(msg.toJson.compactPrint) )

	def main(args: Array[String]) {
		// TODO: The connection string should not be hard-coded.
		val mongoClient: MongoClient = MongoClient("mongodb://mongodb")
		val database: MongoDatabase = mongoClient.getDatabase("wacchat")
		val collection: MongoCollection[Document] = database.getCollection("messages")

		// API routes.
		val route =
			pathPrefix("api") {
				respondWithHeaders(List(
					`Access-Control-Allow-Origin`(HttpOriginRange.*)
				)) {
					path("messages") {
						// Needed to allow POST requests with CORS.
						respondWithHeaders(List(
							`Access-Control-Allow-Headers`("Content-Type"),
							`Access-Control-Allow-Methods`(HttpMethods.GET, HttpMethods.POST, HttpMethods.OPTIONS)
						)) {
							options {
								complete(s"Supported methods : get, post.")
							}
						} ~
						get {
							onComplete( collection.find().toFuture() ) {
								case Success(null) => complete("")
								case Success(messages: Seq[Document]) => complete(messages.map( m => ChatMessage(m.get("name").get.asString().getValue, m.get("content").get.asString().getValue).toJson).toJson)
								case Failure(e) => complete((InternalServerError, e.getMessage))
							}
						} ~
						post {
							entity(as[ChatMessage]) { message =>
								val document: Document = Document.apply( message.toJson.compactPrint )
								onComplete(collection.insertOne( document ).head()) {
									case Success(c) => complete(message.toJson)
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
						handleWebSocketMessages(websocketHandler(websocketKey))
					}
				}
			}

		// Start web server.
		Http().newServerAt("0.0.0.0", 8080).bind(route)
	}
}
