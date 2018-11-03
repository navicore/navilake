# UNDER CONSTRUCTION

# UNDER CONSTRUCTION

# UNDER CONSTRUCTION

# Read Azure Data Lake Storage into Akka Streams

This replays historical data-at-rest into an
existing code base that had been designed for streaming.

## USAGE

update your `build.sbt` dependencies with:

```scala
// https://mvnrepository.com/artifact/tech.navicore/navilake
libraryDependencies += "tech.navicore" %% "navilake" % "TBD"
```

This example reads gzip data from Azure blobs.

Create a config, a connector, and a source via the example below.

```scala
    val consumer = ... // some Sink
    ...
    ...
    ...
    // TODO...
    // TODO...
    // TODO...
    // TODO...
    // TODO...
    ...
    ...
    ...
    src.runWith(consumer)
    ...
    ...
    ...
```

## OPS

### publish local

```console
sbt +publishLocalSigned
```

### publish to nexus staging

```console
export GPG_TTY=$(tty)
sbt +publishSigned
sbt sonatypeReleaseAll
```

---
