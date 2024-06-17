package com.project.gui.user;



import com.google.gson.Gson;
import com.project.constants.Constants;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class MainApp extends Application {

    private static final String SERVER_IP = Constants.serverIp;
    private static final int SERVER_PORT = Constants.serverPort;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Welcome to Attendify!");
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(25, 25, 25, 25));
        Scene scene = new Scene(pane, 300, 275);

        Text sceneTitle = new Text("Attendify");
        sceneTitle.setFont(Font.font("Consolas", FontWeight.NORMAL,20));
        pane.add(sceneTitle, 0, 0, 2, 1);
        Label emailLabel = new Label("Email:");
        pane.add(emailLabel, 0, 1);
        final TextField emailField = new TextField();
        pane.add(emailField, 1, 1);
        Label passwordLabel = new Label("Password:");
        pane.add(passwordLabel,0,2);
        final TextField passwordField = new TextField();
        pane.add(passwordField, 1, 2);

        Button submit = new Button("submit");
        Button check = new Button("check");
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.BOTTOM_RIGHT);
        hbox.getChildren().addAll(submit,check);
        pane.add(hbox, 1, 4);

        final Text taxMessage = new Text();
        pane.add(taxMessage, 1, 6);

        submit.setOnAction(e -> {
            String email = emailField.getText();
            String password  = passwordField.getText();
            try (
                    Socket socket = new Socket("138.68.79.95", SERVER_PORT);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
            ) {
                Gson gson = new Gson();
                UserRegistrationRequest request = new UserRegistrationRequest("register", email, password);
                String requestJson = gson.toJson(request);
                out.println(requestJson);
                String response = in.readLine();
                if(Boolean.parseBoolean(response)){
                    taxMessage.setText("User is registered");
                } else {
                    taxMessage.setText("User is not registered");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });

//        check.setOnAction(e -> {
//            String email = emailField.getText();
//            String password  = passwordField.getText();
//            if(Database.verifyUser(email,password)){
//                taxMessage.setText("User is verified");
//            } else {
//                taxMessage.setText("User is not verified");
//            }
//        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
