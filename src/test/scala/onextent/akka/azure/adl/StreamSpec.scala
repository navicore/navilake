package onextent.akka.azure.adl
import scala.language.postfixOps
import akka.Done
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.scaladsl.Sink
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.Timeout
import org.scalatest._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class StreamSpec extends FlatSpec with Matchers {

  implicit val actorSystem: ActorSystem = ActorSystem("spec")
  implicit val materializer: ActorMaterializer = ActorMaterializer(
    ActorMaterializerSettings(actorSystem))

  def requestDuration: Duration = {
    val t = "120 seconds"
    Duration(t)
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

  var count = 0
  val consumer: Sink[String, Future[Done]] = Sink.foreach(m => {
    count += 1
    println(s"$count sunk $m")
  })

  ignore should "read blobs" in {

    implicit val cfg: LakeConfig =
      LakeConfig(accountFQDN, clientId, authEP, clientKey, Some(path))
    implicit val azureBlobber: Laker = new Laker()

    val connector: ActorRef = actorSystem.actorOf(GzipConnector.props)

    val src = NaviLake(connector)
    val r: Future[Done] = src.runWith(consumer)

    Await.result(r, 10 * 60 seconds)

  }

}
