package ravila.ziverge.challenge.api

import org.http4s.HttpRoutes
import sttp.model._
import sttp.tapir.codec.refined._
import sttp.tapir.Endpoint
import sttp.tapir.ztapir._
import sttp.tapir.json.circe._
import io.circe.generic.auto._
import ravila.ziverge.challenge.wordcounter.WordCounter
import sttp.tapir.generic.auto._
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import zio._
import zio.clock.Clock
import zio.blocking.Blocking
import ravila.ziverge.challenge.Model._

object WordCountEndpoint {

  private val outputExample: Stats =
    Map(
      "foo" -> Map("word" -> 5, "soccer" -> 1),
      "baz" -> Map("desk" -> 2),
      "bar" -> Map("ball" -> 50)
    )

  val description: Endpoint[Unit, Unit, StatusCode, Stats, Any] =
    endpoint.get
      .name("Word Count")
      .in("word" / "count")
      .out(
        jsonBody[Stats]
          .description("Word count.")
          .example(outputExample)
      )
      .errorOut(statusCode)
      .description("Word count.")

  def route(
    wordCounter: WordCounter
  ): HttpRoutes[RIO[Clock with Blocking, *]] =
    ZHttp4sServerInterpreter().from {
      description.zServerLogic { _ =>
        wordCounter
          .stats()
          .uninterruptible
      }
    }.toRoutes
}
