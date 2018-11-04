package onextent.akka.azure.adl

import java.io.{BufferedReader, InputStreamReader}
import java.util.zip.GZIPInputStream

import com.microsoft.azure.datalake.store.ADLFileInputStream

class GZipReader(path: String)(implicit cfg: LakeConfig)
    extends Laker {

  //val blob: CloudBlockBlob = container.getBlockBlobReference(path)

  def read(): Iterator[String] = {

    val bis: ADLFileInputStream = blobClient.getReadStream(path)

    val gis= new GZIPInputStream(bis)
    val decoder = new InputStreamReader(gis, "UTF8")

    val b = new BufferedReader(decoder)

    class LineIterator(b: BufferedReader) extends Iterator[String] {

      override def hasNext: Boolean = b.ready()

      override def next(): String = b.readLine()
    }

    new LineIterator(b)

  }

}
