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

  "util" should "list blobs" in {

    implicit val cfg: BlobConfig = BlobConfig(accountFQDN, clientId, authEP, clientKey, Some(path))
    implicit val azureBlobber: Blobber = new Blobber()

    new BlobPaths().toList.headOption match {
      case Some(p) =>
        println(p)
        assert(p.startsWith(path.substring(1, 4)))
      case _ => assertResult(false)(true)
    }

  }

  ignore should "read blob" in {

    implicit val cfg: BlobConfig = BlobConfig(accountFQDN, clientId, authEP, clientKey, Some(path))
    implicit val azureBlobber: Blobber = new Blobber()

    new BlobPaths().toList.headOption match {
      case Some(p) =>

        val r = new Reader(p)
        val iter = r.read()
        val records = iter.toList

        records.size should be(12404)
        records.slice(0, 10).foreach(println)

      case _ => assertResult(false)(true)
    }

  }

}
