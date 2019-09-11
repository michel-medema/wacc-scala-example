package rugds.wacc

import rugds.wacc.WebServer.ChatMessage
import sangria.schema._
import sangria.macros.derive._

object SchemaDefinition {

  implicit val ChatMessage: ObjectType[Unit, ChatMessage] = deriveObjectType[Unit, ChatMessage]()

  /*val ChatMessage = ObjectType(
    "ChatMessage",
    "Some description.",
    fields[MessageRepository, ChatMessage](
      Field("name", StringType, resolve = _.value.name),
      Field("content", StringType, resolve = _.value.content)
    )
  )*/

  val Query = ObjectType(
    "Query", fields[MessageRepository, Unit](
      Field("messages", ListType(ChatMessage), resolve = ctx => ctx.ctx.getMessages)
    )
  )

  val schema = Schema(Query)
}
