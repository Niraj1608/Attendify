package com.project.gui.user;


import com.google.gson.Gson;
import com.project.constants.Constants;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int port = Constants.serverPort;

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(10); // Thread pool with 10 threads

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                executor.submit(() -> handleClient(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
            Gson gson = new Gson();
            String requestJson = reader.readLine();
            System.out.println("Received request: " + requestJson);
            UserRegistrationRequest request = gson.fromJson(requestJson, UserRegistrationRequest.class);
            switch (request.requestType()) {
                case "verify" -> {
                    int result = Database.verifyUser(request.email(), request.password());
                    writer.println(result);
                }
                case "registerUser", "changePasswordOTP" -> {
                    String email = request.email();
                    final Random random = new Random();
                    int otp = 100000 + random.nextInt(900000);
                    sendMail(email, String.valueOf(otp));
                    writer.println(otp);
                }
                case "register" -> {
                    boolean result = Database.addUser(request.email(), request.password());
                    writer.println(result);
                }
                case "registerAdmin" -> {
                    boolean result = Database.addAdmin(request.email(), request.password());
                    writer.println(result);
                }
                case "changePassword" -> {
                    boolean result = Database.changePassword(request.email(), request.password());
                    writer.println(result);
                }

                case "userQuery" -> {
                    String email = request.email();
                    String userQuery = request.password();
                    sendQuery(userQuery , email);
                }
                default -> writer.println("Invalid request type");
            }


        } catch (Exception e) {
            System.out.println("Error handling client: " + e.getMessage()); // Log the exception
            e.printStackTrace();
        }
    }

    public static void sendQuery(String userQuery, String fromEmail) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String username = "attendifyauth";
        String password = "xxhr zagl tbll decn";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress("attendifyauth@gmail.com"));
            message.setFrom(new InternetAddress(fromEmail));
            try {
                message.setSubject("User Query");
            } catch (MessagingException ex) {
                throw new RuntimeException(ex);
            }
            message.setText("User message: " + userQuery);

            try {
                Transport.send(message);
            } catch (MessagingException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Your queries have been sent successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendMail(String email, String otp) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.host", "smtp.gmail.com");

        String username = "attendifyauth";
        String password = "xxhr zagl tbll decn";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setFrom(new InternetAddress("attendifyauth@gmail.com"));
            message.setSubject("OTP for verification");
            message.setText("Your OTP is: " + otp);
            Transport.send(message);
            System.out.println("Mail sent");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

