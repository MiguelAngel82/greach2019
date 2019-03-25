package es.salenda.springboot.java.example;

import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
public class HomeController {

    final TestService testService;
    final BintrayClient bintrayClient;

    @Autowired
    public HomeController(TestService testService, BintrayClient bintrayClient) {
        this.testService = testService;
        this.bintrayClient = bintrayClient;
    }

    @GetMapping(value = "wavToMp3")
    @ResponseBody
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

    @GetMapping(value = "/fibonacci")
    @ResponseBody
    @Timed("resource.fibonacci")
    public List<BigInteger> fibonacci(@RequestParam("series") Integer series) {
        List<BigInteger> numbers = new ArrayList<BigInteger>();
        for (int i = 0; i < series; i++) {
            numbers.add(testService.fibonacci(i));
        }
        return numbers;
    }

    @GetMapping(value = "/packages")
    @ResponseBody
    @Timed("resource.packages")
    public List<String> packages(){
        List<String> bintrayPackageList = new ArrayList<>();
        Iterable<BintrayPackage> bintrayPackages = bintrayClient.fetchPackages().toIterable();
        for(BintrayPackage bintrayPackage: bintrayPackages) {
            bintrayPackageList.add(bintrayPackage.name);
        }
        return bintrayPackageList;
    }
}
