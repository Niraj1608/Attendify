package com.project.gui.user;



import com.google.gson.Gson;
import com.project.constants.Constants;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.constant.Constable;
import java.net.Socket;

public class PassChange extends Application {

    private static String email;
    public PassChange(String email) {
        this.email = email;
    }
    private static final String SERVER_IP = Constants.serverIp;
    private static final int SERVER_PORT = Constants.serverPort;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Reset Password OTP");
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


        Label passwordLabel = new Label("New Password:");
//        passwordLabel.setTranslateX(0);
        passwordLabel.setTranslateY(50);

        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("New Password");
        passwordInput.setPrefWidth(400);
        passwordInput.setPrefHeight(50);
        passwordInput.setStyle("-fx-border-radius: 10px; -fx-background-radius: 12px;"); // Set border radius

//        passwordInput.setTranslateX(0);
        passwordInput.setTranslateY(50);

        Label passwordLabel2 = new Label("Confirm Password:");
//        passwordLabel2.setTranslateX(0);
        passwordLabel2.setTranslateY(50);

        PasswordField passwordInput2 = new PasswordField();
        passwordInput2.setPromptText("Confirm Password");
        passwordInput2.setPrefWidth(400);
        passwordInput2.setPrefHeight(50);
        passwordInput2.setStyle("-fx-border-radius: 10px; -fx-background-radius: 12px;"); // Set border radius

//        passwordInput.setTranslateX(0);
        passwordInput2.setTranslateY(50);

        Button Changepass = new Button("Change Password");

        Changepass.setPrefWidth(200);
        Changepass.setPrefHeight(40);
        Changepass.setTranslateX(120);
        Changepass.setStyle("-fx-background-color: #5C92FB; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-background-radius: 12px;"); // Set button color

        Changepass.setTranslateY(70);

        Changepass.setOnAction(e -> {
            String password1 = passwordInput.getText();
            String password2 = passwordInput2.getText();
            if (password1 != null && !password1.isEmpty() &&
                    password2 != null && !password2.isEmpty() &&
                    password1.equals(password2)) {
                System.out.println("please enter password");
                if (password1.length() < 6) {
                    passwordInput.setText("");
                    passwordInput2.setText("");
                    System.out.println("Change password button clicked");
                    Label errorLabel = new Label("Password must be 7 character long!");
                    errorLabel.setTextFill(Color.RED);
                    errorLabel.setTranslateX(130);
                    errorLabel.setTranslateY(10);
                    vbox.getChildren().add(errorLabel);
                } else {
                    System.out.println("when both password are same");
                    try (
                            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    ) {
                        System.out.println("Connected to server");
                        Gson gson = new Gson();
                        UserRegistrationRequest request = new UserRegistrationRequest("changePassword", email, password1);
                        String requestJson = gson.toJson(request);
                        out.println(requestJson);
                        String response = in.readLine();
                        if(response.equals("true")) {
                            System.out.println("Password changed successfully");
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Well Done!");
                            alert.setHeaderText(null);
                            alert.setContentText("Your password has been changed successfully! Please login with your new password.");
                            alert.showAndWait();
                            primaryStage.close();
                            openSignInPage();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            } else {
                passwordInput.setText("");
                passwordInput2.setText("");
                Label errorLabel = new Label("Password invalid");
                errorLabel.setTextFill(Color.RED);
                errorLabel.setTranslateX(170);
                errorLabel.setTranslateY(35);
                vbox.getChildren().add(errorLabel);
            }
        });

        Button loginButton = new Button("Login");
        loginButton.setMaxWidth(50);
        loginButton.setMaxHeight(20);

        loginButton.setTranslateY(-350);
        loginButton.setStyle("-fx-background-color: #5C92FB; -fx-text-fill: white; -fx-font-size: 10px; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-background-radius: 12px;");
        loginButton.setOnAction(e ->{
            primaryStage.close();
            openSignInPage();
        } );
        Label errorLabel = new Label();

        vbox.getChildren().addAll(passwordLabel, passwordInput, passwordLabel2, passwordInput2, Changepass, loginButton);

        // Add VBox to StackPane
        stackPane.getChildren().add(vbox);

        // Set up the scene
        Scene scene = new Scene(stackPane, 500, 675);
        primaryStage.setScene(scene);
        primaryStage.show();


        // Add event listener to Sign Up button
//        Changepass.setOnAction(e -> {
//            vbox.getChildren().remove(errorLabel);
//
//            String password1 = passwordInput.getText();
//            String password2 = passwordInput2.getText();
//
//            System.out.println("Changepass button clicked");
//            if (password1 != null && !password1.isEmpty() &&
//                    password2 != null && !password2.isEmpty() &&
//                    password1.equals(password2)){
//                System.out.println("Changepass ");
//                if (password1.length() < 6){
//                    System.out.println("Changepass button ");
//                    passwordInput.setText("");
//                    passwordInput2.setText("");
//
//                    errorLabel.setText("Password must be 7 character long!");
//                    errorLabel.setTextFill(Color.RED);
//                    errorLabel.setTranslateX(130);
//                    errorLabel.setTranslateY(10);
//                }
//
//                else {
//                    primaryStage.close();
//                    openSignInPage(); }
//            }
//            else {
//                passwordInput.setText("");
//                passwordInput2.setText("");
//
//                errorLabel.setText("Password invailid");
//                errorLabel.setTextFill(Color.RED);
//                errorLabel.setTranslateX(170);
//                errorLabel.setTranslateY(35);
//            }
//            vbox.getChildren().add(errorLabel);
//        });
//
//


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
