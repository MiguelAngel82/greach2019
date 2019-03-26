package es.salenda.vertx.java.example;

import io.reactivex.Single;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;

public class BintrayClient {

    Single<String> fetchPackages() {
        WebClientOptions options = new WebClientOptions().setSsl(true).setTrustAll(true);
        Vertx vertx = Vertx.vertx();
        WebClient client = WebClient.create(vertx, options);
        return client
                .get(443, "bintray.com", "/api/v1/repos/micronaut/profiles/packages")
                .rxSend()
                .map(HttpResponse::bodyAsString);
    }

    Future<JsonArray> fetchPackagesNotReactive() {
        WebClientOptions options = new WebClientOptions().setSsl(true).setTrustAll(true);

        io.vertx.core.Vertx vertx = io.vertx.core.Vertx.vertx();
        io.vertx.ext.web.client.WebClient client = io.vertx.ext.web.client.WebClient.create(vertx, options);
        final JsonArray[] body = new JsonArray[1];
        Future<JsonArray> future = Future.future();

        client
                .get(443, "bintray.com", "/api/v1/repos/micronaut/profiles/packages")
                .send(ar -> {
                    if (ar.succeeded()) {
                        io.vertx.ext.web.client.HttpResponse<Buffer> response = ar.result();
                        future.complete(response.bodyAsJsonArray());
                    } else {
                    }
                });
        return future;
    }
}
