package onextent.akka.azure.adl

import com.microsoft.azure.datalake.store.{DirectoryEntry, DirectoryEntryType}

import scala.collection.JavaConverters._

class LakePaths(implicit cfg: LakeConfig)
    extends Laker
    with Iterable[String] {

  private def getFullNames(path: String): List[String] = {

    logger.debug(s"listing blobs at path $path")

    val list: List[DirectoryEntry] = blobClient.enumerateDirectory(path, 2000).asScala.toList

    list.flatMap(i => {
      i.`type` match {
        case DirectoryEntryType.DIRECTORY => getFullNames(i.fullName)
        case _ => List(i.fullName)
      }
    })

  }

  override def iterator: Iterator[String] = {

    cfg.path match {

      case Some(p) =>
        logger.debug(s"listing blobs at path $p")
        val list: List[DirectoryEntry] = blobClient.enumerateDirectory(p, 2000).asScala.toList
        list.flatMap(i => {
          i.`type` match {
            case DirectoryEntryType.DIRECTORY => getFullNames(i.fullName)
            case _ => List(i.fullName)
          }
        }).toIterator

      case _ =>
        logger.debug("listing blobs at path /")
        val list = blobClient.enumerateDirectory("/", 2000).asScala
        list.map(_.fullName).toIterator

    }

  }

}
