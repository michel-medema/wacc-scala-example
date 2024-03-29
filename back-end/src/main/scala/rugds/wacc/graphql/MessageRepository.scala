package rugds.wacc.graphql

import rugds.wacc.WebServer.ChatMessage

class MessageRepository {
  import MessageRepository._

  def getMessages: List[ChatMessage] = messages
}

object MessageRepository {
  val messages: List[ChatMessage] = List(
    ChatMessage("Test User", "A message!"),
    ChatMessage("User A", "Hello World!"),
    ChatMessage("User B", "Hello Back!"),
    ChatMessage("User A", "Goodbye!")
  )
}