/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author daniel
 * 
 */

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class LoginUI extends VBox {
    private final POSApplication _posApp;
    private BorderPane _mainScreen, _createAccountScreen;

    public LoginUI(POSApplication app) {
        _posApp = app;
        setFillWidth(true);
        resetUI();
    }

    private void resetMainScreen() {
        _mainScreen = new BorderPane();
        VBox.setVgrow(_mainScreen, Priority.ALWAYS);

        // Add padding to our main BorderPane
        _mainScreen.setPadding(new Insets(15, 15, 15, 15));

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
        PasswordField passwordField = new PasswordField();
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

        Button createAccountButton = new Button("Create New Account");
        createAccountButton.setMaxWidth(Double.MAX_VALUE);
        createAccountButton.setOnAction(arg0 -> {
            // Alert alert = new Alert(AlertType.ERROR, "Invalid Login Info", ButtonType.OK);
            // alert.showAndWait();
            resetAccountCreationScreen();
            getChildren().setAll(_createAccountScreen);
        });

        Button loginButton = new Button("Login");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setOnAction(arg0 -> {
            User user = _posApp.getUsers().lookupUser(usernameField.getText());

            // Check for valid username and password
            if (user != null && user.verifyPassword(passwordField.getText())) {
                _posApp.loggedIn(user); // Tell the main application that we are done logging in
            } else {
                // Notify the user that their login info is incorrect
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid login info, Please try again", ButtonType.OK);
                alert.showAndWait();
            }
        });

        bottomButtons.add(createAccountButton, 0, 0);
        bottomButtons.add(loginButton, 1, 0);

        _mainScreen.setTop(titleLabel);
        _mainScreen.setCenter(userInput);
        _mainScreen.setBottom(bottomButtons);
    }

    private void resetAccountCreationScreen() {
        _createAccountScreen = new BorderPane();
        VBox.setVgrow(_createAccountScreen, Priority.ALWAYS);

        // Add padding to our main BorderPane
        _createAccountScreen.setPadding(new Insets(15, 15, 15, 15));

        // Create the title label
        Label titleLabel = new Label("Account Creation");
        titleLabel.setFont(new Font(20));
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        GridPane userInput = new GridPane();
        userInput.setHgap(10);
        userInput.setVgap(10);

        String[] fieldNames = {"Username", "Password", "Address", "Phone Number", "Credit Card #"};
        TextField[] fields = new TextField[5];

        // Create all labels and text fields
        for (int i = 0; i < fieldNames.length; i++) {
            Label label = new Label(fieldNames[i] + ":");
            TextField field = new TextField();
            field.setPromptText("Enter your " + (fieldNames[i].toLowerCase()));
            GridPane.setHgrow(field, Priority.ALWAYS);

            fields[i] = field;

            userInput.add(label, 0, i);
            userInput.add(field, 1, i);
        }
      
        userInput.setAlignment(Pos.CENTER);

        // Create CC labels and text fields
        ComboBox<Integer> monthBox = new ComboBox<>();
        for (int i = 1; i <= 12; i++)
            monthBox.getItems().add(i);

        ComboBox<Integer> yearBox = new ComboBox<>();
        for (int i = 0; i <= 99; i++)
            yearBox.getItems().add(i);

        Label label = new Label("Credit Card Expiration Date: ");
        HBox hBox = new HBox();
        hBox.setFillHeight(true);
        hBox.setSpacing(5);
        hBox.setAlignment(Pos.CENTER);

        Label slash = new Label("/");
        slash.setFont(new Font(16));

        Label cvvLabel = new Label("\tCVV: ");
        TextField cvvField = new TextField();
        cvvField.setPromptText("Enter the CVV");

        hBox.getChildren().addAll(monthBox, slash, yearBox, cvvLabel, cvvField);

        userInput.add(label, 0, fieldNames.length);
        userInput.add(hBox, 1, fieldNames.length);

        GridPane bottomButtons = new GridPane();
        bottomButtons.setHgap(10);

        ColumnConstraints constraints = new ColumnConstraints();
        constraints.setPercentWidth(50);

        bottomButtons.getColumnConstraints().addAll(constraints, constraints);

        Button cancelButton = new Button("Cancel");
        cancelButton.setMaxWidth(Double.MAX_VALUE);
        cancelButton.setOnAction(arg0 -> resetUI());

        Button finishButton = new Button("Finish");
        finishButton.setMaxWidth(Double.MAX_VALUE);
        finishButton.setOnAction(arg0 -> {
            // Make sure that none of the fields are blank
            for (int i = 0; i < fieldNames.length; i++) {
                if (fields[i].getText().isBlank()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "The " + fieldNames[i] + " field must be filled in",
                            ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
            }

            if (monthBox.getSelectionModel().getSelectedItem() == null ||
                    yearBox.getSelectionModel().getSelectedItem() == null ||
                    cvvField.getText().isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The credit card info must be filled in",
                        ButtonType.OK);
                alert.showAndWait();
                return;
            }

            // Ensure that the new account username is unique
            if (_posApp.getUsers().lookupUser(fields[0].getText()) != null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The entered username is already taken",
                        ButtonType.OK);
                alert.showAndWait();
                return;
            }

            // Make sure that security code is a number
            int cvv;
            try {
                cvv = Integer.parseInt(cvvField.getText().strip());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid CVV, Please try again",
                        ButtonType.OK);
                alert.showAndWait();
                return;
            }

            // Get new account info from UI and create a new Customer
            CreditCardInfo ccInfo =
                    new CreditCardInfo(fields[4].getText(), monthBox.getValue(), yearBox.getValue(), cvv);
            Customer newCustomer =
                    new Customer(fields[0].getText(), fields[1].getText(), fields[2].getText(),
                            fields[3].getText(), ccInfo);

            // Add the new user to the user database
            _posApp.getUsers().addUser(newCustomer);

            // Log in
            _posApp.loggedIn(newCustomer);
        });

        bottomButtons.add(cancelButton, 0, 0);
        bottomButtons.add(finishButton, 1, 0);

        _createAccountScreen.setTop(titleLabel);
        _createAccountScreen.setCenter(userInput);
        _createAccountScreen.setBottom(bottomButtons);
    }

    public void resetUI() {
        resetMainScreen();
        getChildren().setAll(_mainScreen);
    }
}
