package onextent.akka.azure.adl.cli
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.Timeout

import scala.concurrent.duration._
object Conf {

  implicit val actorSystem: ActorSystem = ActorSystem("spec")
  implicit val materializer: ActorMaterializer = ActorMaterializer(
    ActorMaterializerSettings(actorSystem))

  val timeout: String = sys.env.getOrElse("DATALAKE_TIMEOUT", "180 seconds")
  def requestDuration: Duration = {
    Duration(timeout)
  }
  implicit def requestTimeout: Timeout = {
    val d = requestDuration
    FiniteDuration(d.length, d.unit)
  }

  val accountFQDN: String = sys.env.getOrElse("DATALAKE_URL", "unknown")
  val clientId: String = sys.env.getOrElse("DATALAKE_CLIENT_ID", "unknown")
  val clientKey: String = sys.env.getOrElse("DATALAKE_CLIENT_SECRET", "unknown")
  val authEP: String = sys.env.getOrElse("DATALAKE_AUTH_ENDPOINT", "unknown")
  val path: String = sys.env.getOrElse("DATALAKE_PATH", "/")
  val outputFolder: String = sys.env.getOrElse("DATALAKE_OUTPUT_FOLDER", "")
  val outputFile: String = sys.env.getOrElse("DATALAKE_OUTPUT_FILENAME", "out.csv.gz")
  val keyPath: String = sys.env.getOrElse("KEY_JSONPATH", "$.key")
  val valuePath: String = sys.env.getOrElse("VALUE_JSONPATH", "$.value")

  val outputStream = LakeOutputStream(outputFolder, outputFile)
  outputStream.println("key, value")

}
