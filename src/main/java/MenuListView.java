import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Comparator;

public class MenuListView extends VBox
{
    private Menu mMenu;
    private Order mOrder;
    private Observer mObserver;
    private boolean mIsCustomerView;

    public static class Observer
    {
        public void editButtonPressed(MenuItem item) {}
        public void removeButtonPressed(MenuItem item) {}

        public void addToCartUpdated(MenuItem item, boolean isChecked) {}
        public void viewIngredientsPressed(MenuItem item) {}
    }

    private class MenuItemCell extends ListCell<MenuItem> {
        private GridPane mGrid = null;
        private ImageView mImageView;
        private Label mNameLabel, mPriceLabel;
        private Node mLeftNode, mRightNode;

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

                if (mIsCustomerView) {
                    // Create a checkbox and view ingredients button for the customer view
                    CheckBox checkBox = new CheckBox("Add to Cart");
                    checkBox.setPadding(new Insets(0, 0, 3, 0));
                    GridPane.setHalignment(checkBox, HPos.CENTER);
                    checkBox.setOnAction(actionEvent -> mObserver.addToCartUpdated(item, checkBox.isSelected()));

                    Button viewIngredientsButton = new Button("View Ingredients");
                    viewIngredientsButton.setMaxWidth(Double.MAX_VALUE);
                    viewIngredientsButton.setOnAction(actionEvent -> mObserver.viewIngredientsPressed(item));

                    mLeftNode = checkBox;
                    mRightNode = viewIngredientsButton;
                } else {
                    // Create edit and remove buttons for the employee view
                    Button editButton = new Button("Edit");
                    editButton.setMaxWidth(Double.MAX_VALUE);
                    editButton.setOnAction(actionEvent -> mObserver.editButtonPressed(item));

                    Button removeButton = new Button("Remove");
                    removeButton.setMaxWidth(Double.MAX_VALUE);
                    removeButton.setOnAction(actionEvent -> mObserver.removeButtonPressed(item));

                    mLeftNode = editButton;
                    mRightNode = removeButton;
                }

                // Add elements to the GridView
                mGrid.add(mImageView, 0, 0, 1, 2);
                mGrid.add(mNameLabel, 1, 0);
                mGrid.add(mLeftNode, 1, 1);
                mGrid.add(mPriceLabel, 2, 0);
                mGrid.add(mRightNode, 2, 1);

                // Configure size and position for nodes
                GridPane.setValignment(mLeftNode, VPos.BOTTOM);
                GridPane.setValignment(mRightNode, VPos.BOTTOM);
                GridPane.setHgrow(mLeftNode, Priority.ALWAYS);
                GridPane.setHgrow(mRightNode, Priority.ALWAYS);
            }

            // Make sure the checkbox is checked if this item in the cart
            if (mIsCustomerView)
                ((CheckBox)mLeftNode).setSelected(mOrder.isInCart(item));

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

    public MenuListView(Menu menu, Order order, Observer observer, boolean isCustomerView) {
        mMenu = menu;
        mOrder = order;
        mObserver = observer;
        mIsCustomerView = isCustomerView;

        setSpacing(10);
        setFillWidth(true);
        //setPadding(new Insets(10, 10, 0, 10));

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setFillHeight(true);
        hBox.setSpacing(10);

        // Filter for the search box
        FilteredList<MenuItem> filteredMenuList = new FilteredList<>(menu.getItems(), item -> true);

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
        menuList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        menuList.setItems(sortedMenuList);

        HBox.setHgrow(searchBox, Priority.ALWAYS);
        hBox.getChildren().addAll(searchBox, sortChoice);

        setVgrow(menuList, Priority.ALWAYS);
        getChildren().addAll(hBox, menuList);
    }
}
