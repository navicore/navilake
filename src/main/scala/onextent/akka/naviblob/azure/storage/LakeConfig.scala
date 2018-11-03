package onextent.akka.naviblob.azure.storage

case class LakeConfig(accountFQDN: String, clientId: String, authTokenEndpoint: String, clientKey: String, path: Option[String])

