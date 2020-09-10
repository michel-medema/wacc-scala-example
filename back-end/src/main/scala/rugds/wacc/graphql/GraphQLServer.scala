package rugds.wacc.graphql

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import sangria.ast.Document
import sangria.execution._
import sangria.parser.QueryParser
import sangria.marshalling.sprayJson._
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

import scala.util.{Failure, Success}


// Uncomment extends App to run this server.
object GraphQLServer extends App {
  implicit val system: ActorSystem = ActorSystem("graphql-system")

  import system.dispatcher

  def executeQuery(query: Document): StandardRoute = {
    complete(
      Executor.execute(SchemaDefinition.schema, query, new MessageRepository).map(StatusCodes.OK -> _)
    )
  }

  val route: Route =
    path("graphql") {
      get {
        complete("Test")
      } ~
      post {
        entity(as[JsValue]) { body =>
          val query: String = body.asJsObject.fields("query") match {
            case JsString(q) => q
          }

          println(query)

          QueryParser.parse(query) match {
            case Success(ast) =>
              executeQuery(ast)
            case Failure(exception) =>
              complete(StatusCodes.BadRequest, exception)
          }
        }
      }
    }

  Http().newServerAt("0.0.0.0", 8081).bind(route)
}
