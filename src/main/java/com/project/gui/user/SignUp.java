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
import java.net.Socket;

public class SignUp extends Application {

    private static final String SERVER_IP = Constants.serverIp;
    private static final int SERVER_PORT = Constants.serverPort;

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

        ToggleGroup userTypeToggleGroup = new ToggleGroup();
        RadioButton Client = new RadioButton("Client");
        Client.setToggleGroup(userTypeToggleGroup);
        Client.setSelected(true); // Set default selection to true
        Client.setTranslateY(50);
        Client.setTranslateX(140);
        RadioButton Admin = new RadioButton("Admin");
        Admin.setToggleGroup(userTypeToggleGroup);
        Admin.setTranslateX(220);
        Admin.setTranslateY(25);


        // Add other login components
        Label usernameLabel = new Label("Email:");
//        usernameLabel.setTranslateX(0);
        usernameLabel.setTranslateY(50);

        TextField usernameInput = new TextField();
        usernameInput.setPromptText("email");
        usernameInput.setPrefWidth(400);
        usernameInput.setPrefHeight(50);
        usernameInput.setStyle("-fx-border-radius: 10px; -fx-background-radius: 12px;"); // Set border radius

        usernameInput.setTranslateY(50);

        Label passwordLabel = new Label("Password:");
//        passwordLabel.setTranslateX(0);
        passwordLabel.setTranslateY(50);

        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Password");
        passwordInput.setPrefWidth(400);
        passwordInput.setPrefHeight(50);
        passwordInput.setStyle("-fx-border-radius: 10px; -fx-background-radius: 12px;"); // Set border radius

//        passwordInput.setTranslateX(0);
        passwordInput.setTranslateY(50);

        Label passwordLabel2 = new Label("Confirm Password:");
//        passwordLabel2.setTranslateX(0);
        passwordLabel2.setTranslateY(50);

        PasswordField passwordInput2 = new PasswordField();
        passwordInput2.setPromptText("Password");
        passwordInput2.setPrefWidth(400);
        passwordInput2.setPrefHeight(50);
        passwordInput2.setStyle("-fx-border-radius: 10px; -fx-background-radius: 12px;"); // Set border radius

//        passwordInput.setTranslateX(0);
        passwordInput2.setTranslateY(50);

        Button SigninButton = new Button("Sign Up");
        SigninButton.setPrefWidth(150);
        SigninButton.setPrefHeight(40);
        SigninButton.setTranslateX(140);
        SigninButton.setStyle("-fx-background-color: #5C92FB; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-background-radius: 12px;"); // Set button color

        SigninButton.setTranslateY(70);
//        loginButton.setTranslateX(500);

        Label SignUPlab = new Label("Already have an account ? ");
        SignUPlab.setTranslateY(87);
        SignUPlab.setTranslateX(140);
        Label SignUPlab2 = new Label("Sign In");
        SignUPlab2.setTranslateY(60);
        SignUPlab2.setTranslateX(280);
        SignUPlab2.setTextFill(Color.rgb(92, 146, 251));

        vbox.getChildren().addAll(Client, Admin, usernameLabel, usernameInput, passwordLabel, passwordInput, passwordLabel2, passwordInput2, SigninButton, SignUPlab, SignUPlab2);

        // Add VBox to StackPane
        stackPane.getChildren().add(vbox);
        Label errorLabel = new Label();


        // Set up the scene
        Scene scene = new Scene(stackPane, 500, 675);
        primaryStage.setScene(scene);
        primaryStage.show();


        // Add event listener to Sign Up button



        SigninButton.setOnAction(e -> {
            vbox.getChildren().remove(errorLabel);
            System.out.println("Sign Up button clicked");

            String email = usernameInput.getText();
            String password1 = passwordInput.getText();
            String password2 = passwordInput2.getText();

            if (email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}") ) {
                System.out.println("Valid email: " + email);
                if (password1 != null && !password1.isEmpty() &&
                        password2 != null && !password2.isEmpty() &&
                        password1.equals(password2)){
                    if (password1.length() < 6){
                        usernameInput.setText("");// Clear the TextField if the email is invalid
                        passwordInput.setText("");
                        passwordInput2.setText("");

                        errorLabel.setText("Password must be 7 character long!");
                        errorLabel.setTextFill(Color.RED);
                        errorLabel.setTranslateX(130);
                        errorLabel.setTranslateY(10);
                    }
                    else {
                        if(Client.isSelected()){
                        try (
                                Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
                        ) {
                            System.out.println("Connected to server");
                            Gson gson = new Gson();
                            UserRegistrationRequest request = new UserRegistrationRequest("registerUser", email, password2);
                            String requestJson = gson.toJson(request);
                            out.println(requestJson);
                            int otp = Integer.parseInt(in.readLine());
                           primaryStage.close();
                           opennVerifyPage(otp,email,password2,false);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }}
                        else if(Admin.isSelected()){
                            try (
                                    Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
                            ) {
                                System.out.println("Connected to server");
                                Gson gson = new Gson();
                                UserRegistrationRequest request = new UserRegistrationRequest("registerUser", email, password2);
                                String requestJson = gson.toJson(request);
                                out.println(requestJson);
                                int otp = Integer.parseInt(in.readLine());
                                primaryStage.close();
                                opennVerifyPage(otp,email,password2,true);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                    }}
                }
                else {
                    usernameInput.setText("");// Clear the TextField if the email is invalid
                    passwordInput.setText("");
                    passwordInput2.setText("");

                    errorLabel.setText("Password must be same!");
                    errorLabel.setTextFill(Color.RED);
                    errorLabel.setTranslateX(150);
                    errorLabel.setTranslateY(10);
                }

            } else {
                System.out.println("Invalid email: " + email);
                usernameInput.setText("");// Clear the TextField if the email is invalid
                passwordInput.setText("");
                passwordInput2.setText("");

                errorLabel.setText("Invalid email");
                errorLabel.setTextFill(Color.RED);
                // Set position of errorLabel

                errorLabel.setTranslateX(180);
                errorLabel.setTranslateY(10);
            }

            vbox.getChildren().add(errorLabel);
        });


        // Add event listener to Sign In label
        SignUPlab2.setOnMouseClicked(e -> {
            System.out.println("Sign In label clicked");
            primaryStage.close();
            openSignInPage();
        });


    }
    private void openSignInPage() {
        Main main = new Main();
        Stage stage = new Stage();
        main.start(stage);

    }
    private void opennVerifyPage(int otp,String email,String password,boolean isAdmin) {
        VerifySignUp verifyPage = new VerifySignUp(otp,email,password,isAdmin);
        Stage stage = new Stage();
        verifyPage.start(stage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
