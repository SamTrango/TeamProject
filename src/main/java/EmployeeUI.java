import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;

public class EmployeeUI extends VBox
{
    private final POSApplication mApp;
    private AddEditMenuItemPopup mAddEditPopup;
    private ListView<Customer> mCustomerList;

    public EmployeeUI(POSApplication app) {
        mApp = app;
        mAddEditPopup = new AddEditMenuItemPopup(mApp.getWindow());

        // Configure the VBox
        setFillWidth(true);

        // Create the tab pane
        TabPane tabPane = new TabPane();
        Tab menuTab = new Tab("Menu");
        Tab customersTab = new Tab("Customers");

        buildMenuTab(menuTab);
        buildCustomersTab(customersTab);

        // Configure TabPane settings
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getTabs().addAll(menuTab, customersTab);
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        // Create a pane for the bottom of the UI
        VBox bottomPane = new VBox();
        bottomPane.setStyle("-fx-background-color: #FFFFFF;");
        bottomPane.setFillWidth(true);

        // Create separator out of a Pane, because the real "Separator" class is garbage
        Pane separator = new Pane();
        separator.setMinHeight(1);
        separator.setStyle("-fx-background-color: lightgray;");

        // Create the logout button
        Button logOutButton = new Button("Log Out");
        logOutButton.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(logOutButton, new Insets(5, 10, 10, 10));
        logOutButton.setOnAction(actionEvent -> {
            Alert logOutAlert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to log out?",
                    ButtonType.NO, ButtonType.YES);

            Optional<ButtonType> response = logOutAlert.showAndWait();
            if (response.isPresent() && response.get() == ButtonType.YES)
                mApp.loggedOut();
        });

        bottomPane.getChildren().addAll(separator, logOutButton);
        getChildren().addAll(tabPane, bottomPane);
    }

    public void resetUI() {
        // Set the selected tab to the Menu tab
        ((TabPane)getChildren().get(0)).getSelectionModel().select(0);

        // Refresh the list of Customers, since we did not provide a way to update this
        // in real-time and this will be good enough for now
        mCustomerList.getItems().clear();
        for (User user : mApp.getUsers().getUsers()) {
            if (!user.isEmployee())
                mCustomerList.getItems().add((Customer)user);
        }
    }

    private class EmployeeObserver extends MenuListView.Observer
    {
        @Override
        public void editButtonPressed(MenuItem item) {
            mAddEditPopup.addOrEdit(item);
        }

        @Override
        public void removeButtonPressed(MenuItem item) {
            Alert removeAlert = new Alert(Alert.AlertType.CONFIRMATION,
                    String.format("Are you sure you want to remove \"%s\" from the menu?", item.getName()),
                    ButtonType.NO, ButtonType.YES);

            Optional<ButtonType> response = removeAlert.showAndWait();
            if (response.isPresent() && response.get() == ButtonType.YES)
                mApp.getMenu().removeItem(item);
        }
    }

    private class AddEditMenuItemPopup extends Stage
    {
        private TextField mNameField, mPriceField;
        private Button mPickImgButton, mCancelButton, mSaveButton;
        private ImageView mImage;
        private String mImageSrc;
        private TextArea mIngredientsArea;
        private MenuItem mItem;

        public AddEditMenuItemPopup(Window owner) {
            // Make it so that you cannot interact with the base application while this window is open
            initModality(Modality.APPLICATION_MODAL);
            initOwner(owner);

            // Create the UI components for the add/edit window
            Label nameLabel = new Label("Name:");
            Label priceLabel = new Label("Price:");
            Label ingredientsLabel = new Label("Ingredients:");

            mNameField = new TextField();
            mPriceField = new TextField();

            mPickImgButton = new Button("Pick Image");
            mCancelButton = new Button("Cancel");
            mSaveButton = new Button("Save");

            mImage = new ImageView();
            mIngredientsArea = new TextArea();

            // Configure layout options
            mImage.setFitWidth(200);
            mImage.setFitHeight(200);

            GridPane.setVgrow(mIngredientsArea, Priority.SOMETIMES);
            GridPane.setHgrow(mCancelButton, Priority.SOMETIMES);
            GridPane.setHgrow(mSaveButton, Priority.SOMETIMES);

            mPickImgButton.setMaxWidth(Double.MAX_VALUE);
            mCancelButton.setMaxWidth(Double.MAX_VALUE);
            mSaveButton.setMaxWidth(Double.MAX_VALUE);

            // Arrange the components
            GridPane grid = new GridPane();
            grid.setPadding(new Insets(10, 10, 10, 10));
            grid.setVgap(10);
            grid.setHgap(10);

            grid.add(mImage, 0, 0, 1, 4);
            grid.add(nameLabel, 1, 0);
            grid.add(mNameField, 2, 0);

            grid.add(priceLabel, 1, 1);
            grid.add(mPriceField, 2, 1);

            grid.add(ingredientsLabel, 1, 2);
            grid.add(mIngredientsArea, 1, 3, 2, 1);

            grid.add(mPickImgButton, 0, 4);
            grid.add(mCancelButton, 1, 4);
            grid.add(mSaveButton, 2, 4);

            // Create and assign the new scene
            Scene scene = new Scene(grid, 500, 300);
            setScene(scene);

            // Setup behavior on window close
            setOnCloseRequest(windowEvent -> hide());

            // Setup behavior for buttons
            mPickImgButton.setOnAction(actionEvent -> {
                // Create a file picker for the user to select an image
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Pick an Image");
                chooser.getExtensionFilters().addAll
                        (new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));

                // Allow the user to select an image, then verify that we can load it and store the path
                File file = chooser.showOpenDialog(getScene().getWindow());
                if (file != null && file.exists()) {
                    try {
                        Image img = new Image(new FileInputStream(file));
                        mImage.setImage(img);
                        mImageSrc = file.getPath();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

            mCancelButton.setOnAction(actionEvent -> hide());

            mSaveButton.setOnAction(actionEvent -> {
                // Make sure that the name is not blank
                if (mNameField.getText().isBlank()) {
                    Alert noNameError = new Alert(Alert.AlertType.ERROR,
                            "The item cannot have a blank name!", ButtonType.OK);

                    noNameError.showAndWait();
                    return;
                }

                // Make sure that the item name is not already in use
                for (MenuItem item : mApp.getMenu().getItems()) {
                    if (item.getName().equals(mNameField.getText()) && item != mItem) {
                        Alert nameTakenError = new Alert(Alert.AlertType.ERROR,
                                "There is already a menu item with the name \"" + item.getName() + "\"!",
                                ButtonType.OK);

                        nameTakenError.showAndWait();
                        return;
                    }
                }

                // Make sure we can parse the price
                double price;
                try {
                    price = Double.parseDouble(mPriceField.getText().replace("$", ""));
                } catch (NumberFormatException e) {
                    Alert priceError = new Alert(Alert.AlertType.ERROR,
                            "Invalid price, please enter something with formatting similar to '$5.00'.",
                            ButtonType.OK);

                    priceError.showAndWait();
                    return;
                }

                String[] ingredients = mIngredientsArea.getText().split("\n");

                if (mItem == null) {
                    // Create a new MenuItem if there is no existing one
                    mItem = new MenuItem(mNameField.getText(), ingredients, mImageSrc, price, 0);
                } else {
                    // Assign the new settings to the existing MenuItem
                    mItem.setName(mNameField.getText());
                    mItem.setPrice(price);
                    mItem.setIngredients(ingredients);
                    mItem.setImgSrc(mImageSrc);

                    // We will remove and re-add this item so that the observable list updates
                    mApp.getMenu().getItems().remove(mItem);
                }

                mApp.getMenu().addItem(mItem);
                hide();
            });
        }

        public void addOrEdit(MenuItem item) {
            mItem = item;
            if (item != null) {
                // Set the title of the window
                setTitle(String.format("Edit %s", item.getName()));

                // Pre-populate the window with the data from this item
                mNameField.setText(item.getName());
                mPriceField.setText(String.format("%.2f", item.getPrice()));
                StringBuilder builder = new StringBuilder();
                item.getIngredients().forEach(s -> builder.append(s).append("\n"));
                mIngredientsArea.setText(builder.toString());
                mImage.setImage(item.getImg());
                mImageSrc = item.getImgSrc();
            } else {
                // We are adding a new item, erase all the text fields and put in a placeholder image
                setTitle("Add New Item");

                mNameField.setText("");
                mPriceField.setText("");
                mIngredientsArea.setText("");
                mImageSrc = "./food_images/placeholder.png";
                // Might fail if the placeholder image is not available
                try {
                    mImage.setImage(new Image(mImageSrc));
                } catch (IllegalArgumentException e) {
                    mImage.setImage(null);
                }
            }

            show();
        }
    }

    private void buildMenuTab(Tab menuTab) {
        // Build the Menu Tab
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setFillWidth(true);
        vBox.setPadding(new Insets(10, 10, 5, 10));

        MenuListView menuListView = new MenuListView(mApp.getMenu(),
                null, new EmployeeObserver(), false);

        Button addItemButton = new Button("Add New Item To Menu");
        addItemButton.setMaxWidth(Double.MAX_VALUE);
        addItemButton.setOnAction(actionEvent -> mAddEditPopup.addOrEdit(null));

        VBox.setVgrow(menuListView, Priority.ALWAYS);
        vBox.getChildren().addAll(menuListView, addItemButton);

        // Add the content to the tab
        menuTab.setContent(vBox);
    }

    private static class CustomerCell extends ListCell<Customer> {
        private GridPane mGrid = null;
        private Label mNameLabel, mOrdersLabel, mCouponsLabel;
        private Button mGiveCouponButton, mViewDetailsButton;

        @Override
        protected void updateItem(Customer customer, boolean empty) {
            super.updateItem(customer, empty);

            // Always set text to null, we never want to use it
            setText(null);

            // Draw nothing if this is an empty cell
            if (empty) {
                setGraphic(null);
                return;
            }

            // Initialize components if this is the first time the item is being used for real
            if (mGrid == null) {
                mGrid = new GridPane();
                mGrid.setHgap(5);
                mGrid.setVgap(5);
                mGrid.setPadding(new Insets(5, 5, 5, 5));

                // Make the column distribution 50/50
                ColumnConstraints constraints = new ColumnConstraints();
                constraints.setPercentWidth(50);
                mGrid.getColumnConstraints().addAll(constraints, constraints);

                mNameLabel = new Label();
                mOrdersLabel = new Label();
                mCouponsLabel = new Label();
                mGiveCouponButton = new Button("Give Coupon");
                mGiveCouponButton.setMaxWidth(Double.MAX_VALUE);
                mViewDetailsButton = new Button("View Details");
                mViewDetailsButton.setMaxWidth(Double.MAX_VALUE);

                // This is the chaos that is required in order to center a label
                // Sacrifices had to be made, the label on the left cannot give way anymore
                GridPane labelPane = new GridPane();

                GridPane.setHalignment(mOrdersLabel, HPos.CENTER);
                GridPane.setHalignment(mCouponsLabel, HPos.RIGHT);

                labelPane.add(mNameLabel, 0, 0);
                labelPane.add(mOrdersLabel, 1, 0);
                labelPane.add(mCouponsLabel, 2, 0);

                ColumnConstraints oneThirdConstraints = new ColumnConstraints();
                oneThirdConstraints.setPercentWidth(33.3);
                labelPane.getColumnConstraints().setAll(oneThirdConstraints, oneThirdConstraints, oneThirdConstraints);

                mGrid.add(labelPane, 0, 0, 2, 1);
                mGrid.add(mViewDetailsButton, 0, 1);
                mGrid.add(mGiveCouponButton, 1, 1);

                GridPane.setHgrow(mGiveCouponButton, Priority.SOMETIMES);
                GridPane.setHgrow(mViewDetailsButton, Priority.SOMETIMES);
            }

            // Update the label text for this item
            mNameLabel.setText(customer.getUsername());
            mOrdersLabel.setText(String.format("Orders: %d", customer.getNumberOfOrders()));
            mCouponsLabel.setText(String.format("Coupons: %d", customer.getNumberOfCoupons()));

            // Assign the functionality for the give coupon button
            mGiveCouponButton.setOnAction(actionEvent -> {
                customer.addCoupon();
                mCouponsLabel.setText(String.format("Coupons: %d", customer.getNumberOfCoupons()));
            });

            // Assign the functionality for the View Details button
            mViewDetailsButton.setOnAction(actionEvent -> {
                // Create a new window
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(getScene().getWindow());
                stage.setTitle("Details for " + customer.getUsername());

                // Generate the information text
                StringBuilder builder = new StringBuilder();
                builder.append("Username: ");
                builder.append(customer.getUsername());
                builder.append("\nAddress: ");
                builder.append(customer.getAddress());
                builder.append("\nPhone Number: ");
                builder.append(customer.getPhoneNumber());
                builder.append("\n\nNumber of Orders: ");
                builder.append(customer.getNumberOfOrders());
                builder.append("\nNumber of Coupons: ");
                builder.append(customer.getNumberOfCoupons());

                CreditCardInfo ccInfo = customer.getCreditCardInfo();
                if (ccInfo != null) {
                    builder.append("\n\nCredit Card #: ");
                    builder.append(ccInfo.getNumber());
                    builder.append("\nCredit Card Date: ");
                    builder.append(ccInfo.getMonth());
                    builder.append("/");
                    builder.append(ccInfo.getYear());
                    builder.append("\nCVV: ");
                    builder.append(ccInfo.getCode());
                }

                VBox vBox = new VBox();
                vBox.setFillWidth(true);
                vBox.setSpacing(10);
                vBox.setPadding(new Insets(10, 10, 10, 10));

                TextArea infoArea = new TextArea();
                infoArea.setText(builder.toString());
                infoArea.setEditable(false);
                VBox.setVgrow(infoArea, Priority.ALWAYS);

                Button closeButton = new Button("Close");
                closeButton.setMaxWidth(Double.MAX_VALUE);
                closeButton.setOnAction(actionEvent1 -> stage.close());

                vBox.getChildren().addAll(infoArea, closeButton);

                Scene scene = new Scene(vBox, 300, 300);
                stage.setScene(scene);
                stage.show();
            });

            // Use the grid we created as the graphic for this cell
            setGraphic(mGrid);
        }
    }

    private void buildCustomersTab(Tab customerTab) {
        // Create a new VBox, which will layout our CustomerView
        VBox vBox = new VBox();
        vBox.setFillWidth(true);
        vBox.setPadding(new Insets(10, 10, 5, 10));

        mCustomerList = new ListView<>();
        VBox.setVgrow(mCustomerList, Priority.ALWAYS);
        mCustomerList.setCellFactory(customerListView -> new CustomerCell());

        vBox.getChildren().addAll(mCustomerList);
        customerTab.setContent(vBox);
    }
}
