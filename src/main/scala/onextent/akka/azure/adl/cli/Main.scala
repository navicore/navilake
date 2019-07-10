package onextent.akka.azure.adl.cli

import akka.Done
import akka.actor.ActorRef
import akka.stream.scaladsl.Sink
import navicore.data.navipath.dsl.NaviPathSyntax._
import onextent.akka.azure.adl.cli.Conf._
import onextent.akka.azure.adl.{GzipConnector, LakeConfig, Laker, NaviLake}

import scala.concurrent.Future

object Main extends App {

  var count: Int = 0
  val consumer: Sink[String, Future[Done]] = Sink.foreach(m => {
    val keyO = m.query[String](keyPath)
    val valueO = m.query[String](valuePath)
    (keyO, valueO) match {
      case (Some(key), Some(value)) =>
        outputStream.println(s"$key, $value")
        count = count + 1
        val d = count % 10000
        if (d == 0) {
          println(s"$count, $key, $value")
          outputStream.flush()
        }
      case _ => // noop
        println(s"skipped")
    }
  })

  implicit val cfg: LakeConfig =
    LakeConfig(accountFQDN, clientId, authEP, clientKey, Some(path))
  implicit val azureBlobber: Laker = new Laker()

  val connector: ActorRef = actorSystem.actorOf(GzipConnector.props)

  val src = NaviLake(connector)
  src.runWith(consumer)

}
