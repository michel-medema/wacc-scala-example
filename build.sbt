name := "WacChat"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
	"com.typesafe.akka" %% "akka-http"   % "10.1.5",
	"com.typesafe.akka" %% "akka-stream" % "2.5.16",
	"com.typesafe.akka" %% "akka-http-spray-json" % "10.1.5",
	"org.mongodb.scala" %% "mongo-scala-driver" % "2.4.1"
)

enablePlugins(JavaAppPackaging)

packageName in Docker := "wacc-backend"
dockerExposedPorts := Seq(8080)