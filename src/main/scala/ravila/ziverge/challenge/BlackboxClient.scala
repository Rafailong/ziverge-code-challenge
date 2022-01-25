package ravila.ziverge.challenge

import io.circe.parser._
import io.github.vigoo.prox.zstream._
import io.github.vigoo.prox.{zstream, _}
import ravila.ziverge.challenge.Model.BlackBoxInput
import ravila.ziverge.challenge.wordcounter.WordCounter
import zio._
import zio.blocking.Blocking
import zio.stream._

// improving opportunity
// refactor so `run` return i.e. URIO[WordCounter with ProcessRunner with Logger, ProxyResource]]
// and log decoding error in the `sink`
object BlackboxClient {

  private val transform: ZTransducer[Any, Nothing, Byte, String] =
    ZTransducer.utfDecode >>> ZTransducer.splitLines

  private def sink(
    wordCounter: WordCounter
  ): ZSink[Any, ProxError, String, String, Unit] =
    ZSink.foreach[Any, ProxError, String] { str =>
      decode[BlackBoxInput](str) match {
        case Left(_)      => ZIO.unit
        case Right(input) => wordCounter.count(input)
      }
    }

  def run(wordCounter: WordCounter)(implicit
    runner: ProcessRunner[JVMProcessInfo] = new JVMProcessRunner
  ) =
    Process("blackbox.macosx")
      .toSink(TransformAndSink[Byte, String](transform, sink(wordCounter)))
      .start()
}
