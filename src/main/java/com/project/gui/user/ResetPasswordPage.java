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


public class ResetPasswordPage extends Application {

    private static final String SERVER_IP = Constants.serverIp;
    private static final int SERVER_PORT = Constants.serverPort;

    private static int otp;

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



        // Add reset password components
        Label emailLabel = new Label("Email:");
        emailLabel.setTranslateY(100);

        TextField emailInput = new TextField();
        emailInput.setPromptText("Email");
        emailInput.setMaxWidth(226); // Set maximum width
        emailInput.setPrefHeight(50);
        emailInput.setStyle("-fx-border-radius: 10px; -fx-background-radius: 12px;"); // Set border radius
        emailInput.setTranslateY(100);

        Button sendCodeButton = new Button("Send Code");
        sendCodeButton.setPrefWidth(120);
        sendCodeButton.setPrefHeight(40);
        sendCodeButton.setTranslateX(250);
        sendCodeButton.setStyle("-fx-background-color: #5C92FB; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-background-radius: 12px;");

        sendCodeButton.setTranslateY(50);
        sendCodeButton.setOnAction(e -> {
            String email = emailInput.getText();
            try (
                    Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
            ) {
                System.out.println("Connected to server");
                Gson gson = new Gson();
                UserRegistrationRequest request = new UserRegistrationRequest("changePasswordOTP", email);
                String requestJson = gson.toJson(request);
                out.println(requestJson);
                String response = in.readLine();
                otp = Integer.parseInt(response);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


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
        verifyButton.setTranslateY(170);
        verifyButton.setStyle("-fx-background-color: #5C92FB; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-background-radius: 12px;");

        verifyButton.setOnAction(e->{
            if(Integer.parseInt(verificationCodeInput.getText()) == otp) {
                String email = emailInput.getText();
                System.out.println("Verified from reset password page");
                primaryStage.close();
                openPassChangePage(email);

            } else {
                System.out.println("Not verified");
            }
        });

        Label verilab = new Label("Didn’t get verification code ? ");
        verilab.setTranslateY(170);
        verilab.setTranslateX(110);
        Label verilab2 = new Label("Resend");
        verilab2.setTranslateY(144);
        verilab2.setTranslateX(270);
        verilab2.setTextFill(Color.rgb(92, 146, 251));


        Button loginButton = new Button("Login");
        loginButton.setMaxWidth(50);
        loginButton.setMaxHeight(20);

        loginButton.setTranslateY(-450);
        loginButton.setStyle("-fx-background-color: #5C92FB; -fx-text-fill: white; -fx-font-size: 10px; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-background-radius: 12px;");
        loginButton.setOnAction(e ->{
            primaryStage.close();
            openSignInPage();
        } );



        vbox.getChildren().addAll(emailLabel, emailInput, sendCodeButton, verificationCodeLabel, verificationCodeInput, verifyButton, verilab, verilab2, loginButton);

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

    private void openPassChangePage(String email) {
        PassChange passChange = new PassChange(email);
        Stage stage = new Stage();
        passChange.start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
