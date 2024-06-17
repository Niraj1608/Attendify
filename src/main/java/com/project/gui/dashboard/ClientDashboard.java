package com.project.gui.dashboard;

import com.project.Cookie;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.project.gui.user.Main;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import static com.project.gui.user.Database.calculateAttendancePercentage;
import static com.project.gui.user.Database.getEmail;

public class ClientDashboard extends Application {

    private void loadProfilePicture(ImageView profilepic) {
        Properties props = new Properties();
        try {
            FileInputStream input = new FileInputStream("config.properties");
            props.load(input);
            String profilePicturePath = props.getProperty("profilePicturePath");
            input.close();

            // Check if a profile picture path is saved
            if (profilePicturePath != null) {
                // Load the profile picture from the saved path
                Image profileImage = new Image(new File(profilePicturePath).toURI().toString());
                profilepic.setImage(profileImage);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void start(Stage primaryStage) {

        String email = getEmail(Cookie.getUserid());

        primaryStage.setTitle("Client DashBoard");
        primaryStage.setResizable(false);
        Image icon = new Image(getClass().getResourceAsStream("/icon.png"));
        primaryStage.getIcons().add(icon);

        // Create a StackPane to hold the elements
        StackPane stackPane = new StackPane();
        Image logoImage = new Image(getClass().getResourceAsStream("/logo1.png"));


        ImageView imageViewlogo = new ImageView(logoImage);
        imageViewlogo.setFitWidth(130); // Set image width
        imageViewlogo.setFitHeight(130); // Set image height
        imageViewlogo.setTranslateY(5);
        imageViewlogo.setTranslateX(850);
        imageViewlogo.setPreserveRatio(true);




        // Create VBox to hold reset password components
        final VBox[] vbox = {new VBox(10)}; // 10 pixels spacing between elements
        VBox vbox2 = new VBox();
        vbox2.setStyle("-fx-background-color: #A6BCE5; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        vbox2.setMaxHeight(655);
        vbox2.setMaxWidth(325);
        StackPane.setAlignment(vbox2, Pos.BOTTOM_LEFT);

        //profile

        Image Proimage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/user.jpg")));
        ImageView profilepic = new ImageView(Proimage);
        loadProfilePicture(profilepic);
        profilepic.setTranslateX(100);
        profilepic.setTranslateY(10);
        Label Email = new Label(email); //get from database
        Email.setStyle("-fx-color:#000;-fx-font-size: 14px");
        Email.setTranslateY(70);
        Email.setTranslateX(70);

        Button Editpro = new Button("Edit Profile");
        Editpro.setMaxWidth(100);
        Editpro.setMaxHeight(45);
        Editpro.setTranslateY(90);
        Editpro.setTranslateX(100);
        Editpro.setOnAction(e -> {
        });
        Editpro.setStyle("-fx-background-color: #5C92FB; -fx-text-fill: white; -fx-font-size: 11px; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-background-radius: 12px;");

        // Set the desired size for the circular image
        double imageSize = 100; // Set the size of the circular image
        profilepic.setFitWidth(imageSize);
        profilepic.setFitHeight(imageSize);

        // Create a circular clip with half the size of the desired image size
        Circle clip = new Circle();
        clip.setCenterX(imageSize / 2); // Set the center X coordinate of the circle
        clip.setCenterY(imageSize / 2); // Set the center Y coordinate of the circle
        clip.setRadius(imageSize / 2); // Set the radius of the circle
        profilepic.setClip(clip);
        vbox[0].getChildren().clear();

        //add logo
        ImageView imageViewlogo2 = new ImageView(imageViewlogo.getImage());
        imageViewlogo2.setTranslateY(120);
        imageViewlogo2.setTranslateX(500);

        ImageView Discription = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Home.gif"))));
        Discription.setTranslateY(-100);
        Discription.setTranslateX(400);
        Discription.setFitWidth(600);
        Discription.setFitHeight(600);



        vbox[0].getChildren().addAll(imageViewlogo2,Discription); // Add logo to the VBox

        Button Home = new Button("Home");
        Home.setMaxWidth(325);
        Home.setMaxHeight(45);
        Home.setTranslateY(150);
        Home.setTranslateX(-120);
        Home.setStyle("-fx-background-color:#A6BCE5;-fx-font-size: 18px;");


        Button Comunity = new Button("Comunity");
        Comunity.setMaxWidth(325);
        Comunity.setMaxHeight(45);
        Comunity.setTranslateY(150);
        Comunity.setTranslateX(-110);
        Comunity.setStyle("-fx-background-color:#A6BCE5;-fx-font-size: 18px;");

        Button Help = new Button("Help and Support");
        Help.setMaxWidth(325);
        Help.setMaxHeight(45);
        Help.setTranslateY(150);
        Help.setTranslateX(-83);
        Help.setStyle("-fx-background-color:#A6BCE5;-fx-font-size: 18px;");

        Button SignOut = new Button("Sign Out");
        SignOut.setMaxWidth(100);
        SignOut.setMaxHeight(45);
        SignOut.setTranslateY(300);
        SignOut.setTranslateX(110);
        SignOut.setOnAction(e -> {
            primaryStage.close();
            openSignInPage();
        });
        SignOut.setStyle("-fx-background-color: #5C92FB; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-background-radius: 12px;");

        vbox2.getChildren().addAll( profilepic,Email,Editpro, createSeparator(0, 150), Home, createSeparator(0, 150), Comunity, createSeparator(0, 150), Help, createSeparator(0, 155), SignOut);
        VBox vbox3 = new VBox();
        vbox3.setMaxHeight(45);
        vbox3.setMaxWidth(1000);
        StackPane.setAlignment(vbox3, Pos.TOP_RIGHT);
        vbox3.setStyle("-fx-background-color: #D9D9D9; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        vbox3.getChildren().addAll(imageViewlogo); // Add logo to the VBox

        ComboBox<String> monthComboBox = new ComboBox<String>();
        monthComboBox.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
        monthComboBox.setTranslateY(90);
        monthComboBox.setTranslateX(500);
        monthComboBox.setStyle("-fx-font-size: 14px; -fx-pref-width: 150px;");
        monthComboBox.setValue("January");

        ComboBox<String> yearComboBox = new ComboBox<String>();
        yearComboBox.getItems().addAll("2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030","2031","2032","2033","2034","2035","2036","2037","2038","2039","2040");
        yearComboBox.setTranslateY(50);
        yearComboBox.setTranslateX(700);
        yearComboBox.setStyle("-fx-font-size: 14px; -fx-pref-width: 100px;");
        yearComboBox.setValue("2021");



        // Create circular progress bar (arc)
        Arc arc = new Arc(100, 100, 80, 80, 90, 0);
        arc.setType(ArcType.OPEN);
        arc.setStroke(Color.rgb(92, 169, 251));
        arc.setStrokeWidth(10);
        arc.setFill(null);
        arc.setTranslateX(500);
        arc.setTranslateY(250);
        arc.setStrokeWidth(20);
        //set shadow to arc


//Home BUtton
        Home.setOnAction(e -> {
            // Remove all elements from vbox
            vbox[0].getChildren().clear();

            //add logo



            vbox[0].getChildren().addAll(imageViewlogo2,Discription); // Add logo to the VBox


            // Add new elements

        });
// Edit Profile BUtton
        Editpro.setOnAction(e -> {
            // Remove all elements from vbox
            vbox[0].getChildren().clear();

            // Add new elements
            ImageView profilepic2 = new ImageView(profilepic.getImage());
            profilepic2.setTranslateX(600);
            profilepic2.setTranslateY(70);

            Label Change = new Label("Change Profile Picture");
            Change.setStyle("-fx-color:#5C92FB;-fx-font-size: 16px");
            Change.setTranslateY(60);
            Change.setTranslateX(600);
            Change.setTextFill(Color.rgb(92, 146, 251));

            // Inside the Change.setOnMouseClicked() event handler
            Change.setOnMouseClicked(event -> {
                // Create a file chooser
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose Profile Picture");

                // Filter only image files to be selectable
                FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif");
                fileChooser.getExtensionFilters().add(imageFilter);

                // Get the current stage
                Stage stage = (Stage) Change.getScene().getWindow();

                // Show the file chooser dialog
                File selectedFile = fileChooser.showOpenDialog(stage);

                // Check if a file is selected
                if (selectedFile != null) {
                    try {
                        // Get the path of the resources folder
                        File resourcesFolder = new File(System.getProperty("user.dir") + "/src/main/resources");

                        // Copy the selected file to the resources folder as propic.png
                        Path destinationPath = resourcesFolder.toPath().resolve("propic.png");
                        Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

                        // Save the path of the selected profile picture to config.properties
                        Properties props = new Properties();
                        props.setProperty("profilePicturePath", destinationPath.toString());
                        FileOutputStream output = new FileOutputStream("config.properties");
                        props.store(output, "Profile Picture Path");
                        output.close();

                        // Load the newly replaced propic.png into the profilepic2 and profilepic ImageViews
                        Image newImage = new Image(destinationPath.toUri().toString());
                        profilepic2.setImage(newImage);
                        profilepic.setImage(newImage);

                        // Optionally, update the circular clip and adjust the size of the new profile picture
                        double imageSize2 = 120;
                        profilepic2.setFitWidth(imageSize2);
                        profilepic2.setFitHeight(imageSize2);

                        Circle clip2 = new Circle();
                        clip2.setCenterX(imageSize2 / 2);
                        clip2.setCenterY(imageSize2 / 2);
                        clip2.setRadius(imageSize2 / 2);
                        profilepic2.setClip(clip2);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            double imageSize2 = 120;
            profilepic2.setFitWidth(imageSize2);
            profilepic2.setFitHeight(imageSize2);

            Circle clip2 = new Circle();
            clip2.setCenterX(imageSize2 / 2);
            clip2.setCenterY(imageSize2 / 2);
            clip2.setRadius(imageSize2 / 2);

            profilepic2.setClip(clip2);

            // Add the new profile picture to vbox
            vbox[0].getChildren().addAll(profilepic2, Change);
        });


        // comunity button

        Comunity.setOnAction(e->{
            vbox[0].getChildren().clear();



            Button perc= new Button("show attandace");
            perc.setMaxHeight(200);
            perc.setMaxWidth(150);
            perc.setStyle("-fx-background-color: #5C92FB; -fx-text-fill: white; -fx-font-size: 11px; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-background-radius: 12px;");
            perc.setTranslateX(600);
            perc.setTranslateY(70);

            perc.setOnAction(ex->{
                int year= Integer.parseInt(yearComboBox.getValue());
                int month= monthComboBox.getSelectionModel().getSelectedIndex()+1;
                updateProgressBar(arc, year, month);
                double per=calculateAttendancePercentage(Cookie.getUserid(), year, month);
                Label percentage=new Label("Attendance: " + per);
                percentage.setTranslateX(600);
                percentage.setTranslateY(300);
                vbox[0].getChildren().addAll(percentage);
            });

            vbox[0].getChildren().addAll(arc,monthComboBox,yearComboBox,perc);

        });


        // Help and Support Button
        Help.setOnAction(e->{
            vbox[0].getChildren().clear();
            // Add elements for Help and Support
            Label helpLabel = new Label("Help and Support");
            Label helpLabel2 = new Label("We understand that sometimes you may encounter difficulties or\n have questions while using our platform," +
                    " and we're here to assist you \n every step of the way.");
            helpLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
            helpLabel2.setStyle("-fx-font-size: 12px;");
            helpLabel.setTranslateY(180);
            helpLabel2.setTranslateY(180);
            helpLabel.setTranslateX(500);
            helpLabel2.setTranslateX(500);

            TextArea helpTextArea = new TextArea();
            helpTextArea.setPromptText("Type your query here...");
            helpTextArea.setMaxSize(300, 200);
            helpTextArea.setTranslateY(200);
            helpTextArea.setTranslateX(500);

            Button submitButton = new Button("Submit");
            submitButton.setTranslateY(210);
            submitButton.setTranslateX(600);
            submitButton.setStyle("-fx-background-color: #5C92FB; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-background-radius: 12px;");
            submitButton.setOnAction(event -> {
                // Get the text from the TextArea
                String query = helpTextArea.getText();

                System.out.println("Query submitted: " + query);

                // Optionally, you can display a confirmation message to the user
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Query Submitted");
                alert.setHeaderText(null);
                alert.setContentText("Your query has been submitted. We will get back to you soon.");
                alert.showAndWait();

                // Clear the TextArea after submission
                helpTextArea.clear();
            });

            vbox[0].getChildren().addAll(helpLabel,helpLabel2, helpTextArea, submitButton);
        });



        stackPane.getChildren().addAll(vbox[0], vbox2, vbox3);

        // Set up the scene
        Scene scene = new Scene(stackPane, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Separator createSeparator(double x, double y) {
        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);
        separator.setStyle("-fx-background-color: #5C92FB;  -fx-font-weight: bold;");
        separator.setTranslateX(x); // Translate the separator 10 pixels to the right
        separator.setTranslateY(y);
        return separator;
    }

    private void updateProgressBar(Arc arc, int year, int month) {

        double percentage;
        percentage = calculateAttendancePercentage(Cookie.getUserid(), year, month);

        double progressValue = percentage * 360 / 100; // Calculate progress value based on attendance percentage

        // Update the length of the arc to represent progress
        arc.setLength(-progressValue); // Negative angle for clockwise rotation
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
