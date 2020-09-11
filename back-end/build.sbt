name := "WacChat"
version := "0.1"

scalaVersion := "2.13.3"

val AkkaVersion = "2.6.9"
val AkkaHttpVersion = "10.2.0"

libraryDependencies ++= Seq(
	"com.typesafe.akka" %% "akka-http"   % AkkaHttpVersion,
	"com.typesafe.akka" %% "akka-stream" % AkkaVersion,
	"com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
	"org.mongodb.scala" %% "mongo-scala-driver" % "4.1.0",
	"org.sangria-graphql" %% "sangria" % "2.0.0",
	"org.sangria-graphql" %% "sangria-spray-json" % "1.0.2"
)

mainClass in Compile := Some("rugds.wacc.WebServer")

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

dockerBaseImage       := "openjdk:jre"
packageName in Docker := "wacc-backend"
dockerExposedPorts 		:= Seq(8080)
dockerRepository 			:= Some("index.docker.io/michelm")
dockerUpdateLatest		:= true