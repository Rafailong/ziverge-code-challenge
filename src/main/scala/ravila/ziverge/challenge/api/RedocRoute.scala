package ravila.ziverge.challenge.api

import org.http4s.HttpRoutes
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.openapi._
import sttp.tapir.openapi.circe.yaml._
import sttp.tapir.redoc.Redoc
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import zio._
import zio.blocking.Blocking
import zio.clock.Clock

object RedocRoute {

  def apply(): HttpRoutes[ZIO[Has[Clock.Service] with Has[Blocking.Service], Throwable, *]] = {
    val openApiDocs: OpenAPI =
      OpenAPIDocsInterpreter()
        .toOpenAPI(
          es      = List(WordCountEndpoint.description),
          title   = "Ziverge - Code Challenge",
          version = "1.0"
        )
    ZHttp4sServerInterpreter()
      .from(
        Redoc[Task](
          title = "Ziverge - Code Challenge",
          yaml  = openApiDocs.toYaml
        )
      )
      .toRoutes
  }
}
