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
    private Timer timerFibonacci, timerWavToMp3, timerPackagesReactive, timerPackagesNotReactive;
    private TestService testService;
    private BintrayClient bintrayClient;

    @Override
    public void start(Future<Void> fut) {
        StopWatch watch = new StopWatch();
        watch.start();
        testService = new TestServiceImpl();
        bintrayClient = new BintrayClient();

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

        router.get("/packages-reactive").handler(this::packagesReactive);

        router.get("/packages-not-reactive").handler(this::packagesNotReactive);

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

        timerPackagesReactive = Timer
                .builder("resource.packages.reactive")
                .register(registry);

        timerPackagesNotReactive = Timer
                .builder("resource.packages.not.reactive")
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

    private void fibonacci(RoutingContext routingContext) {
        timerFibonacci.record(() -> {
            Integer series = Integer.valueOf(routingContext.request().getParam("series"));
            routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(this.getFibonacciSeries(series)));
        });
    }

    private void wavToMp3(RoutingContext routingContext) {
        timerWavToMp3.record(() -> {
            routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(this.wavToMp3()));
        });
    }

    private void packagesReactive(RoutingContext routingContext) {
        timerPackagesReactive.record(() -> {
            Single<String> single = bintrayClient.fetchPackages();
            single.subscribe(response -> {
                JsonArray data = new JsonArray(response);
                List<String> bintrayPackageList;
                bintrayPackageList = data.stream().map(object -> {
                    JsonObject bintrayPackageJson = (JsonObject) object;
                    BintrayPackage bintrayPackage = new BintrayPackage(bintrayPackageJson.getString("name"), bintrayPackageJson.getBoolean("linked"));
                    return bintrayPackage;
                }).map(object -> object.name).collect(Collectors.toList());
                routingContext.response().putHeader("content-type", "application/json; charset=utf-8").end(Json.encodePrettily(bintrayPackageList));
            });
        });
    }

    private void packagesNotReactive(RoutingContext routingContext) {
        timerPackagesNotReactive.record(() -> {
            Future<JsonArray> response = bintrayClient.fetchPackagesNotReactive();
            response.setHandler(handler -> {
                if (handler.succeeded()) {
                    JsonArray data = handler.result();
                    List<String> bintrayPackageList;
                    bintrayPackageList = data.stream().map(object -> {
                        JsonObject bintrayPackageJson = (JsonObject) object;
                        BintrayPackage bintrayPackage = new BintrayPackage(bintrayPackageJson.getString("name"), bintrayPackageJson.getBoolean("linked"));
                        return bintrayPackage;
                    }).map(object -> object.name).collect(Collectors.toList());
                    routingContext.response().putHeader("content-type", "application/json; charset=utf-8").end(Json.encodePrettily(bintrayPackageList));
                }
            });
        });
    }

    private List<BigInteger> getFibonacciSeries(Integer series) {
        List<BigInteger> numbers = new ArrayList<BigInteger>();
        for (int i = 0; i < series; i++) {
            numbers.add(testService.fibonacci(i));
        }
        return numbers;

    }

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
