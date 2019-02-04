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
 * Skeleton of a ContinuousIntegrationServer which acts as webhook See the Jetty
 * documentation for API documentation of those classes.
 */
public class ContinuousIntegrationServer extends AbstractHandler {

    ConcurrentLinkedQueue<PushPayload> queue = new ConcurrentLinkedQueue<PushPayload>();

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        System.out.println(target);


        // here you do all the continuous integration tasks
        // for example
        // 1st clone your repository
        // 2nd compile the code (run ./gradlew build)
        String requestData = request.getReader().lines().collect(Collectors.joining());
        if (request.getMethod() == "POST") {
            JSONObject obj = new JSONObject(requestData);
            String branch = obj.getString("ref");
            String pusherMail = obj.getJSONObject("pusher").getString("email");
            String pusherName = obj.getJSONObject("pusher").getString("name");

            PushPayload pp = new PushPayload(branch, pusherName, pusherMail);
            queue.add(pp);
            //System.out.println(pp);
        }

        if (request.getMethod() == "GET") {
            String path = "data/";
            if (target.equals("/")) {
                path += "index.html";
            } else {
                path += "builds" + target;
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

    // used to start the CI server in command line
    public static void main(String[] args) throws Exception {
        Server server = new Server(8022);
        server.setHandler(new ContinuousIntegrationServer());
        server.start();
        server.join();

        // HistoryLogger.storeBuild("hello this is build result\nit is very nice");
    }

}
