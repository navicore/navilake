package onextent.akka.azure.adl

case class LakeConfig(accountFQDN: String, clientId: String, authTokenEndpoint: String, clientKey: String, path: Option[String])
