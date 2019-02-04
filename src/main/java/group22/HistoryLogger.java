package group22;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class HistoryLogger {

    private static void makeDir() {
        File f = new File("data/builds");
        f.mkdirs();
    }

    /**
     * Updates the data/index.html file to include a link to the given build.
     */
    private static void updateIndex(PushPayload p) throws IOException {
        String path = "data/index.html";
        BufferedWriter w = new BufferedWriter(new FileWriter(path, true));
        w.write("<p><a href=\"" + p.commitSHA + ".html\">" + p.commitSHA + "</a></p>\n");
        w.close();
    }

    /**
     * Stores the result of a given build in a formatted html file inside data/builds/
     */
    public static void storeBuild(PushPayload p) throws IOException {
        makeDir();
        String path = "data/builds/" + p.commitSHA + ".html";
        PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(path)));

        w.println("<p><strong><a href=\"/\">Back</a></strong></p>");
        w.println("<p><strong>SHA:</strong> " + p.commitSHA +  "</p>");
        w.println("<p><strong>Author:</strong> " + p.pusherName + "</p>");
        w.println("<p><strong>Date:</strong> " + p.date + "</p>"); 
        w.println("<p><strong>Status:</strong> " + p.buildResult + "</p>");
        w.println("<p><strong>Logs:</strong></p> ");
        w.println("<div style=\"border: 1px solid black; background:#ffffcc\"><pre>");
        w.println(p.buildMessage);
        w.println("</pre></div>");
        w.close();

        updateIndex(p);
    }
}
