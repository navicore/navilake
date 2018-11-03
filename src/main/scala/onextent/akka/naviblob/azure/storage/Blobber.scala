package onextent.akka.naviblob.azure.storage

import com.microsoft.azure.datalake.store.ADLStoreClient
import com.microsoft.azure.datalake.store.oauth2.ClientCredsTokenProvider
import com.typesafe.scalalogging.LazyLogging

class Blobber(implicit cfg: BlobConfig) extends LazyLogging {

  val provider = new ClientCredsTokenProvider(cfg.authTokenEndpoint, cfg.clientId, cfg.clientKey)

  val blobClient: ADLStoreClient = ADLStoreClient.createClient(cfg.accountFQDN, provider)

}
