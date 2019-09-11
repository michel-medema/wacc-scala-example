package rugds.wacc

import akka.NotUsed
import akka.actor.{ActorSystem, Props}
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.{Flow, Sink, Source}
import rugds.wacc.WebServer.ChatMessage

class ChatRoom(actorSystem: ActorSystem) {
	implicit val system: ActorSystem = ActorSystem("my-system")
	implicit val materializer: ActorMaterializer = ActorMaterializer()
	private[this] val chatRoomActor = actorSystem.actorOf(Props(classOf[ChatRoomActor]))

	private def chatInSink(user: String): Sink[(String, ChatMessage), NotUsed] = Sink.actorRef[(String, ChatMessage)](chatRoomActor, UserLeft(user))

	def websocketFlow(user: String): Flow[ChatMessage, ChatMessage, _] = {
		// Flow that receives incoming messages and sends these to the chat room actor.
		val in = Flow[ChatMessage]
  		.map( (user, _) )
			.to(chatInSink(user))

		// New source based on an actor that receives messages from the chat room actor and outputs these.
		val out = Source.actorRef[ChatMessage](1, OverflowStrategy.fail)
			.mapMaterializedValue(chatRoomActor ! UserJoined(user, _))

		Flow.fromSinkAndSource(in, out)
	}

	def sendMessage(message: ChatMessage): Unit = chatRoomActor ! message
}

object ChatRoom {
	def apply()(implicit actorSystem: ActorSystem) = new ChatRoom(actorSystem)
}
