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
    public static void build(PushPayload p) throws IOException, InterruptedException {
        Process pr = new ProcessBuilder("./gradlew", "check")
            .directory(new File(ContinuousIntegrationServer.REPO_PATH))
            .start();

        pr.waitFor();

        if (pr.exitValue() != 0) {
            p.buildResult = "fail";
            p.buildMessage = stringFromInputStream(pr.getErrorStream());
        }

        p.buildResult = "success";
        p.buildMessage = stringFromInputStream(pr.getInputStream());
    }
}
