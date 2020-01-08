// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.7.3")

// sbt-paradox, used for documentation
addSbtPlugin("com.lightbend.paradox" % "sbt-paradox" % "0.4.4")

// Load testing tool:
// http://gatling.io/docs/2.2.2/extensions/sbt_plugin.html
addSbtPlugin("io.gatling" % "gatling-sbt" % "3.0.0")

// Scala formatting: "sbt scalafmt"
addSbtPlugin("com.lucidchart" % "sbt-scalafmt" % "1.15")

//kamon instrumentation
addSbtPlugin("io.kamon" % "sbt-kanela-runner-play-2.7" % "2.0.2")

// Dockerizing app
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.4.1")
