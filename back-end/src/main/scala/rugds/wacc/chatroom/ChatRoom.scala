package rugds.wacc.chatroom

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Sink, Source}
import rugds.wacc.WebServer.ChatMessage

class ChatRoom(actorSystem: ActorSystem) {
	private[this] val chatRoomActor = actorSystem.actorOf(ChatRoomActor.props())

	private def chatInSink(user: String): Sink[(String, ChatMessage), NotUsed] =
		Sink.actorRef[(String, ChatMessage)](chatRoomActor, ChatRoomActor.UserLeft(user))

	def webSocketFlow(user: String): Flow[ChatMessage, ChatMessage, _] = {
		// Flow that receives incoming messages and sends these to the chat room actor.
		val in = Flow[ChatMessage].map( (user, _) ).to(chatInSink(user))

		// Independent source that receives messages from chat room actor and outputs them.
		val out = Source
			.actorRef[ChatMessage](10, OverflowStrategy.fail)
			.mapMaterializedValue(chatRoomActor ! ChatRoomActor.UserJoined(user, _))

		Flow.fromSinkAndSource(in, out)
	}
}

object ChatRoom {
	def apply()(implicit actorSystem: ActorSystem ) = new ChatRoom(actorSystem)
}
