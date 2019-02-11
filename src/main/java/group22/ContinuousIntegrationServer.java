package group22;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import org.json.*;

import java.util.concurrent.ConcurrentLinkedQueue;

import java.util.stream.Collectors;

/**
 * Represent a CI server that handle webhooks from GitHub
 */
public class ContinuousIntegrationServer extends AbstractHandler {
    public static final String DATA_PATH = "data";
    public static final String REPO_PATH = DATA_PATH + "/repo";
    public static final String BUILDS_PATH = DATA_PATH + "/builds";
    public static final String INDEX_PATH = DATA_PATH + "/index.html";

    private static ConcurrentLinkedQueue<PushPayload> queue = new ConcurrentLinkedQueue<PushPayload>();
    private static volatile boolean shouldStop = false;

    /**
     * handle a POST request by getting its payload,
     * the handling of this POST is then transfered to the handleQueue method
     * handle a GET request by either stopping the server or by displaying the different build of our project
     *
     * @param target The target of the request - either a URI or a name.
     * @param baseRequest The original unwrapped request object.
     * @param request The request either as the Request object or a wrapper of that request.
     * @param response The response as the Response object or a wrapper of that request.
     */
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        System.out.println(target);

        if (request.getMethod() == "POST") { //If the server receives a webhook, we parse it and keep the information we need.
            String requestData = request.getReader().lines().collect(Collectors.joining());
            JSONObject obj = new JSONObject(requestData);
            String ref = obj.getString("ref");
            JSONArray commits = obj.getJSONArray("commits");
            JSONObject info = commits.getJSONObject(0);
            String pusherMail = info.getJSONObject("author").getString("email");
            String pusherName = info.getJSONObject("author").getString("name");
            String commitSHA = info.getString("id");
            String url = obj.getJSONObject("repository").getString("html_url");
            String date = info.getString("timestamp");
            PushPayload pp = new PushPayload(ref, pusherName, pusherMail, commitSHA, url, date);
            queue.add(pp);
            System.out.println(pp);
        } else if (request.getMethod() == "GET") {
            System.out.println("get");
            if (target.equals("/stop")) {
                response.getOutputStream().println("Stopping server. Good bye.");
                shouldStop = true;
            } else {
                String path;
                if (target.equals("/")) {
                    path = INDEX_PATH;
                } else {
                    path = BUILDS_PATH + target;
                }
                ServletOutputStream s = response.getOutputStream();
                try {
                    byte[] b = Files.readAllBytes(Paths.get(path));
                    s.write(b);
                } catch(NoSuchFileException e) {
                    s.println("404 file not found");
                }
            }
        }
        response.flushBuffer();
    }
    /**
     * This method sleeps while nothing is pushed.
     * When a PushPayload is pushed in the queue, it is pulled and built with the given information.
     * An email is sent to the user to inform him about the result of the building of his latest commit.
     * @throws Exception to show the error message if the building does not work.
     */
    private static void handleQueue() throws Exception {
        try {
            while(!shouldStop) {
                PushPayload p = queue.poll();

                if (p == null) {
                    Thread.sleep(1000);
                } else {
                    System.out.println("cloning repo");
                    GitHandler.cloneRepo(p);
                    System.out.println("executing build");
                    ProjectBuilder.build(p);
                    System.out.println("storing build");
                    HistoryLogger.storeBuild(p);
                    System.out.println("done");
                    ProcessBuilder b = new ProcessBuilder();
                    b.command("bash", "-c", "mail -s \"Your latest push\" "+ p.pusherMail +" <<< '"+ p.buildResult + "'");
                    b.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Start the CI server and set its handler to be our ContinuousIntegrationServer implementation
     */
    public static void main(String[] args) throws Exception {
        Server server = new Server(8022);
        server.setHandler(new ContinuousIntegrationServer());
        server.start();

        handleQueue();
        server.stop();
    }

}
