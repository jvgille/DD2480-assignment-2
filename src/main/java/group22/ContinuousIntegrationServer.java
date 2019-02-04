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

    ConcurrentLinkedQueue<PushPayload> queue = new ConcurrentLinkedQueue<PushPayload>();

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        System.out.println(target);

        PushPayload pp = new PushPayload();
        // here you do all the continuous integration tasks
        // for example
        // 1st clone your repository
        // 2nd compile the code (run ./gradlew build)
        String requestData = request.getReader().lines().collect(Collectors.joining());
        System.out.println(requestData);
        if (request.getMethod() == "POST") {
            JSONObject obj = new JSONObject(requestData);
            String ref = obj.getString("ref");
            JSONArray commits = obj.getJSONArray("commits");
            JSONObject info = commits.getJSONObject(0);
            String pusherMail = info.getJSONObject("author").getString("email");
            String pusherName = info.getJSONObject("author").getString("name");
            String commitSHA = info.getString("id");
            String commitMessage = info.getString("message");
            pp = new PushPayload(ref, pusherName, pusherMail, commitSHA, commitMessage);
            queue.add(pp);
            System.out.println(pp);
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
        server.join();

        // HistoryLogger.storeBuild("hello this is build result\nit is very nice");
    }

}
