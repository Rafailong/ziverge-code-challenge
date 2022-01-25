package ravila.ziverge.challenge.wordcounter

import ravila.ziverge.challenge.Model.{BlackBoxInput, Stats}
import zio._

trait WordCounter {

  def count(input: BlackBoxInput): UIO[Unit]

  def stats(): UIO[Stats]

  def reset(): UIO[Unit]
}

object WordCounter {

  def count(input: BlackBoxInput): ZIO[Has[WordCounter], Nothing, Unit] =
    ZIO.serviceWith[WordCounter](_.count(input))
}
