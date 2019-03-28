package es.salenda.micronaut.java;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Flowable;

import java.util.List;

@Client(AsterankConfiguration.ASTERANK_API_URL)
public interface AsterankClient {

    @Get("/api/mpc?limit=${asterank.limit}")
    Flowable<Asterank> fetchAsteroids();

    @Get("/api/mpc?limit=${asterank.limit}")
    List<Asterank> fetchAsteroidsNotReactive();
}
