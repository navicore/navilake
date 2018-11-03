[![Build Status](https://travis-ci.org/navicore/navilake.svg?branch=master)](https://travis-ci.org/navicore/navilake)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/1901174b92304a8d98ce2d8b64f4d9dc)](https://www.codacy.com/app/navicore/navilake?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=navicore/navilake&amp;utm_campaign=Badge_Grade)

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
