import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class OrderUI extends StackPane {
    private POSApplication app;
    private Order inProgressOrder;

    public class MenuListObserver extends MenuListView.Observer{
        @Override
        public void addToCartUpdated(MenuItem item, boolean isChecked) {
            if (isChecked){
                inProgressOrder.addToCart(item);
            }
            else{
                inProgressOrder.removeFromCart(item, true);
            }

        }
        @Override
        public void viewIngredientsPressed(MenuItem item) {
            showDetailedView(item);
        }
    }

    public OrderUI(POSApplication _app) {
        app = _app;
        for(Order order: app.getOrderQueue().getOrderQueue()){
            if (order.getUsername().equals(app.getLoggedInUser().getUsername()))
                inProgressOrder = order;
        }
        showMenuAndCart();
    }

    private void showMenuAndCart() {
        SplitPane splitPane = new SplitPane();
        VBox menu = new VBox(new Label("Menu"));
        VBox cart = new VBox(new Label("Cart"));
        VBox menuList = new MenuListView(app.getMenu(), inProgressOrder, new MenuListObserver(), true);
        Button but_logOut = new Button("Log Out");
        Button but_order = new Button("Order");
        Label lab_total = new Label();
        ListView<MenuItem> orderList = new ListView<MenuItem>();
        orderList.setCellFactory(lv -> new CustomCell());
        orderList.setItems(inProgressOrder.getItems());
        //lab_total.setText(String.format("Total: $%.2f", 1.1)); //remove after testing
        lab_total.setText(String.format("Total: $%.2f", inProgressOrder.calculateTotalPrice())); // Actual implementation
        
        but_logOut.setAlignment(Pos.BOTTOM_CENTER);
        VBox.setVgrow(orderList, Priority.ALWAYS);
        VBox.setVgrow(menuList, Priority.ALWAYS);
        but_logOut.setMaxWidth(Double.MAX_VALUE);
        but_order.setMaxWidth(Double.MAX_VALUE);
        menu.setPadding(new Insets(5,5,5,5));
        cart.setPadding(new Insets(5,5,5,5));
        splitPane.setDividerPositions(.6);
        but_logOut.setFont(new Font(15));
        lab_total.setFont(new Font(15));
        but_order.setFont(new Font(15));
        menu.setFillWidth(true);
        menu.setSpacing(5);
        cart.setSpacing(5);

        but_logOut.setOnAction((event) -> { 
            //getChildren().clear();
            app.loggedOut();
        });

        but_order.setOnAction((event) -> {
            getChildren().clear();
            showOrderProgress();
        });

        menu.getChildren().addAll(menuList,but_logOut);
        cart.getChildren().addAll( orderList, lab_total, but_order);
        splitPane.getItems().addAll(menu,cart);
        getChildren().add(splitPane);
    }

    private void showOrderProgress() {
        VBox vBox = new VBox();
        Text txt_Msg = new Text("We're working on your order!");
        Text txt_Pos = new Text("Position in line: " );
        Text txt_EstTime = new Text("Estimated Wait Time: " + convertWaitTime());
        Button but_Cancel = new Button("Cancel Order");

        txt_Msg.setFont(new Font(30));
        txt_Pos.setFont(new Font(30));
        txt_EstTime.setFont(new Font(30));
        but_Cancel.setFont(new Font(30));

        but_Cancel.setOnAction((event)->{
            getChildren().clear();
            showMenuAndCart();
        });

        vBox.getChildren().addAll(txt_Msg, txt_Pos, txt_EstTime, but_Cancel);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);

        getChildren().add(vBox);
    }

    private void showDetailedView(MenuItem item) {
        Stage stage = new Stage();
        BorderPane bPane = new BorderPane();
        Scene scene = new Scene(bPane, 500, 500);
        ImageView img = new ImageView(item.getImg());
        ListView<String> ingredients = new ListView<>();

        BorderPane.setMargin(img, new Insets(5,5,5,5));
        BorderPane.setMargin(ingredients, new Insets(5,5,5,0));
        BorderPane.setAlignment(img, Pos.CENTER);
        img.setFitWidth(200);
        img.setFitHeight(200);
        

        ingredients.getItems().addAll(item.getIngredients());
        bPane.setLeft(img);
        bPane.setCenter(ingredients);
        stage.setScene(scene);
        stage.setTitle("Ingredients in " + item.getName());
        stage.show();
    }

    //private BorderPane createOrderList(String itemName, int itemAmount, double itemPrice){//remove after testing
    private BorderPane createOrderList(MenuItem item){ //actual implementation
        BorderPane bPane = new BorderPane();
        bPane.setPadding(new Insets(3, 3, 3, 3));

        //Label txt_itemName = new Label(itemName);//remove after testing
        Label txt_itemName = new Label(item.getName()); //actual implementation
        txt_itemName.setTextAlignment(TextAlignment.CENTER);
        txt_itemName.setFont(new Font(20));
        
        Button but_RemoveItem = new Button("X");
        but_RemoveItem.setPrefWidth(25);
        but_RemoveItem.setOnAction((event) ->{
            inProgressOrder.removeFromCart(item, true); // Actual implementation
        });

        Button but_SubtractItem = new Button("-");
        but_SubtractItem.setPrefWidth(25);
        but_SubtractItem.setOnAction((event) ->{
            inProgressOrder.removeFromCart(item, false); // Actual implementation
        });

        //TextField txt_itemAmount = new TextField(Integer.toString(itemAmount));
        TextField txt_itemAmount = new TextField(Integer.toString(inProgressOrder.getMenuItemAmount(item))); // Actual implementation
        txt_itemAmount.setEditable(false);
        txt_itemAmount.setFont(new Font(20));
        txt_itemAmount.setAlignment(Pos.CENTER);
        txt_itemAmount.setPrefWidth(50);
        txt_itemAmount.setPadding(new Insets(0,5,0,5));

        Button but_AddItem = new Button("+");
        but_AddItem.setPrefWidth(25);
        but_AddItem.setOnAction((event) ->{
            inProgressOrder.addToCart(item); // Actual implementation
        });

        Text txt_itemTotalPrice = new Text();
        txt_itemTotalPrice.setFont(new Font(20));
        //txt_itemTotalPrice.setText(String.format("$%.2f",inProgressOrder.calculateTotalPrice()));//remove after testing
        txt_itemTotalPrice.setText(String.format("$%.2f",inProgressOrder.priceForItem(item))); //actual implementation
        txt_itemTotalPrice.setTextAlignment(TextAlignment.CENTER);

        HBox hbox1 = new HBox(but_RemoveItem,txt_itemName);
        hbox1.setAlignment(Pos.CENTER);
        hbox1.setSpacing(5);

        HBox hbox2 = new HBox(but_SubtractItem,txt_itemAmount,but_AddItem);
        hbox2.setAlignment(Pos.CENTER);
        hbox2.setSpacing(5);

        bPane.setLeft(hbox1);
        BorderPane.setAlignment(hbox1, Pos.CENTER);
        bPane.setCenter(hbox2);
        BorderPane.setAlignment(hbox2, Pos.CENTER);
        bPane.setRight(txt_itemTotalPrice);
        BorderPane.setAlignment(txt_itemTotalPrice, Pos.CENTER);
        return bPane;
    }

    private class CustomCell extends ListCell<MenuItem>{
        @Override
        public void updateItem(MenuItem item, boolean empty){
            super.updateItem(item,empty);
            if(item == null) 
                return;

            //BorderPane bPane = createOrderList("itemName", 1, .5); //remove after testing
            BorderPane bPane = createOrderList(item); //actual implementation
            setGraphic(bPane);
        }
    }

    private String convertWaitTime(){
        int hours = (int)Math.floor(inProgressOrder.calculateTotalWaitTime()/60);
        int minutes = inProgressOrder.calculateTotalWaitTime() % 60;
        if (hours != 0){
            return  hours + " hr " + minutes + " min";
        }
        else
            return minutes + " min";
    }
}