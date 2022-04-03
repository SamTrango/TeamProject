/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author daniel
 * 
 */

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class createAccountUI extends BorderPane {

    private POSApplication _posApp;

    public createAccountUI(POSApplication app) {
        _posApp = app;

        // Add padding to our main GridPane, enable grid lines for debugging
        setPadding(new Insets(15, 15, 15, 15));
        
        // Create the title label
        Label titleLabel = new Label("Create Customer Account");
        titleLabel.setFont(new Font(40));
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        GridPane userInput = new GridPane();
        userInput.setHgap(10);
        userInput.setVgap(10);

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        GridPane.setHgrow(usernameField, Priority.ALWAYS);

        Label passwordLabel = new Label("Password:");
        TextField passwordField = new TextField();
        passwordField.setPromptText("Enter your password");
        GridPane.setHgrow(passwordField, Priority.ALWAYS);

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");
        GridPane.setHgrow(nameField, Priority.ALWAYS);

        Label addressLabel = new Label("Address:");
        TextField addressField = new TextField();
        addressField.setPromptText("Enter your address");
        GridPane.setHgrow(addressField, Priority.ALWAYS);

        Label phoneNumberLabel = new Label("Phone Number:");
        TextField phoneNumberField = new TextField();
        phoneNumberField.setPromptText("Enter your phone number");
        GridPane.setHgrow(phoneNumberField, Priority.ALWAYS);

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        GridPane.setHgrow(emailField, Priority.ALWAYS);

        Label ccLabel = new Label("Credit Card Number:");
        TextField ccField = new TextField();
        ccField.setPromptText("Enter your Credit Card Number");
        GridPane.setHgrow(ccField, Priority.ALWAYS);


        userInput.add(usernameLabel, 0, 0);
        userInput.add(usernameField, 1, 0);
        userInput.add(passwordLabel, 0, 1);
        userInput.add(passwordField, 1, 1);
        userInput.add(nameLabel, 0, 2);
        userInput.add(nameField, 1, 2);
        userInput.add(addressLabel, 0, 3);
        userInput.add(addressField, 1, 3);
        userInput.add(phoneNumberLabel, 0, 4);
        userInput.add(phoneNumberField, 1, 4);
        userInput.add(emailLabel, 0, 5);
        userInput.add(emailField, 1, 5);
        userInput.add(ccLabel, 0, 6);
        userInput.add(ccField, 1, 6);
        userInput.setAlignment(Pos.CENTER);

        GridPane bottomButtons = new GridPane();
        bottomButtons.setHgap(10);

        ColumnConstraints constraints = new ColumnConstraints();
        constraints.setPercentWidth(50);

        bottomButtons.getColumnConstraints().addAll(constraints, constraints);

        Button cancelButton = new Button("Cancel");
        cancelButton.setMaxWidth(Double.MAX_VALUE);
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                // Alert alert = new Alert(AlertType.ERROR, "Invalid Login Info", ButtonType.OK);
                // alert.showAndWait();
           
            }
        });

        Button nextButton = new Button("Finish");
        nextButton.setMaxWidth(Double.MAX_VALUE);
        nextButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                // Alert alert = new Alert(AlertType.ERROR, "Invalid Login Info", ButtonType.OK);
                // alert.showAndWait();
            }
        });

        bottomButtons.add(cancelButton, 0, 0);
        bottomButtons.add(nextButton, 1, 0);

        setTop(titleLabel);
        setCenter(userInput);
        setBottom(bottomButtons);
    }

    

    

   
}
