import sbt.Keys._

resolvers += Resolver.mavenLocal
resolvers ++= Seq(
  "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots"
)

lazy val root = (project in file("."))
  .enablePlugins(PlayService, JavaAgent, PlayLayoutPlugin, Common, JavaAppPackaging)
  .settings(
    name := "play-scala-rest-api-example",
    version := "2.7.x",
    scalaVersion := "2.13.0",
    libraryDependencies ++= Seq(
      guice,
      "io.kamon" %% "kamon-bundle" % "2.0.2",
      "io.kamon" %% "kamon-apm-reporter" % "2.0.0",
      "org.joda" % "joda-convert" % "2.1.2",
      "net.logstash.logback" % "logstash-logback-encoder" % "5.2",
      "io.lemonlabs" %% "scala-uri" % "1.4.10",
      "net.codingwell" %% "scala-guice" % "4.2.5",
      "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test,
      "io.kamon" %% "kamon-prometheus" % "2.0.0",
      "com.newrelic.telemetry" %% "kamon-newrelic-reporter" % "0.0.3-SNAPSHOT"
    ),
      dockerExposedPorts := Seq(5266, 9000, 9001, 9095),
      dockerExposedVolumes := Seq("/opt/docker/logs")
  )

lazy val gatlingVersion = "3.1.3"
lazy val gatling = (project in file("gatling"))
  .enablePlugins(GatlingPlugin)
  .settings(
    scalaVersion := "2.12.8",
    libraryDependencies ++= Seq(
      "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion % Test,
      "io.gatling" % "gatling-test-framework" % gatlingVersion % Test
    )
  )

// Documentation for this project:
//    sbt "project docs" "~ paradox"
//    open docs/target/paradox/site/index.html
lazy val docs = (project in file("docs")).enablePlugins(ParadoxPlugin).
  settings(
    scalaVersion := "2.13.0",
    paradoxProperties += ("download_url" -> "https://example.lightbend.com/v1/download/play-rest-api")
  )
