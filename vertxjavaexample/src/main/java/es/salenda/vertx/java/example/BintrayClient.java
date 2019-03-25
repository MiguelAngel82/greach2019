package es.salenda.vertx.java.example;

import io.reactivex.Single;
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
}
