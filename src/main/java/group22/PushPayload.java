package group22;
/*
* A utility class that stores important information about a commit. 
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
