package group22;

public class PushPayload {
    String branch;
    String pusherName;
    String pusherMail;

    public PushPayload(String branch, String pusherName, String pusherMail) {
        this.branch = branch;
        this.pusherName = pusherName;
        this.pusherMail = pusherMail;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        return b.append(branch + " " + pusherMail + " " + pusherName).toString();
    }
}
