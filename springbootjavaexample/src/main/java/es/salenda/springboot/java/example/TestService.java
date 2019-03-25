package es.salenda.springboot.java.example;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

public interface TestService {

    void wavToMp3(File wavFile, File mp3File) throws IOException;

    BigInteger fibonacci(Integer series);
}