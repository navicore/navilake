name := "NaviLake"
organization := "tech.navicore"
javacOptions ++= Seq("-source", "1.8", "-target", "1.8") 
scalacOptions ++= Seq(
  "-target:jvm-1.8"
)
fork := true
javaOptions in test ++= Seq(
  "-Xms512M", "-Xmx2048M",
  "-XX:MaxPermSize=2048M",
  "-XX:+CMSClassUnloadingEnabled"
)

parallelExecution in test := false

val akkaVersion = "2.6.20"

val scala212 = "2.12.17"
val scala213 = "2.13.10"

crossScalaVersions := Seq(scala212, scala213)

inThisBuild(List(
  organization := "tech.navicore",
  homepage := Some(url("https://github.com/navicore/navilake")),
  licenses := List("MIT" -> url("https://github.com/navicore/navilake/blob/master/LICENSE")),
  developers := List(
    Developer(
      "navicore",
      "Ed Sweeney",
      "ed@onextent.com",
      url("https://navicore.tech")
    )
  )
))

libraryDependencies ++=
  Seq(

    "tech.navicore" %% "navipath" % "4.1.3",
    
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",

    "com.microsoft.azure" % "azure-data-lake-store-sdk" % "2.3.10",

    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,

    "org.scalatest" %% "scalatest" % "3.2.14" % "test"
  )

dependencyOverrides ++= Seq(
  "com.typesafe.akka" %% "akka-actor"  % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion
)

mainClass in assembly := Some("onextent.akka.azure.adl.cli.Main")
assemblyJarName in assembly := "NaviLake.jar"

assemblyMergeStrategy in assembly := {
  case PathList("reference.conf") => MergeStrategy.concat
  case x if x.endsWith("io.netty.versions.properties") => MergeStrategy.first
  case PathList("META-INF", _ @ _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}

