package com.project.gui.user;


import com.project.Cookie;
import com.project.constants.Constants;
import com.project.gui.dashboard.ClientDashboard;
import com.project.gui.dashboard.DashBoard;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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

import static com.project.gui.user.Database.getUserId;

public class Main extends Application {
    private static final String SERVER_IP = Constants.serverIp;
    private static final int SERVER_PORT = Constants.serverPort;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login Page");
        primaryStage.setResizable(false);

        // Create a StackPane to hold the elements
        StackPane stackPane = new StackPane();

        // Load logo image
        Image logoImage = new Image(getClass().getResourceAsStream("/logo.png"));

        ImageView imageView = new ImageView(logoImage);
        imageView.setFitWidth(150); // Set image width
        imageView.setFitHeight(150); // Set image height
        //marginTop is 20 pixels
        imageView.setTranslateY(20);
        imageView.setTranslateX(140);
        imageView.setPreserveRatio(true);

        // Create VBox to hold login components
        VBox vbox = new VBox(10); // 10 pixels spacing between elements

        vbox.setPadding(new Insets(0, 50, 50, 50)); // Add top padding to make space for the logo
        vbox.getChildren().add(imageView); // Add logo to the VBox

        // Add other login components
        Label emailLabel = new Label("Email:");
//        usernameLabel.setTranslateX(0);
        emailLabel.setTranslateY(100);

        TextField emailInput = new TextField();
        emailInput.setPromptText("email");
        emailInput.setPrefWidth(400);
        emailInput.setPrefHeight(50);
        emailInput.setStyle("-fx-border-radius: 10px; -fx-background-radius: 12px;"); // Set border radius

        emailInput.setTranslateX(0);
        emailInput.setTranslateY(100);

        Label passwordLabel = new Label("Password:");
//        passwordLabel.setTranslateX(0);
        passwordLabel.setTranslateY(120);

        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Password");
        passwordInput.setPrefWidth(400);
        passwordInput.setPrefHeight(50);
        passwordInput.setStyle("-fx-border-radius: 10px; -fx-background-radius: 12px;"); // Set border radius

//        passwordInput.setTranslateX(0);
        passwordInput.setTranslateY(120);

        Label Forgotlab = new Label("Don’t remember password ?");
//        Forgotlab.setFont(new javafx.scene.text.Font(10);
        Forgotlab.setTextFill(Color.RED);
        Forgotlab.setTranslateY(100);

        Forgotlab.setTranslateX(140);

        Button loginButton = new Button("Sign In");
        loginButton.setPrefWidth(150);
        loginButton.setPrefHeight(40);
        loginButton.setTranslateX(140);
        loginButton.setStyle("-fx-background-color: #5C92FB; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-background-radius: 12px;"); // Set button color

        loginButton.setTranslateY(200);
//        loginButton.setTranslateX(500);

        Label SignUPlab = new Label("Don’t have an account ? ");
        SignUPlab.setTranslateY(200);
        SignUPlab.setTranslateX(140);
        Label SignUPlab2 = new Label("Sign Up");
        SignUPlab2.setTranslateY(173);
        SignUPlab2.setTranslateX(270);
        SignUPlab2.setTextFill(Color.rgb(92, 146, 251));


        // Add components to VBox
        vbox.getChildren().addAll(emailLabel, emailInput, passwordLabel, passwordInput, loginButton, Forgotlab, SignUPlab, SignUPlab2);

        // Add VBox to StackPane
        stackPane.getChildren().add(vbox);

        // Set up the scene
        Scene scene = new Scene(stackPane, 500, 675);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Define errorLabel as a class field
        Label errorLabel = new Label();

        // Inside the button event handler
        loginButton.setOnAction(e -> {
            // Remove previous error label
            vbox.getChildren().remove(errorLabel);
            // Send the username and password to the server
            String email = emailInput.getText();
            String password = passwordInput.getText();
            System.out.println("Email: " + email);
            System.out.println("Password: " + password);
            try (
                    Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
            ) {
                System.out.println("Connected to server");
                Gson gson = new Gson();
                UserRegistrationRequest request = new UserRegistrationRequest("verify", email, password);
                String requestJson = gson.toJson(request);
                System.out.println(requestJson);
                out.println(requestJson);
                String response = in.readLine();
                System.out.println("before if " + response);
                int userId = getUserId(email);
                Cookie.setUserid(userId);
                if (response != null && !response.isEmpty()) {
                    if (Integer.parseInt(response) == 1) {
                        errorLabel.setTextFill(Color.GREEN);
                        errorLabel.setText("User is verified client");
                        primaryStage.close();
                        openDashBoard();
                    } else if(Integer.parseInt(response) == 2){
                        errorLabel.setTextFill(Color.GREEN);
                        errorLabel.setText("User is verified admin");
                        primaryStage.close();
                        openAdminDashBoard();
                    }
                } else {
                    System.out.println("Response from server is null or empty");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            errorLabel.setTranslateX(140);
            errorLabel.setTranslateY(120);


            vbox.getChildren().add(errorLabel);
        });

        Forgotlab.setOnMouseClicked(e -> {
            primaryStage.close();
            openResetPasswordWindow();

        });
        SignUPlab2.setOnMouseClicked(e -> {
            primaryStage.close();
            openSignupWindow();
        });


    }
    private void openResetPasswordWindow() {

        // Code to open the reset password window goes here
        ResetPasswordPage resetPasswordPage = new ResetPasswordPage();
        Stage resetPasswordStage = new Stage();
        resetPasswordPage.start(resetPasswordStage);
    }
    private void openSignupWindow() {

        // Code to open the reset password window goes here
        SignUp signUpPage = new SignUp();
        Stage signUpStage = new Stage();
        signUpPage.start(signUpStage);

    }

    private void openDashBoard() {
        ClientDashboard clientDashboard = new ClientDashboard();
        Stage dashboardStage = new Stage();
        clientDashboard.start(dashboardStage);
    }

    private void openAdminDashBoard() {
        DashBoard dashBoard = new DashBoard();
        Stage dashboardStage = new Stage();
        dashBoard.start(dashboardStage);
    }



    public static void main(String[] args) {
        launch(args);
    }
}
