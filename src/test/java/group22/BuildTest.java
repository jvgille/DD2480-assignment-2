package group22;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;


public class BuildTest {
    PushPayload p;

    @Before
    public void setup() {
        p = new PushPayload("ref", "pusherName", "pusherMail", "commitSHA", "repoURL", "date");
    }

    @Test
    public void test_build_successful() {
        ContinuousIntegrationServer.REPO_PATH = "test_resources/passing";
        try {
            ProjectBuilder.build(p);
        } catch (Exception e) {
            e.printStackTrace();
            fail("ProjectBuilder threw an exception.");
            return;
        }
        assertEquals("success", p.buildResult);
        assertNotEquals("", p.buildMessage);
    }

    @Test
    public void test_build_unsuccessful() {
        ContinuousIntegrationServer.REPO_PATH = "test_resources/failing";
        try {
            ProjectBuilder.build(p);
        } catch (Exception e) {
            e.printStackTrace();
            fail("ProjectBuilder threw an exception.");
            return;
        }
        assertEquals("fail", p.buildResult);
        assertNotEquals("", p.buildMessage);
    }
}