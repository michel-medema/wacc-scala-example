package rugds.wacc.graphql

import rugds.wacc.WebServer.ChatMessage
import sangria.macros.derive.deriveObjectType
import sangria.schema.{Field, ListType, ObjectType, Schema, fields}

object SchemaDefinition {
  implicit val ChatMessage: ObjectType[Unit, ChatMessage] = deriveObjectType[Unit, ChatMessage]()

  val query: ObjectType[MessageRepository, Unit] = ObjectType(
    "Query", fields[MessageRepository, Unit](
      Field("messages", ListType(ChatMessage), resolve = ctx => ctx.ctx.getMessages)
    )
  )

  val schema: Schema[MessageRepository, Unit] = Schema(query)
}
