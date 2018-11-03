package onextent.akka.naviblob.azure.storage

import scala.collection.JavaConverters._

class BlobPaths(implicit cfg: BlobConfig)
    extends Blobber
    with Iterable[String] {

  var paths = List()

  override def iterator: Iterator[String] = {

    cfg.path match {
      case Some(p) =>
        logger.debug(s"listing blobs at path $p")
        val list = blobClient.enumerateDirectory(p, 2000).asScala
        list.map(_.fullName).toIterator
      case _ =>
        logger.debug("listing blobs at path /")
        val list = blobClient.enumerateDirectory("/", 2000).asScala
        list.map(_.fullName).toIterator
    }

  }

}
