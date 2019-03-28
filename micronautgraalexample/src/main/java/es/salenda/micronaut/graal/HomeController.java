package es.salenda.micronaut.graal;

import io.micrometer.core.annotation.Timed;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Controller("/")
public class HomeController {

    private final AsterankClient asterankClient;
    protected final TestService testService;

    public HomeController(TestService testService, AsterankClient asterankClient) {
        this.testService = testService;
        this.asterankClient = asterankClient;
    }

    @Get("/wavToMp3")
    @Produces(MediaType.TEXT_PLAIN)
    @Timed("resource.wavToMp3")
    public String wavToMp3() {
        File wavFile = new File("src/main/resources/test.wav");
        File mp3File = new File("src/main/resources/test.mp3");
        try {
            testService.wavToMp3(wavFile, mp3File);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "File converted";
    }

    @Get("/fibonacci")
    @Produces(MediaType.TEXT_PLAIN)
    @Timed("resource.fibonacci")
    public List<BigInteger> fibonacci(Integer series) {
        List<BigInteger> numbers = new ArrayList<BigInteger>();
        for (int i = 0; i < series; i++) {
            numbers.add(testService.fibonacci(i));
        }
        return numbers;
    }

    @Get(uri = "/asterank-reactive", produces = MediaType.TEXT_PLAIN)
    @Timed("resource.asterank.reactive")
    List<String> asteranks() {
        List<String> asterankList = new ArrayList<>();
        Iterable<Asterank> asteranks = asterankClient.fetchPackages().blockingIterable();
        for (Asterank asterank : asteranks) {
            asterankList.add(asterank.readable_des);
        }
        return asterankList;
    }

    @Get(uri = "/asterank-not-reactive", produces = MediaType.TEXT_PLAIN)
    @Timed("resource.asterank.not.reactive")
    List<String> asteranksNotReactive() {
        List<String> asterankList = new ArrayList<>();
        Iterable<Asterank> asteranks = asterankClient.fetchPackagesNotReactive();
        for (Asterank asterank : asteranks) {
            asterankList.add(asterank.readable_des);
        }
        return asterankList;
    }

}