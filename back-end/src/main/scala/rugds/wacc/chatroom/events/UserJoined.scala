package rugds.wacc.chatroom.events

import akka.actor.ActorRef

case class UserJoined(name: String, actor: ActorRef) extends ChatEvent
