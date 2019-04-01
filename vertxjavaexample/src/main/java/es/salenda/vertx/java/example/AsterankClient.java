package es.salenda.vertx.java.example;

import io.reactivex.Single;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;

public class AsterankClient {

    /**
     * Gets API response using reactive client.
     * @return A Single object with API response.
     */
    Single<String> fetchAsteroids() {
        Vertx vertx = Vertx.vertx();
        WebClient client = WebClient.create(vertx);
        return client
                .get(80, "www.asterank.com", "/api/mpc?limit=10")
                .rxSend()
                .map(HttpResponse::bodyAsString);
    }

    /**
     * Gets API response using regular client. If the response is successful, a Future object is created with the
     * response.
     * @return A Future Object with a JsonArray (response of the request).
     */
    Future<JsonArray> fetchAsteroidsNotReactive() {
        io.vertx.core.Vertx vertx = io.vertx.core.Vertx.vertx();
        io.vertx.ext.web.client.WebClient client = io.vertx.ext.web.client.WebClient.create(vertx);
        Future<JsonArray> future = Future.future();
        client
                .get(80, "www.asterank.com", "/api/mpc?limit=10")
                .send(ar -> {
                    if (ar.succeeded()) {
                        io.vertx.ext.web.client.HttpResponse<Buffer> response = ar.result();
                        future.complete(response.bodyAsJsonArray());
                    }
                });
        return future;
    }
}
