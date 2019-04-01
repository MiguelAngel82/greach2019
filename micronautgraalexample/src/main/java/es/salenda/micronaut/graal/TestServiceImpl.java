package es.salenda.micronaut.graal;

import de.sciss.jump3r.Main;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;


@Singleton
public class TestServiceImpl implements TestService {

    /**
     * Converts a wav file into mp3 file using jump3r library.
     * The converstion is done with parameters:
     * - algorithm quality selection (-q) set to 0 (use slowest and best possible version of all algorithms).
     * - mode (-m) set to stereo..
     * @see https://github.com/Sciss/jump3r
     * @param wavFile
     * @param mp3File
     * @throws IOException
     */
    @Override
    public void wavToMp3(File wavFile, File mp3File) throws IOException {
        String[] mp3Args = {"--preset", "standard",
                "-q", "0",
                "-m", "s",
                wavFile.getAbsolutePath(),
                mp3File.getAbsolutePath()
        };
        (new Main()).run(mp3Args);
    }

    /**
     * Calculates the nth element of Fibonacci in a recursive way.
     * @param series nth element to be calculated.
     * @return Fibonacci number is position nth.
     */
    @Override
    public BigInteger fibonacci(Integer series) {
        if (series <= 1) return BigInteger.valueOf(series);
        else {
            return fibonacci(series - 2).add(fibonacci(series - 1));
        }
    }
}
