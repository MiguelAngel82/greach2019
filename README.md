# Java Frameworks Comparison

This project contains some frameworks (Micronaut v.1.1.RC1 and Micronaut v.1.1.RC1 + GraalVM v.1.0 RC4, Spring Boot v.2.1.3 and Vert.x v.3.6.2 at the moment). 
All the projects can be rn with the generated JAR and the Graal project with the native image. Once every project is run, they expose one endpoint for Micrometer and another to Prometheus. Then, you can configure Prometheus to listen to a particular port (the port in where every project has been run) and you are allow to show metrics. 

## Prometheus

To run prometehus in Docker:

```bash
docker run -d -p 9090:9090 --mount type=bind,source="$(pwd)"/prometheus/prometheus.yml,target=/etc/prometheus/prometheus.yml prom/prometheus --config.file=/etc/prometheus/prometheus.yml
```

You can configure `promethues.yml` file to add new artifacts to listen to.

## Micronaut

To run Micronaut (from `micronautjavaexample`):

```bash
./gradlew assemble
java -jar builds/lib/<jar_file>.jar
```

Its configured by default to start in 8090 port. 

## Micronaut + GraalVM

To run Micronaut (from `micronautgraalexample`):

```bash
./gradlew assemble
./docker-build.sh
docker run --network host micronautgraalexample
```

Its configured by default to start in 8093 port. 

## Spring Boot

To run Micronaut (from `springbootjavaexample`):

```bash
./gradlew assemble
java -jar builds/lib/<jar_file>.jar
```

Its configured by default to start in 8091 port.

## Vert.x

To run Micronaut (from `vertxjavaexample`):

```bash
./gradlew assemble
java -jar builds/lib/<jar_file>.jar
```

Its configured by default to start in 8092 port.

## Benchmark

There are some scripts called `check*.sh` that execute a call to the every framework endpoint in a loop with an elapsed time between on call and another.