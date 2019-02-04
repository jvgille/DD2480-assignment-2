package group22;

public class PushPayload {
    String branch;
    String pusherName;
    String pusherMail;
    String commitSHA;
    String commitMessage;

    String buildResult;
    String buildMessage;
    String date;

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
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        return b.append("branch: " + branch + "\npusher Mail: " + pusherMail + "\npusher Name: " + pusherName + "\ncommit SHA: " + commitSHA + "\ncommit message: " + commitMessage + "\ndate: " + date).toString();
    }
}
