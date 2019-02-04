package group22;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;
import java.lang.InterruptedException;
import java.lang.ProcessBuilder;
import java.util.stream.Collectors;

public class ProjectBuilder {
    static class BuildOutput {
        boolean success;
        String message;
    }

    /**
     * Utility method which builds a String from all the data in an InputStream.
     */
    private static String stringFromInputStream(InputStream i) {
        return new BufferedReader(new InputStreamReader(i))
                    .lines().collect(Collectors.joining("\n"));
    }

    /**
     * Builds the gradle project located in "data/DD2480-assignment-2"
     * and returns a BuildOutput object describing the result.
     */
    public static BuildOutput build() throws IOException, InterruptedException {
        BuildOutput result = new BuildOutput();

        Process pr = new ProcessBuilder("./gradlew", "check")
            .directory(new File("data/DD2480-assignment-2"))
            .start();

        pr.waitFor();

        if (pr.exitValue() != 0) {
            result.success = false;
            result.message = stringFromInputStream(pr.getErrorStream());
            return result;
        }

        result.success = true;
        result.message = stringFromInputStream(pr.getInputStream());
        return result;
    }
}
