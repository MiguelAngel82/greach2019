FROM oracle/graalvm-ce:1.0.0-rc13 as graalvm
COPY . /home/app/micronautgraalhelloworld
WORKDIR /home/app/micronautgraalhelloworld
RUN native-image --no-server -cp build/libs/micronautgraalhelloworld-*.jar

FROM frolvlad/alpine-glibc
EXPOSE 8080
COPY --from=graalvm /home/app/micronautgraalhelloworld .
ENTRYPOINT ["./micronautgraalhelloworld"]
