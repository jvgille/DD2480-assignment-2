package group22;

public class PushPayload {
    final String ref;
    final String pusherName;
    final String pusherMail;
    final String commitSHA;
    final String repoURL;

    String buildResult;
    String buildMessage;

    public PushPayload(String ref, String pusherName, String pusherMail, String commitSHA, String repoURL) {
        this.ref = ref;
        this.pusherName = pusherName;
        this.pusherMail = pusherMail;
        this.commitSHA = commitSHA;
        this.repoURL = repoURL;
    }

    @Override
    public String toString() {
        return "ref: " + ref + "\nURL: " + "\nemail: " + pusherMail + "\nname: " + pusherName + "\nSHA: " + commitSHA;
    }
}
