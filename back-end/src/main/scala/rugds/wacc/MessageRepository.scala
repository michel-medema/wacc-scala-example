package rugds.wacc

import rugds.wacc.WebServer.ChatMessage

class MessageRepository {
  import MessageRepository._

  def getMessages: List[ChatMessage] = {
    messages
  }
}

object MessageRepository {
  val messages: List[ChatMessage] = List(
    ChatMessage("Test User", "A message!")
  )
}