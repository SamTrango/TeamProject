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
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginUI extends BorderPane {
    private POSApplication _posApp;

    public LoginUI(POSApplication app) {
        _posApp = app;

        // Add padding to our main GridPane, enable grid lines for debugging
        setPadding(new Insets(15, 15, 15, 15));
        
        // Create the title label
        Label titleLabel = new Label("Restaurant Name");
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

        userInput.add(usernameLabel, 0, 0);
        userInput.add(usernameField, 1, 0);
        userInput.add(passwordLabel, 0, 1);
        userInput.add(passwordField, 1, 1);
        userInput.setAlignment(Pos.CENTER);

        GridPane bottomButtons = new GridPane();
        bottomButtons.setHgap(10);

        ColumnConstraints constraints = new ColumnConstraints();
        constraints.setPercentWidth(50);

        bottomButtons.getColumnConstraints().addAll(constraints, constraints);

        Button createAccountButton = new Button("Create Account");
        createAccountButton.setMaxWidth(Double.MAX_VALUE);
        createAccountButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                // Alert alert = new Alert(AlertType.ERROR, "Invalid Login Info", ButtonType.OK);
                // alert.showAndWait();
                Stage stage2 = new Stage();  //links create account button to createAccountUI that pops up
                BorderPane bp = new createAccountUI(app);
                //stage.show();
                Scene scene2 = new Scene(bp);
                stage2.setScene(scene2);
                stage2.show();

            }
        });

        Button loginButton = new Button("Login");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                // Alert alert = new Alert(AlertType.ERROR, "Invalid Login Info", ButtonType.OK);
                // alert.showAndWait();
            }
        });

        bottomButtons.add(createAccountButton, 0, 0);
        bottomButtons.add(loginButton, 1, 0);

        setTop(titleLabel);
        setCenter(userInput);
        setBottom(bottomButtons);
    }

    private void showMainScreen() {

        // Example of checking login information
        String username = "";
        String password = "";

        User user = _posApp.getUsers().lookupUser(username);

        // Check for valid username and password
        if (user != null && user.verifyPassword(password)) {
            _posApp.loggedIn(user); // Tell the main application that we are done logging in
        } else {
            // Notify the user that their login info is incorrect
            Alert alert = new Alert(AlertType.ERROR, "Invalid Login Info", ButtonType.OK);
            alert.showAndWait();
        }
     
    }

    private void showAccountCreation() {
        // Example of creating a new user

        // Ensure that the new account username is unique
        if (_posApp.getUsers().lookupUser("PutEnteredUsernameHere") != null) {
            // Get new account info from UI and create a new Customer
            CreditCardInfo ccInfo = new CreditCardInfo("CreditCardNumber", 0, 0, 0);
            Customer newCustomer = new Customer("Username", "Password", "Address", "PhoneNumber", ccInfo);

            // Add the new user to the user database
            _posApp.getUsers().addUser(newCustomer);
        } else {
            // Error because the username is already taken
        }
    }

    public void startLogin() {

    }
}
