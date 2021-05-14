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

val akkaVersion = "2.6.14"

val scala212 = "2.12.13"
val scala213 = "2.13.5"

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

    "tech.navicore" %% "navipath" % "4.0.2",
    
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.3",

    "com.microsoft.azure" % "azure-data-lake-store-sdk" % "2.3.9",

    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,

    "org.scalatest" %% "scalatest" % "3.2.9" % "test"
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

