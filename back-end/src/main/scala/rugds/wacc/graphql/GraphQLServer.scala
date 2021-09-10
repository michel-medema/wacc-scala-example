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

object GraphQLServer extends App {
  import system.dispatcher

  implicit val system: ActorSystem = ActorSystem("graphql-system")

  def executeQuery(query: Document): StandardRoute = {
    complete(
      Executor.execute(SchemaDefinition.schema, query, new MessageRepository).map(StatusCodes.OK -> _)
    )
  }

  val route: Route =
    path("graphql") {
      post {
        entity(as[JsValue]) { body =>
          body.asJsObject.fields("query") match {
            case JsString(query: String) =>
              println(query)

              QueryParser.parse(query) match {
                case Success(ast) =>
                  executeQuery(ast)
                case Failure(exception) =>
                  complete(StatusCodes.BadRequest, exception)
              }

            case _ => complete(StatusCodes.BadRequest, "Missing query.")
          }
        }
      }
    }

  Http().newServerAt("0.0.0.0", 8081).bind(route)
}
