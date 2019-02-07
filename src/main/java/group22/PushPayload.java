package group22;

public class PushPayload {
    final String ref;
    final String pusherName;
    final String pusherMail;
    final String commitSHA;
    final String repoURL;
<<<<<<< HEAD

=======
    String branch;
    String commitMessage;
>>>>>>> 30dc82fb1579c23df6b43b3baeb3d3f0c9e6869e
    String buildResult;
    String buildMessage;
    String date;

<<<<<<< HEAD
    public PushPayload(String ref, String pusherName, String pusherMail, String commitSHA, String repoURL, String date) {
        this.ref = ref;
=======

    public PushPayload(String ref, String pusherName, String pusherMail, String commitSHA, String repoURL, String commitMessage, String date) {
        this.ref = ref;
        this.branch = getBranchName(ref);
>>>>>>> 30dc82fb1579c23df6b43b3baeb3d3f0c9e6869e
        this.pusherName = pusherName;
        this.pusherMail = pusherMail;
        this.commitSHA = commitSHA;
        this.repoURL = repoURL;
        this.date = date;
        this.repoURL = repoURL;
    }

    @Override
    public String toString() {
        return "ref: " + ref + "\nURL: " + "\nemail: " + pusherMail + "\nname: " + pusherName + "\nSHA: " + commitSHA + "\ndate: " + date;
    }
}
