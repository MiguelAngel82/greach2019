package es.salenda.springboot.java.example;

import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
public class HomeController {

    final TestService testService;
    final AsterankClient asterankClient;

    @Autowired
    public HomeController(TestService testService, AsterankClient asterankClient) {
        this.testService = testService;
        this.asterankClient = asterankClient;
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

    @GetMapping(value = "/asterank-reactive")
    @ResponseBody
    @Timed("resource.asterank.reactive")
    public List<String> asteranks() {
        List<String> asterankList = new ArrayList<>();
        Iterable<Asterank> asteranks = asterankClient.fetchAsteroids().toIterable();
        for (Asterank asterank : asteranks) {
            asterankList.add(asterank.readable_des);
        }
        return asterankList;
    }

    @GetMapping(value = "/asterank-not-reactive")
    @ResponseBody
    @Timed("resource.asterank.not.reactive")
    public List<String> packagesNotReactive() {
        List<String> asterankList = new ArrayList<>();
        Iterable<Asterank> asteranks = asterankClient.fetchAsteroidsNotReactive();
        for (Asterank asterank : asteranks) {
            asterankList.add(asterank.readable_des);
        }
        return asterankList;
    }
}
