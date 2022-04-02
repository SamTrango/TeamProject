import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
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
import java.util.Comparator;
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
        setSpacing(9);
        //setStyle("-fx-background-color: #DDDDDD;");

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
        VBox.setMargin(logOutButton, new Insets(9, 10, 10, 10));

        bottomPane.getChildren().addAll(separator, logOutButton);

        getChildren().addAll(tabPane, bottomPane);
    }

    private class MenuItemCell extends ListCell<MenuItem> {
        private GridPane mGrid = null;
        private ImageView mImageView;
        private Label mNameLabel, mPriceLabel;
        private Button mEditButton, mRemoveButton;
        private Menu mMenu;

        public MenuItemCell(Menu menu) { this.mMenu = menu; }

        @Override
        protected void updateItem(MenuItem item, boolean empty) {
            super.updateItem(item, empty);

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
                //mGrid.setGridLinesVisible(true);
                mGrid.setPadding(new Insets(5, 5, 5, 5));

                // Create new GUI elements
                mImageView = new ImageView();
                mNameLabel = new Label();
                mPriceLabel = new Label();
                mEditButton = new Button("Edit");
                mRemoveButton = new Button("Remove");

                // Add elements to the GridView
                mGrid.add(mImageView, 0, 0, 1, 2);
                mGrid.add(mNameLabel, 1, 0);
                mGrid.add(mEditButton, 1, 1);
                mGrid.add(mPriceLabel, 2, 0);
                mGrid.add(mRemoveButton, 2, 1);

                // Configure size and position for buttons
                mEditButton.setMaxWidth(Double.MAX_VALUE);
                mRemoveButton.setMaxWidth(Double.MAX_VALUE);
                GridPane.setValignment(mEditButton, VPos.BOTTOM);
                GridPane.setValignment(mRemoveButton, VPos.BOTTOM);
                GridPane.setHgrow(mEditButton, Priority.ALWAYS);
                GridPane.setHgrow(mRemoveButton, Priority.ALWAYS);
            }

            // Setup edit button
            mEditButton.setOnAction(actionEvent -> {
                mAddEditPopup.addOrEdit(item);
            });

            // Setup remove button
            mRemoveButton.setOnAction(actionEvent -> {
                Alert removeAlert = new Alert(Alert.AlertType.CONFIRMATION,
                        String.format("Are you sure you want to remove \"%s\" from the menu?", item.getName()),
                        ButtonType.NO, ButtonType.YES);

                Optional<ButtonType> response = removeAlert.showAndWait();
                if (response.isPresent() && response.get() == ButtonType.YES)
                    mMenu.removeItem(item);
            });

            // Configure images and labels for this item
            mImageView.setImage(item.getImg());
            mImageView.setFitWidth(100);
            mImageView.setFitHeight(100);
            mNameLabel.setText(item.getName());
            mPriceLabel.setText(String.format("$%.2f", item.getPrice()));

            // Use the grid we created as the graphic for this cell
            setGraphic(mGrid);
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
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        vBox.setFillWidth(true);
        vBox.setPadding(new Insets(10, 10, 0, 10));

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setFillHeight(true);
        hBox.setSpacing(10);

        // Filter for the search box
        FilteredList<MenuItem> filteredMenuList = new FilteredList<>(mApp.getMenu().getItems(), item -> true);

        // Comparators for the different sorting modes
        Comparator<MenuItem> alphabeticalComparator = Comparator.comparing(MenuItem::getName);
        Comparator<MenuItem> priceLowToHighComparator = (o1, o2) -> (int)((o1.getPrice() - o2.getPrice())*100);
        Comparator<MenuItem> priceHighToLowComparator = (o1, o2) -> (int)((o2.getPrice() - o1.getPrice())*100);

        SortedList<MenuItem> sortedMenuList = new SortedList<>(filteredMenuList, alphabeticalComparator);

        // Setup the search box
        TextField searchBox = new TextField();
        searchBox.setPromptText("Start Typing to Search");
        searchBox.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue.isBlank())
                filteredMenuList.setPredicate(item -> true);
            else
                filteredMenuList.setPredicate(
                        item -> item.getName().toLowerCase().contains(newValue.toLowerCase()));
        });

        // Setup the sort selection choice box
        ChoiceBox<String> sortChoice = new ChoiceBox<>();
        sortChoice.getItems().addAll("Alphabetical", "Low to High Price", "High to Low Price");
        sortChoice.getSelectionModel().select("Alphabetical");
        sortChoice.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldValue, newValue) -> {
            switch (newValue.intValue()) {
                case 0 -> sortedMenuList.setComparator(alphabeticalComparator);
                case 1 -> sortedMenuList.setComparator(priceLowToHighComparator);
                case 2 -> sortedMenuList.setComparator(priceHighToLowComparator);
            }
        });

        ListView<MenuItem> menuList = new ListView<>();
        menuList.setCellFactory(listView -> new MenuItemCell(mApp.getMenu()));
        menuList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        menuList.setItems(sortedMenuList);

        Button addItemButton = new Button("Add New Item To Menu");
        addItemButton.setMaxWidth(Double.MAX_VALUE);
        addItemButton.setOnAction(actionEvent -> mAddEditPopup.addOrEdit(null));

        HBox.setHgrow(searchBox, Priority.ALWAYS);
        hBox.getChildren().addAll(searchBox, sortChoice);

        VBox.setVgrow(menuList, Priority.ALWAYS);
        vBox.getChildren().addAll(hBox, menuList, addItemButton);

        // Add the content to the tab
        menuTab.setContent(vBox);
    }

    private void showEditMenu() {

    }

    private void showEditMenuItem() {

    }

    private void showCustomerInfo() {
        
    }
}
