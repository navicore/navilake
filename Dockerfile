FROM hseeberger/scala-sbt:8u212_1.2.8_2.12.8 as builder
WORKDIR /build
# Cache dependencies first
COPY project project
COPY build.sbt .
RUN sbt update
# Then build
COPY . .
RUN sbt assembly

#FROM openjdk:8u181-jre-slim
FROM openjdk:8-jdk-slim
WORKDIR /app
COPY --from=builder /build/target/scala-2.12/*.jar .
CMD ["java", "-jar", "./NaviLake.jar", "-Xms4096M", "-Xmx8192M"]
