package es.salenda.springboot.java.example;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class AsterankClient {

    private WebClient client = WebClient.create("http://www.asterank.com");

    public Flux<Asterank> fetchAsteroids() {
        Flux<Asterank> result = client.get()
                .uri("/api/mpc?limit=10")
                .retrieve()
                .bodyToFlux(Asterank.class);
        result.subscribe();
        return result;
    }

    public List<Asterank> fetchAsteroidsNotReactive() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        RestTemplate restTemplate = builder.build();
        ResponseEntity<List<Asterank>> response = restTemplate.exchange(
                "http://www.asterank.com/api/mpc?limit=10",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Asterank>>() {
                });
        List<Asterank> asteranks = response.getBody();
        return asteranks;
    }
}
