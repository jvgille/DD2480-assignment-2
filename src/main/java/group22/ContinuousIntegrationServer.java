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

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

/**
 * Skeleton of a ContinuousIntegrationServer which acts as webhook See the Jetty
 * documentation for API documentation of those classes.
 */
public class ContinuousIntegrationServer extends AbstractHandler {
    public static final String DATA_PATH = "data";
    public static final String REPO_PATH = DATA_PATH + "/repo";
    public static final String BUILDS_PATH = DATA_PATH + "/builds";
    public static final String INDEX_PATH = DATA_PATH + "/index.html";

    private static ConcurrentLinkedQueue<PushPayload> queue = new ConcurrentLinkedQueue<PushPayload>();
    private static volatile boolean shouldStop = false;

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        System.out.println(target);

        if (request.getMethod() == "POST") {
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

    private static void handleQueue() {
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
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        sendEmail(pp.pusherMail, "arturo.vignon@gmail.com", "localhost", "branch");
    }

    private void sendEmail(String to, String from, String host, String branch) {
      // Get system properties
      Properties properties = System.getProperties();
      // Setup mail server
      properties.setProperty("mail.smtp.host", "130.229.164.248");
      //properties.setProperty("mail.smtp.port", "8022");
      // Get the default Session object.
      Session session = Session.getDefaultInstance(properties);
      try {
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);
         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));
         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
         // Set Subject: header field
         message.setSubject("Your last commit");
         // Send the actual HTML message, as big as you like
         if (true) {
                message.setContent("<h1>Hello, your last commit on the branch " + branch + " succeeded.</h1>", "text/html");
         } else {
             message.setContent("<h1>Hello, your last commit on the branch " + branch + " failed.</h1>", "text/html");
         }

         // Send message
         Transport.send(message);
         System.out.println("Sent message successfully....");
      } catch (MessagingException mex) {
         mex.printStackTrace();
      }
    }
    // used to start the CI server in command line
    public static void main(String[] args) throws Exception {
        Server server = new Server(8022);
        server.setHandler(new ContinuousIntegrationServer());
        server.start();

        handleQueue();
        server.stop();
    }

}
