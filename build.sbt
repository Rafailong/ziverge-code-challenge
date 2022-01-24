val CirceVersion            = "0.14.1"
val ZioVersion              = "1.0.12"
val ZioLoggingVersion       = "0.5.14"
val ZioMagicVersion         = "0.3.10"
val LogBackVersion          = "1.2.7"
val RefinedVersion          = "0.9.27"
val TapirVersion            = "0.19.0"
val KindProjectorVersion    = "0.13.2"
val BetterMonadicForVersion = "0.3.1"
val ZioHttpVersion          = "1.0.0.0-RC17"
val Sttp3Version            = "3.3.16"
val PureconfigVersion       = "0.17.0"

ThisBuild / organization := "me.rafa"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.8"

lazy val zioBaseBundle = Seq(
  "dev.zio" %% "zio" % ZioVersion,
  "dev.zio" %% "zio-streams" % ZioVersion,
  "dev.zio" %% "zio-logging" % ZioLoggingVersion,
  "dev.zio" %% "zio-logging-slf4j" % ZioLoggingVersion,
  "dev.zio" %% "zio-test" % ZioVersion % Test,
  "dev.zio" %% "zio-test-sbt" % ZioVersion % Test,
  "dev.zio" %% "zio-test-magnolia" % ZioVersion % Test,
  "io.github.kitlangton" %% "zio-magic" % ZioMagicVersion,
  "dev.zio" %% "zio-interop-cats" % "3.1.1.0",
//  "dev.zio" %% "zio-prelude" % "1.0.0-RC8",
  "io.github.vigoo" %% "prox-zstream" % "0.7.3"
)

lazy val sttpBundle = Seq(
  "com.softwaremill.sttp.client3" %% "async-http-client-backend-zio" % Sttp3Version,
  "com.softwaremill.sttp.client3" %% "core" % Sttp3Version,
  "com.softwaremill.sttp.client3" %% "circe" % Sttp3Version
)

lazy val pureconfigBundle = Seq(
  "com.github.pureconfig" %% "pureconfig" % PureconfigVersion,
  "com.github.pureconfig" %% "pureconfig-sttp" % PureconfigVersion
)

lazy val tapirBundle = Seq(
  "com.softwaremill.sttp.tapir" %% "tapir-core" % TapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-zio" % TapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-zio-http4s-server" % TapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % TapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % TapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % TapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-redoc" % TapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-redoc-bundle" % TapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-refined" % TapirVersion,
  "com.softwaremill.sttp.tapir" %% "sttp-mock-server" % TapirVersion % Test,
  "com.softwaremill.sttp.tapir" %% "tapir-sttp-stub-server" % TapirVersion % Test
)

lazy val CirceBundle = Seq(
  "io.circe" %% "circe-generic" % CirceVersion,
  "io.circe" %% "circe-generic-extras" % CirceVersion,
  "io.circe" %% "circe-refined" % CirceVersion
)

lazy val RefinedBundle = Seq(
  "eu.timepit" %% "refined" % RefinedVersion,
  "eu.timepit" %% "refined-cats" % RefinedVersion
)

lazy val root = (project in file("."))
  .settings(
    name := "ziverge-challenge",
    scalacOptions += "-Ymacro-annotations",
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework")),
    Compile / mainClass := Some("ravila.ziverge.challenge.Main"),
    Compile / run / mainClass := Some("ravila.ziverge.challenge.Main"),
    libraryDependencies ++=
      zioBaseBundle ++ tapirBundle ++ sttpBundle ++ pureconfigBundle ++ RefinedBundle ++ CirceBundle ++ Seq(
        "ch.qos.logback" % "logback-classic" % LogBackVersion
      ),
    addCompilerPlugin(
      "org.typelevel" % "kind-projector" % KindProjectorVersion cross CrossVersion.full
    ),
    addCompilerPlugin(
      "com.olegpy" %% "better-monadic-for" % BetterMonadicForVersion
    )
  )
