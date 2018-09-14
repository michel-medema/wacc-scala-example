package rugds.wacc

import akka.actor.{Actor, ActorRef}
import rugds.wacc.WebServer.ChatMessage

class ChatRoomActor extends Actor {
	var participants: Map[String, ActorRef] = Map.empty[String, ActorRef]

	override def receive: Receive = {
		case UserJoined(name, actorRef) =>
			participants += name -> actorRef
			println(s"User $name joined channel")

		case UserLeft(name) =>
			println(s"User $name left channel")
			participants -= name

		case (user: String, msg: ChatMessage) =>
			println(s"Message $msg")
			broadcast(user, msg)
	}

	// Send message to all chat room users except the user who sent the message.
	def broadcast(user: String, message: ChatMessage): Unit = participants.filter( p => p._1 != user ).values.foreach(_ ! message)
}
