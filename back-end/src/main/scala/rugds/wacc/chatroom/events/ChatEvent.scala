package rugds.wacc.chatroom.events

import akka.actor.ActorRef

sealed trait ChatEvent

final case class UserJoined(name: String, actor: ActorRef) extends ChatEvent
final case class UserLeft(name: String) extends ChatEvent