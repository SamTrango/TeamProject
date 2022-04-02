import javafx.collections.FXCollections;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;


public class OrderUI extends StackPane {
    private POSApplication app;
    private Order inProgressOrder;

    public OrderUI(POSApplication app) {
        this.app = app;
        showMenuAndCart();
    }

    private void showMenuAndCart() {
        //Menu UI Components
        SplitPane splitPane = new SplitPane();
        
        VBox menu = new VBox(new Label("Menu"));
        menu.setFillWidth(true);
        menu.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(0), Insets.EMPTY)));
        //placeholder for menu
        VBox menuList = new VBox();
        

        Button but_logOut = new Button("Log Out");
        but_logOut.setMaxWidth(Double.MAX_VALUE);
        but_logOut.setFont(new Font(15));
        but_logOut.setAlignment(Pos.BOTTOM_CENTER);
        VBox.setVgrow(but_logOut, Priority.ALWAYS);
        but_logOut.setOnAction((event) -> { 
            getChildren().clear();
            //app.loggedOut(); // Actual implementation
        });


        menu.getChildren().addAll(menuList,but_logOut);
        //Cart UI Components
        VBox cart = new VBox();

        ListView<MenuItem> orderList = new ListView<MenuItem>();
        orderList.setItems(FXCollections.observableArrayList(new MenuItem("name", "imagSrc", 1, 1), new MenuItem("name", "imagSrc", 1, 1))); ////remove after testing
        orderList.setCellFactory(lv -> new CustomCell());
        VBox.setVgrow(orderList, Priority.ALWAYS);

        Label lab_total = new Label();
        lab_total.setText(String.format("Total: $%.2f", 1.1)); //remove after testing
        //lab_total.setText(String.format("Total: $%.2f" + inProgressOrder.calculateTotalPrice()); // Actual implementation

        lab_total.setFont(new Font(15));

        Button but_order = new Button("Order");
        but_order.setFont(new Font(15));
        but_order.setMaxWidth(Double.MAX_VALUE);
        but_order.setOnAction((event) -> {
            getChildren().clear();
            showOrderProgress();
        });
        cart.getChildren().addAll(new Label("Cart"), orderList, lab_total, but_order);
        
        splitPane.getItems().addAll(menu,cart);
        splitPane.setDividerPositions(.6);
        getChildren().add(splitPane);
    }

    private void showOrderProgress() {
        VBox vBox = new VBox();
        Text txt_Msg = new Text("We're working on your order!");
        txt_Msg.setFont(new Font(30));
        Text txt_Pos = new Text("Position in line: #" );
        txt_Pos.setFont(new Font(30));
        Text txt_EstTime = new Text("Estimated Wait Time: ");
        txt_EstTime.setFont(new Font(30));
        Button but_Cancel = new Button("Cancel Order");
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
        
    }

    private BorderPane createOrderList(String itemName, int itemAmount, double itemPrice){//remove after testing
    //private BorderPane createOrderList(MenuItem item){ //actual implementation
        BorderPane bPane = new BorderPane();
        bPane.setPadding(new Insets(3, 3, 3, 3));

        Label txt_itemName = new Label(itemName);//remove after testing
        //Label txt_itemName = new Label(item); //actual implementation
        txt_itemName.setTextAlignment(TextAlignment.CENTER);
        txt_itemName.setFont(new Font(20));
        
        Button but_RemoveItem = new Button("X");
        but_RemoveItem.setPrefWidth(25);
        but_RemoveItem.setOnAction((event) ->{
            //inProgressOrder.removeFromCart(item, true); // Actual implementation
        });

        Button but_SubtractItem = new Button("-");
        but_SubtractItem.setPrefWidth(25);
        but_SubtractItem.setOnAction((event) ->{
            //inProgressOrder.removeFromCart(item, false); // Actual implementation
        });

        TextField txt_itemAmount = new TextField(Integer.toString(itemAmount));
        //TextField txt_itemAmount = new TextField(Integer.toString(itemAmount)); // Actual implementation
        txt_itemAmount.setFont(new Font(20));
        txt_itemAmount.setAlignment(Pos.CENTER);
        txt_itemAmount.setPrefWidth(50);
        txt_itemAmount.setPadding(new Insets(0,5,0,5));

        Button but_AddItem = new Button("+");
        but_AddItem.setPrefWidth(25);
        but_AddItem.setOnAction((event) ->{
            //inProgressOrder.addToCart(item); // Actual implementation
        });

        Text txt_itemTotalPrice = new Text();
        txt_itemTotalPrice.setFont(new Font(20));
        txt_itemTotalPrice.setText(String.format("$%.2f",itemAmount*itemPrice));//remove after testing
        //txt_itemTotalPrice.setText(String.format("$%.2f",inProgressOrder.priceForItem(item))); //actual implementation
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

            BorderPane bPane = createOrderList("itemName", 1, .5); //remove after testing
            //BorderPane bPane = createOrderList(item); //actual implementation
            setGraphic(bPane);
        }
    }
}