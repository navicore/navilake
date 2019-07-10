package onextent.akka.azure.adl

import java.util.zip.ZipException

import akka.actor.{Actor, Props}
import com.typesafe.scalalogging.LazyLogging

object GzipConnector extends LazyLogging {

  val name: String = "GZipConnector"

  def props[T](implicit config: LakeConfig) = Props(new GzipConnector())
}

class GzipConnector(implicit config: LakeConfig)
    extends Actor
    with LazyLogging {

  val pathsIterator: Iterator[String] = new LakePaths().toList.iterator

  val firstPath: String = pathsIterator.next()
  logger.debug(s"reading from first path $firstPath")

  var readerIterator: Iterator[String] = new GZipReader(firstPath).read()

  def newFile(): Unit = {
    // open next file and read one
    if (pathsIterator.hasNext) {
      val nextPath = pathsIterator.next()
      logger.debug(s"reading from next path $nextPath")
      readerIterator = new GZipReader(nextPath).read()
      sender() ! readerIterator.next()
    } else {
      // all files in original path spec have been processed
      sender() ! NoMore()
    }
  }

  override def receive: Receive = {

    case _: Pull =>
      if (readerIterator.hasNext) {
        // read one from the current file
        try {
          sender() ! readerIterator.next()
        } catch {
          case e: ZipException =>
            logger.warn(s"zip error - failed file $firstPath - continuing...", e)
            newFile()
          case e: Throwable    =>
            logger.warn(s"error - failed file $firstPath - continuing...", e)
            newFile()
        }
      } else {
        // open next file and read one
        newFile()
      }

    case x => logger.error(s"I don't know how to handle ${x.getClass.getName}")

  }

}
