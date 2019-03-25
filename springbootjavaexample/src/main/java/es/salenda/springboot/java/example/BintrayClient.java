package es.salenda.springboot.java.example;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BintrayClient {

    private WebClient client = WebClient.create("https://bintray.com/");

    private WebClient.ResponseSpec result = client.get()
            .uri("/api/v1/repos/micronaut/profiles/packages")
            .accept(MediaType.TEXT_PLAIN)
            .retrieve();

    public Flux<BintrayPackage> fetchPackages() {
        Flux<BintrayPackage> result = client.get()
                .uri("/api/v1/repos/micronaut/profiles/packages")
                .retrieve()
                .bodyToFlux(BintrayPackage.class);
        result.subscribe();
        return result;
    }
}
