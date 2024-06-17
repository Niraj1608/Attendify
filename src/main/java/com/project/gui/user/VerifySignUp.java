package com.project.gui.user;


import com.google.gson.Gson;
import com.project.constants.Constants;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class VerifySignUp extends Application {

    private static final String SERVER_IP = Constants.serverIp;
    private static final int SERVER_PORT = Constants.serverPort;

    boolean isAdmin;

    private int otp;
    private String email, password;
    public VerifySignUp(int otp,String email,String password,boolean isAdmin) {
        this.otp = otp;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Reset Password");
        primaryStage.setResizable(false);

        // Create a StackPane to hold the elements
        StackPane stackPane = new StackPane();

        // Load logo image
        Image logoImage = new Image(getClass().getResourceAsStream("/logo.png"));

        ImageView imageView = new ImageView(logoImage);
        imageView.setFitWidth(150); // Set image width
        imageView.setFitHeight(150); // Set image height
        imageView.setTranslateY(20);
        imageView.setTranslateX(150);
        imageView.setPreserveRatio(true);

        // Create VBox to hold reset password components
        VBox vbox = new VBox(10); // 10 pixels spacing between elements
        vbox.setPadding(new Insets(0, 50, 50, 50)); // Add padding
        vbox.getChildren().add(imageView); // Add logo to the VBox


        Label verificationCodeLabel = new Label("Verification code:");
        verificationCodeLabel.setTranslateY(100);

        TextField verificationCodeInput = new TextField();
        verificationCodeInput.setPromptText("Verification code");
        verificationCodeInput.setPrefWidth(400);
        verificationCodeInput.setPrefHeight(50);
        verificationCodeInput.setStyle("-fx-border-radius: 10px; -fx-background-radius: 12px;");
        verificationCodeInput.setTranslateY(120);

        Button verifyButton = new Button("Verify");
        verifyButton.setPrefWidth(120);
        verifyButton.setPrefHeight(40);
        verifyButton.setTranslateX(140);
        verifyButton.setTranslateY(150);
        verifyButton.setStyle("-fx-background-color: #5C92FB; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-background-radius: 12px;");

        verifyButton.setOnAction(e->{
        if(Integer.parseInt(verificationCodeInput.getText()) == otp) {
            System.out.println("Verified");
            try (
                    Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
            ) {
                System.out.println("Connected to server");
                Gson gson = new Gson();
                String requestJson;
                if(!isAdmin) {
                    UserRegistrationRequest request = new UserRegistrationRequest("register", email, password);
                    requestJson = gson.toJson(request);
                } else {
                    UserRegistrationRequest request = new UserRegistrationRequest("registerAdmin", email, password);
                   requestJson = gson.toJson(request);
                }
                out.println(requestJson);
                String response = in.readLine();
                if(response.equals("true")) {
                    System.out.println("User registered successfully");
                    primaryStage.close();
                    openSignInPage();
                } else {
                    System.out.println("User registration failed");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("Not verified");
        }
        });


        Label verilab = new Label("Didn’t get verification code ? ");
        verilab.setTranslateY(150);
        verilab.setTranslateX(110);
        Label verilab2 = new Label("Resend");
        verilab2.setTranslateY(124);
        verilab2.setTranslateX(270);
        verilab2.setTextFill(Color.rgb(92, 146, 251));

        Button loginButton = new Button("Login");
        loginButton.setMaxWidth(50);
        loginButton.setMaxHeight(20);

        loginButton.setTranslateY(-320);
        loginButton.setStyle("-fx-background-color: #5C92FB; -fx-text-fill: white; -fx-font-size: 10px; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-background-radius: 12px;");
        loginButton.setOnAction(e ->{
            primaryStage.close();
            openSignInPage();
        } );



        vbox.getChildren().addAll( verificationCodeLabel, verificationCodeInput, verifyButton, verilab, verilab2, loginButton);

        // Add VBox to StackPane
        stackPane.getChildren().add(vbox);

        // Set up the scene
        Scene scene = new Scene(stackPane, 500, 675);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void openSignInPage() {
        Main main = new Main();
        Stage stage = new Stage();
        main.start(stage);

    }
    public static void main(String[] args) {
        launch(args);
    }
}
