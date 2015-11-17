name := "ScalaRest"

version := "1.0"

scalaVersion := "2.11.7"

scalacOptions in ThisBuild := Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8", "-Xlint")

val sprayVersion = "1.3.2"

libraryDependencies ++= Seq(
    "org.scala-lang.modules" %% "scala-async" % "0.9.5"
  , "com.typesafe.akka" %% "akka-actor" % "2.4.0"
  , "com.typesafe.akka" %% "akka-http-experimental" % "1.0"
  , "io.spray" %% "spray-client" % sprayVersion
  , "io.spray" %% "spray-json" % "1.3.2"
  , "io.spray" %% "spray-httpx" % sprayVersion
  , "io.spray" %% "spray-routing" % sprayVersion
  , "io.spray" %% "spray-testkit" % sprayVersion % "test"
  , "org.json4s" %% "json4s-jackson" % "3.2.11"
  , "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2"
  , "ch.qos.logback" % "logback-classic" % "1.1.3"
  , "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)
