package group22;

public class PushPayload {
    final String ref;
    final String pusherName;
    final String pusherMail;
    final String commitSHA;
    final String repoURL;

    String buildResult;
    String buildMessage;
    String date;

<<<<<<< 4f058dc1c6116b6e336f4ff4b9b6a0579d3843b9
    public PushPayload(String ref, String pusherName, String pusherMail, String commitSHA, String repoURL) {
        this.ref = ref;
        this.pusherName = pusherName;
        this.pusherMail = pusherMail;
        this.commitSHA = commitSHA;
        this.repoURL = repoURL;
=======
    public PushPayload(String ref, String pusherName, String pusherMail, String commitSHA, String commitMessage, String date) {
        this.branch = getBranchName(ref);
        this.pusherName = pusherName;
        this.pusherMail = pusherMail;
        this.commitSHA = commitSHA;
        this.commitMessage = commitMessage;
        this.date = date;
    }

    private String getBranchName(String ref) {
        char c;
        StringBuilder sb = new StringBuilder();
        int index = ref.length() - 1;
        while ((c = ref.charAt(index)) != '/') {
            sb.append(ref.charAt(index));
            index -= 1;
        }
        return sb.reverse().toString();
>>>>>>> #18, added date, needs to be tested
    }

    @Override
    public String toString() {
<<<<<<< 4f058dc1c6116b6e336f4ff4b9b6a0579d3843b9
        return "ref: " + ref + "\nURL: " + "\nemail: " + pusherMail + "\nname: " + pusherName + "\nSHA: " + commitSHA;
=======
        StringBuilder b = new StringBuilder();
        return b.append("branch: " + branch + "\npusher Mail: " + pusherMail + "\npusher Name: " + pusherName + "\ncommit SHA: " + commitSHA + "\ncommit message: " + commitMessage + "\ndate: " + date).toString();
>>>>>>> #18, added date, needs to be tested
    }
}
