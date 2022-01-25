package ravila.ziverge.challenge

import eu.timepit.refined.auto.autoUnwrap
import eu.timepit.refined.types.all.{NonEmptyString, NonNegLong}
import io.circe._
import io.circe.generic.extras.ConfiguredJsonCodec
import io.circe.refined._

import java.time.Instant

object Model {

  @ConfiguredJsonCodec
  case class BlackBoxInput(
    eventType: NonEmptyString,
    data: NonEmptyString,
    timestamp: NonNegLong
  ) {

    lazy val timestampAsInstant: Instant =
      Instant.ofEpochSecond(timestamp)
  }

  type Stats = Map[String, Map[String, Long]]

  object Stats {

    def empty: Stats = Map.empty

    private implicit val innerMapEncoder: Encoder[Map[String, Long]] =
      Encoder.encodeMap[String, Long]

    private implicit val innerMapDecoder: Decoder[Map[String, Long]] =
      Decoder.decodeMap[String, Long]

    implicit val statsEncoder: Encoder[Map[String, Map[String, Long]]] =
      Encoder.encodeMap[String, Map[String, Long]]

    implicit val statsDecoder: Decoder[Map[String, Map[String, Long]]] =
      Decoder.decodeMap[String, Map[String, Long]]
  }

}
