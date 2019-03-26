package es.salenda.springboot.java.example;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

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

    public List<BintrayPackage> fetchPackagesNotReactive() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        RestTemplate restTemplate = builder.build();
        ResponseEntity<List<BintrayPackage>> response = restTemplate.exchange(
                "https://bintray.com/api/v1/repos/micronaut/profiles/packages",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BintrayPackage>>(){});
        List<BintrayPackage> bintrayPackages = response.getBody();
        return bintrayPackages;
    }
}
