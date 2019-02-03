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
    private static void updateIndex(String SHA) throws IOException {
        String path = "data/index.html";
        BufferedWriter w = new BufferedWriter(new FileWriter(path, true));
        w.write("<p><a href=\"" + SHA + ".html\">" + SHA + "</a></p>\n");
        w.close();
    }

    /**
     * Stores the result of a given build in a formatted html file inside data/builds/
     */
    public static void storeBuild(String build) throws IOException {
        makeDir();
        String SHA = "" + System.currentTimeMillis();
        String path = "data/builds/" + SHA + ".html";
        PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(path)));

        w.println("<p><strong><a href=\"/\">Back</a></strong></p>");
        w.println("<p><strong>SHA:</strong> " + "sha goes here" +  "</p>");
        w.println("<p><strong>Author:</strong> " + "author goes here" + "</p>");
        w.println("<p><strong>Date:</strong> " + "date goes here" + "</p>");
        w.println("<p><strong>Status:</strong> " + "status goes here" + "</p>");
        w.println("<p><strong>Logs:</strong></p> ");
        w.println("<div style=\"border: 1px solid black; background:#ffffcc\"><pre>");
        w.println(build);
        w.println("</pre></div>");
        w.close();

        updateIndex(SHA);
    }
}
