package rugds.wacc

import akka.actor.ActorRef

case class UserJoined(name: String, actor: ActorRef) extends ChatEvent