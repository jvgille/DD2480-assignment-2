package group22;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Class for testing the mail system
 */

public class MailProcessTest {
    /**
     * Test that our notification system works properly and that the subprocess doesn't return an error
     */
    @Test public void test1() throws Exception {
        PushPayload p = new PushPayload("refs/heads/myBranchh", "Paul", "griesser@kth.se", "af73722fa8737de3b33b4069d509156cbceb822b",  "https://github.com/GriesserP/hello-test", "2019-02-11T16:24:10+01:00");
        ProcessBuilder b = new ProcessBuilder();
        b.command("bash", "-c", "mail -s \"Your latest push\" "+ p.pusherMail +" <<< '"+ p.buildResult + "'");
        assertTrue(b.start().waitFor() == 0);
    }
}
