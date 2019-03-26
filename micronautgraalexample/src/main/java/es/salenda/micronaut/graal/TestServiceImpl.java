package es.salenda.micronaut.graal;

import de.sciss.jump3r.Main;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;


@Singleton
public class TestServiceImpl implements TestService {

    @Override
    public void wavToMp3(File wavFile, File mp3File) throws IOException {
        String[] mp3Args = { "--preset","standard",
                "-q","0",
                "-m","s",
                wavFile.getAbsolutePath(),
                mp3File.getAbsolutePath()
        };
        (new Main()).run(mp3Args);
    }

    @Override
    public BigInteger fibonacci(Integer series) {
        if (series <= 1) return BigInteger.valueOf(series);
        else {
            return fibonacci(series - 2).add(fibonacci(series - 1));
        }
    }
}
