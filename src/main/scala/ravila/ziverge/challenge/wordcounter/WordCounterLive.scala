package ravila.ziverge.challenge.wordcounter

import cats.syntax.option._
import eu.timepit.refined.auto.autoUnwrap
import ravila.ziverge.challenge.Model._
import ravila.ziverge.challenge.wordcounter.WordCounterLive.Key
import zio._

case class WordCounterLive private (
  storage: Ref[Map[Key, Long]]
) extends WordCounter {

  override def count(input: BlackBoxInput): UIO[Unit] = {
    storage.update { map =>
      val key = Key(input.eventType, input.data)
      map.updatedWith(key) {
        case Some(n) => (n + 1).some
        case None    => Some(1)
      }
    }
  }

  override def stats(): UIO[Stats] =
    storage.get.map {
      _.foldLeft(Stats.empty) {
        case (acc, (Key(eventType, word), count)) =>
          acc.updatedWith(eventType) {
            case Some(map) =>
              map
                .updatedWith(word) {
                  case Some(n) => (n + count).some
                  case None    => count.some
                }
                .some

            case None => Map(word -> count).some
          }
      }
    }

  override def reset(): UIO[Unit] =
    storage.update(_ => Map.empty)
}

object WordCounterLive {

  private[wordcounter] case class Key(eventType: String, word: String)

  val live: UIO[WordCounter] = {
    Ref
      .make[Map[Key, Long]](Map.empty)
      .map(WordCounterLive(_))
  }
}
