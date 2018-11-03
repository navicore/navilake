package onextent.akka.naviblob.azure.storage

case class BlobConfig(accountFQDN: String, clientId: String, authTokenEndpoint: String, clientKey: String, path: Option[String])

