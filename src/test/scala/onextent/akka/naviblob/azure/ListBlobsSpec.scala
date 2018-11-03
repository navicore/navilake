package onextent.akka.naviblob.azure

import com.typesafe.scalalogging.LazyLogging
import onextent.akka.naviblob.azure.avro.Reader
import onextent.akka.naviblob.azure.storage.{BlobConfig, BlobPaths, Blobber}
import org.scalatest._

class ListBlobsSpec extends FlatSpec with Matchers with LazyLogging {

  val accountFQDN: String = sys.env.getOrElse("DATALAKE_URL", "unknown")
  val clientId: String = sys.env.getOrElse("DATALAKE_CLIENT_ID", "unknown")
  val clientKey: String = sys.env.getOrElse("DATALAKE_CLIENT_SECRET", "unknown")
  val authEP: String = sys.env.getOrElse("DATALAKE_AUTH_ENDPOINT", "unknown")
  val path: String = sys.env.getOrElse("DATALAKE_PATH", "/")

  "lister" should "list blobs" in {

    implicit val cfg: BlobConfig = BlobConfig(accountFQDN, clientId, authEP, clientKey, Some(path))
    implicit val azureBlobber: Blobber = new Blobber()

    new BlobPaths().toList.headOption match {
      case Some(p) =>
        logger.info(s"listed: $p")
        assert(p.startsWith(path.substring(0, 4)))
      case _ => assertResult(false)(true)
    }

  }

  "reader" should "read blob" in {

    implicit val cfg: BlobConfig = BlobConfig(accountFQDN, clientId, authEP, clientKey, Some(path))
    implicit val azureBlobber: Blobber = new Blobber()

    new BlobPaths().toList.headOption match {
      case Some(p) =>
        logger.info(s"reading: $p")

        val r = new Reader(s"/$p")
        val iter = r.read()
        val records = iter.toList

        records.size should be(66894)
        records.slice(0, 10).foreach(println)

      case _ => assertResult(false)(true)
    }

  }

}
