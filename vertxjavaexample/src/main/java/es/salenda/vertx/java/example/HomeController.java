package es.salenda.vertx.java.example;

import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.reactivex.Single;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;
import io.vertx.micrometer.backends.BackendRegistries;
import org.apache.commons.lang3.time.StopWatch;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeController extends AbstractVerticle {
    private Timer timerFibonacci, timerWavToMp3, timerAsteranksReactive, timerAsterankNotReactive;
    private TestService testService;
    private AsterankClient asterankClient;

    /**
     * First of all, a watch is started to measure the start up time.
     * Then, Micrometer options are set, to get all available metrics. Prometheus is also configured.
     * Routers to all the endpoints are set and also timers for Micrometer are created.
     * @param fut
     */
    @Override
    public void start(Future<Void> fut) {
        StopWatch watch = new StopWatch();
        watch.start();
        testService = new TestServiceImpl();
        asterankClient = new AsterankClient();

        MicrometerMetricsOptions options = new MicrometerMetricsOptions()
                .setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true))
                .setEnabled(true);
        Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(options));

        PrometheusMeterRegistry registry = (PrometheusMeterRegistry) BackendRegistries.getDefaultNow();
        new ClassLoaderMetrics().bindTo(registry);
        new JvmMemoryMetrics().bindTo(registry);
        new JvmGcMetrics().bindTo(registry);
        new ProcessorMetrics().bindTo(registry);
        new JvmThreadMetrics().bindTo(registry);

        Router router = Router.router(vertx);

        router.get("/fibonacci").handler(this::fibonacci);

        router.get("/wavToMp3").handler(this::wavToMp3);

        router.get("/asterank-reactive").handler(this::asteranksReactive);

        router.get("/asterank-not-reactive").handler(this::asteranksNotReactive);

        router.route("/metrics").handler(ctx -> {
            String response = registry.scrape();
            ctx.response().end(response);
        });
        timerFibonacci = Timer
                .builder("resource.fibonacci")
                .register(registry);

        timerWavToMp3 = Timer
                .builder("resource.wavToMp3")
                .register(registry);

        timerAsteranksReactive = Timer
                .builder("resource.asterank.reactive")
                .register(registry);

        timerAsterankNotReactive = Timer
                .builder("resource.asterank.not.reactive")
                .register(registry);

        vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .listen(
                        config().getInteger("http.port", 8092),
                        result -> {
                            watch.stop();
                            System.out.println("Startup completed in: " + watch.getTime() + "ms");
                            if (result.succeeded()) {
                                fut.complete();
                            } else {
                                fut.fail(result.cause());
                            }
                        }
                );
    }

    /**
     * Handler to get Fibonacci response.
     * @param routingContext
     */
    private void fibonacci(RoutingContext routingContext) {
        timerFibonacci.record(() -> {
            Integer series = Integer.valueOf(routingContext.request().getParam("series"));
            routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(this.getFibonacciSeries(series)));
        });
    }

    /**
     * Handler to get wav to mp3 transformation response.
     * @param routingContext
     */
    private void wavToMp3(RoutingContext routingContext) {
        timerWavToMp3.record(() -> {
            routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(this.wavToMp3()));
        });
    }

    /**
     * Handler to get asteranks API reactive response. The response is converted from the JSON array to a Asterank objects list.
     * @param routingContext
     */
    private void asteranksReactive(RoutingContext routingContext) {
        timerAsteranksReactive.record(() -> {
            Single<String> single = asterankClient.fetchAsteroids();
            single.subscribe(response -> {
                JsonArray data = new JsonArray(response);
                List<String> asteranksList;
                asteranksList = data.stream().map(object -> {
                    JsonObject asterankJson = (JsonObject) object;
                    Asterank asterank = createAsterank(asterankJson);
                    return asterank;
                }).map(object -> object.readable_des).collect(Collectors.toList());
                routingContext.response().putHeader("content-type", "application/json; charset=utf-8").end(Json.encodePrettily(asteranksList));
            });
        });
    }

    /**
     * Handler to get asteranks API response. The response is converted from the JSON array to a Asterank objects list.
     * @param routingContext
     */
    private void asteranksNotReactive(RoutingContext routingContext) {
        timerAsterankNotReactive.record(() -> {
            Future<JsonArray> response = asterankClient.fetchAsteroidsNotReactive();
            response.setHandler(handler -> {
                if (handler.succeeded()) {
                    JsonArray data = handler.result();
                    List<String> asteranksList;
                    asteranksList = data.stream().map(object -> {
                        JsonObject asterankJson = (JsonObject) object;
                        Asterank asterank = createAsterank(asterankJson);
                        return asterank;
                    }).map(object -> object.readable_des).collect(Collectors.toList());
                    routingContext.response().putHeader("content-type", "application/json; charset=utf-8").end(Json.encodePrettily(asteranksList));
                }
            });
        });
    }

    /**
     * Helper method to create an Asterank object from a JSON object.
     * @param asterankJson JSON object.
     * @return An Asterank object.
     */
    private Asterank createAsterank(JsonObject asterankJson) {
        return new Asterank(
                asterankJson.getFloat("rms"),
                asterankJson.getString("epoch"),
                asterankJson.getString("readable_des"),
                asterankJson.getFloat("h"),
                asterankJson.getInteger("num_obs"),
                asterankJson.getString("ref"),
                asterankJson.getFloat("g"),
                asterankJson.getString("last_obs"),
                asterankJson.getString("comp"),
                asterankJson.getFloat("m"),
                asterankJson.getString("u"),
                asterankJson.getFloat("e"),
                asterankJson.getFloat("a"),
                asterankJson.getFloat("om"),
                asterankJson.getString("pert_p"),
                asterankJson.getFloat("d"),
                asterankJson.getFloat("i"),
                asterankJson.getString("des"),
                asterankJson.getString("flags"),
                asterankJson.getInteger("num_opp"),
                asterankJson.getFloat("w"),
                asterankJson.getString("pert_c")
        );
    }

    /**
     * Helper method to get the list of Fibonacci numbers specified by 'series'.
     * @param series Fibonacci numbers to get.
     * @return Fibonacci numbers list.
     */
    private List<BigInteger> getFibonacciSeries(Integer series) {
        List<BigInteger> numbers = new ArrayList<BigInteger>();
        for (int i = 0; i < series; i++) {
            numbers.add(testService.fibonacci(i));
        }
        return numbers;

    }

    /**
     * Helper method to specify the main wav File and the output mp3 file.
     * @return A string with convertion result.
     */
    private String wavToMp3() {
        File wavFile = new File("src/main/resources/test.wav");
        File mp3File = new File("src/main/resources/test.mp3");
        try {
            testService.wavToMp3(wavFile, mp3File);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "File converted";
    }
}
