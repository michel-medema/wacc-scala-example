name := "WacChat"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
	"com.typesafe.akka" %% "akka-http"   % "10.1.9",
	"com.typesafe.akka" %% "akka-stream" % "2.5.25",
	"com.typesafe.akka" %% "akka-http-spray-json" % "10.1.9",
	"org.mongodb.scala" %% "mongo-scala-driver" % "2.7.0",
	"org.sangria-graphql" %% "sangria" % "1.4.2",
	"org.sangria-graphql" %% "sangria-spray-json" % "1.0.1"
)

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

dockerBaseImage       := "openjdk:jre"
packageName in Docker := "wacc-backend"
dockerExposedPorts := Seq(8080)