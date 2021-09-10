package rugds.wacc.chatroom

import akka.actor.{Actor, ActorRef, Props}
import rugds.wacc.WebServer.ChatMessage

object ChatRoomActor {
	sealed trait ChatEvent
	final case class UserJoined(name: String, actor: ActorRef) extends ChatEvent
	final case class UserLeft(name: String) extends ChatEvent

	def props(): Props = Props(new ChatRoomActor())
}

class ChatRoomActor extends Actor {
	import ChatRoomActor._

	override def receive: Receive = receive( Map.empty )

	private def receive( participants: Map[String, ActorRef] ): Receive = {
		case UserJoined(name, actorRef) =>
			println(s"User $name joined channel")
			context become receive( participants.+( name -> actorRef ) )

		case UserLeft(name) =>
			println(s"User $name left channel")
			context become receive( participants.-( name ) )

		case (user: String, message: ChatMessage) =>
			println(s"Message $message")
			// Send message to all chat room users except the user who sent the message.
			participants.-(user).values.foreach(_ ! message)
	}
}
