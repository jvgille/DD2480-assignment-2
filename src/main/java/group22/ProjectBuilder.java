package group22;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;
import java.lang.InterruptedException;
import java.lang.ProcessBuilder;
import java.util.stream.Collectors;
/**
* A class for encapsulating the building and testing of the project
*/
public class ProjectBuilder {
    /**
     * Utility method which builds a String from all the data in an InputStream.
     * @param i: The InputStream which we want to read from
     * @return the string containing the data from the InputStream
     */
    private static String stringFromInputStream(InputStream i) {
        return new BufferedReader(new InputStreamReader(i))
                    .lines().collect(Collectors.joining("\n"));
    }

    /**
     * Builds the gradle project located in the REPO_PATH (data/repo)
     * @param p: The PushPayload where we will store the result of the build
     *
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
