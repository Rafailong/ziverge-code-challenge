package ravila.ziverge

import io.circe.generic.extras.Configuration

package object challenge {

  implicit val circeConfig: Configuration =
    Configuration.default.withSnakeCaseMemberNames

}
