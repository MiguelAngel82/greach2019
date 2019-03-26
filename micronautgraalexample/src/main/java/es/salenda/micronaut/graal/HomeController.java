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

    private final BintrayClient bintrayClient;

    protected final TestService testService;

    public HomeController(TestService testService, BintrayClient bintrayClient) {
        this.testService = testService;
        this.bintrayClient = bintrayClient;
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

    @Get(uri = "/packages", produces = MediaType.TEXT_PLAIN)
    @Timed("resource.packages")
    List<String> packages() {
        List<String> bintrayPackageList = new ArrayList<>();
        Iterable<BintrayPackage> bintrayPackages = bintrayClient.fetchPackages().blockingIterable();
        for (BintrayPackage bintrayPackage : bintrayPackages) {
            bintrayPackageList.add(bintrayPackage.name);
        }
        return bintrayPackageList;
    }
}