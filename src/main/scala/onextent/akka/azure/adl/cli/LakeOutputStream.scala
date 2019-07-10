package onextent.akka.azure.adl.cli

import java.io.PrintStream
import java.util.zip.GZIPOutputStream

import com.microsoft.azure.datalake.store.oauth2.ClientCredsTokenProvider
import com.microsoft.azure.datalake.store.{ADLStoreClient, IfExists}
import com.typesafe.scalalogging.LazyLogging
import onextent.akka.azure.adl.cli.Conf._

import scala.util.{Failure, Success, Try}

object LakeOutputStream extends LazyLogging {

  def apply(folder: String, filename: String): PrintStream = {

    Try {
      val provider =
        new ClientCredsTokenProvider(authEP, clientId, clientKey)
      val client = ADLStoreClient.createClient(accountFQDN, provider)

      val location = folder + "/" + filename
      val stream = if (client.checkExists(location)) {
        logger.debug(s"opening stream to append to existing file $location")
        Try(
          client.getAppendStream(location)
        ) match {
          case Success(s) =>
            logger.debug(s"opened stream to append to existing file $location")
            s
          case Failure(e) =>
            val moreFilename = folder + "/_more_" + System
              .currentTimeMillis() + filename
            logger.warn(
              s"can not append to existing file $location due to $e. creating $moreFilename file.")
            val s = client.createFile(moreFilename, IfExists.FAIL)
            logger.debug(s"opened stream to _more file $moreFilename")
            s
        }
      } else {
        logger.debug(s"opening stream to create new file $location")
        client.createFile(location, IfExists.FAIL)
      }

      val gzipStream = new GZIPOutputStream(stream, true)
      new PrintStream(gzipStream)
    } match {
      case Success(ps) =>
        logger.debug(s"opened new stream to $folder $filename")
        ps
      case Failure(e) =>
        logger.error(s"can not open new stream to $folder $filename due to $e",
                     e)
        throw e
    }

  }

}
