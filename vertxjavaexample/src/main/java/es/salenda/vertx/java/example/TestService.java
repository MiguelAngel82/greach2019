package es.salenda.vertx.java.example;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

public interface TestService {

    BigInteger fibonacci(Integer series);

    void wavToMp3(File wavFile, File mp3File) throws IOException;

}
