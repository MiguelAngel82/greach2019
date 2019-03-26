FROM oracle/graalvm-ce:1.0.0-rc13 as graalvm
COPY . /home/app/micronautgraalexample
WORKDIR /home/app/micronautgraalexample
RUN native-image --no-server -cp build/libs/micronautgraalexample-*.jar

FROM frolvlad/alpine-glibc
EXPOSE 8080
COPY --from=graalvm /home/app/micronautgraalexample .
ENTRYPOINT ["./micronautgraalexample"]
