import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class EmployeeUI extends TabPane
{
    private final POSApplication mApp;

    public EmployeeUI(POSApplication app) {
        mApp = app;

        Tab menuTab = new Tab("Menu");
        Tab customersTab = new Tab("Customers");

        buildMenuTab(menuTab);

        this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        this.getTabs().addAll(menuTab, customersTab);
    }

    private static class MenuItemCell extends ListCell<MenuItem> {
        private GridPane mGrid = null;
        private ImageView mImageView;
        private Label mNameLabel, mPriceLabel;
        private Button mEditButton, mRemoveButton;

        public MenuItemCell() {}

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

    private void buildMenuTab(Tab menuTab) {
        // Build the Menu Tab
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        vBox.setFillWidth(true);
        vBox.setPadding(new Insets(10, 10, 10, 10));

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
        menuList.setCellFactory(listView -> new MenuItemCell());

        menuList.setItems(sortedMenuList);

        Button logOutButton = new Button("Log Out");
        logOutButton.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(searchBox, Priority.ALWAYS);
        hBox.getChildren().addAll(searchBox, sortChoice);

        VBox.setVgrow(menuList, Priority.ALWAYS);
        vBox.getChildren().addAll(hBox, menuList, logOutButton);

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
