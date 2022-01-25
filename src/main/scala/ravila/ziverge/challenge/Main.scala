package ravila.ziverge.challenge

import org.http4s.server.Router
import org.http4s.blaze.server.BlazeServerBuilder
import ravila.ziverge.challenge.api.{RedocRoute, WordCountEndpoint}
import ravila.ziverge.challenge.wordcounter.{WordCounter, WordCounterLive}
import zio._
import zio.duration._
import zio.clock.Clock
import zio.blocking.Blocking
import zio.interop.catz._

import java.util.concurrent.TimeUnit

object Main extends App {

  def serve(
    wordCounter: WordCounter
  ): ZIO[ZEnv, Throwable, Unit] = {
    import cats.syntax.all._

    val routes = RedocRoute() <+> WordCountEndpoint.route(wordCounter)

    ZIO.runtime[ZEnv].flatMap {
      implicit runtime => // This is needed to derive cats-effect instances needed by http4s
        BlazeServerBuilder[RIO[Clock with Blocking, *]]
          .withExecutionContext(runtime.platform.executor.asEC)
          .bindHttp(port = 8080, host = "localhost")
          .withHttpApp(Router("/" -> routes).orNotFound)
          .serve
          .compile
          .drain
    }
  }

  def resetCounter(wordCounter: WordCounter) = {
    val windowDuration = Duration(30L, TimeUnit.SECONDS)
    val schedule       = Schedule.windowed(windowDuration)
    wordCounter.reset().delay(windowDuration).repeat(schedule)
  }

  val program = for {
    counter <- WordCounterLive.live
    f1      <- BlackboxClient.run(counter).useForever.fork
    f2      <- resetCounter(counter).fork
    _ <- serve(counter).ensuring(
      f1.join.mapError(_.toThrowable).orDie &&&
      f2.join
    )
  } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    program.exitCode
}
