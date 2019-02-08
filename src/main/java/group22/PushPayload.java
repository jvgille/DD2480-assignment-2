package group22;
/**
* A utility class that stores all the needed information about the commit after parsing the http payload.
*/
public class PushPayload {
    final String ref;
    final String pusherName;
    final String pusherMail;
    final String commitSHA;
    final String repoURL;

    String buildResult;
    String buildMessage;
    String date;

    /**
     * Create a PushPayload object with the given parameters.
     * @param ref: the reference to the branch which was modified
     * @param pusherName: the name of the user who committed the changed
     * @param pusherMail: the email to which the server will send the approval
     * @param commitSHA: the SHA associated to the commit
     * @param repoURL: the URL for the repository
     * @param date: The date of the commit
     */
    public PushPayload(String ref, String pusherName, String pusherMail, String commitSHA, String repoURL, String date) {
        this.ref = ref;
        this.pusherName = pusherName;
        this.pusherMail = pusherMail;
        this.commitSHA = commitSHA;
        this.repoURL = repoURL;
        this.date = date;
    }

    @Override
    public String toString() {
        return "ref: " + ref + "\nURL: " + "\nemail: " + pusherMail + "\nname: " + pusherName + "\nSHA: " + commitSHA + "\ndate: " + date;
    }
}
