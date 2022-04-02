import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
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

    public EmployeeUI(POSApplication app) {
        mApp = app;
        mAddEditPopup = new AddEditMenuItemPopup(mApp.getWindow());

        // Configure the VBox
        setFillWidth(true);
        //setSpacing(9);

        // Create the tab pane
        TabPane tabPane = new TabPane();
        Tab menuTab = new Tab("Menu");
        Tab customersTab = new Tab("Customers");

        buildMenuTab(menuTab);

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

        bottomPane.getChildren().addAll(separator, logOutButton);

        getChildren().addAll(tabPane, bottomPane);
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
                if (file.exists()) {
                    try {
                        Image img = new Image(new FileInputStream(file));
                        mImage.setImage(img);
                        mImageSrc = file.getAbsolutePath();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

            mCancelButton.setOnAction(actionEvent -> hide());

            mSaveButton.setOnAction(actionEvent -> {
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
                mImage.setImage(new Image(mImageSrc));
            }

            show();
        }
    }

    private void buildMenuTab(Tab menuTab) {
        // Build the Menu Tab
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setFillWidth(true);
        //vBox.setStyle("-fx-background-color: lightgray;");
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
}
