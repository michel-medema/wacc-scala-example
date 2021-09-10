name := "WacChat"
version := "0.1"

scalaVersion := "2.13.6"

val AkkaVersion = "2.6.16"
val AkkaHttpVersion = "10.2.6"

libraryDependencies ++= Seq(
	"com.typesafe.akka" %% "akka-http"   % AkkaHttpVersion,
	"com.typesafe.akka" %% "akka-stream" % AkkaVersion,
	"com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
	"org.mongodb.scala" %% "mongo-scala-driver" % "4.3.2",
	"org.sangria-graphql" %% "sangria" % "2.1.3",
	"org.sangria-graphql" %% "sangria-spray-json" % "1.0.2"
)

Compile / mainClass := Some("rugds.wacc.WebServer")

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

dockerBaseImage       := "openjdk:11.0.12-jre-slim"
Docker / packageName  := "wacc-backend"
dockerExposedPorts 		:= Seq(8080)
dockerRepository 			:= Some("index.docker.io/michelm")
dockerUpdateLatest		:= true