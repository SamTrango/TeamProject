import javafx.collections.ListChangeListener;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class OrderUI extends StackPane {
    private final POSApplication app;
    private SplitPane menuCart;
    private VBox orderWaitScreen;
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
    }

    public void resetUI(){
        inProgressOrder = findOrder();
        showMenuAndCart();
        getChildren().add(menuCart);
    }

    private void showMenuAndCart() {
        menuCart = new SplitPane();
        VBox menu = new VBox(new Label("Menu"));
        VBox cart = new VBox(new Label("Cart"));
        VBox menuList = new MenuListView(app.getMenu(), inProgressOrder, new MenuListObserver(), true);
        VBox prices = new VBox();
        Button but_logOut = new Button("Log Out");
        Button but_order = new Button("Order");
        Label lab_subtotal = new Label("Subtotal: $0.00");
        Label lab_discount = new Label("Discount: $0.00");
        Label lab_total = new Label("Total: $0.00");
        

        ListView<MenuItem> orderList = new ListView<>();
        orderList.setCellFactory(lv -> new CustomCell());
        orderList.setItems(inProgressOrder.getItems());
        inProgressOrder.getItems().addListener((ListChangeListener.Change<? extends MenuItem> change) -> {
            lab_subtotal.setText(String.format("Subtotal: $%.2f", inProgressOrder.calculateSubtotalPrice()));
            lab_discount.setText(String.format("Discount: $%.2f", inProgressOrder.calculateDiscountPrice()));
            lab_total.setText(String.format("Total: $%.2f", inProgressOrder.calculateTotalPrice()));
        });
        
        menu.setPadding(new Insets(5,5,5,5));
        menu.setFillWidth(true);
        menu.setSpacing(5);

        VBox.setVgrow(orderList, Priority.ALWAYS);
        VBox.setVgrow(menuList, Priority.ALWAYS);

        prices.setMaxWidth(Double.MAX_VALUE);

        lab_subtotal.setAlignment(Pos.CENTER);
        lab_subtotal.setFont(new Font(15));
        lab_discount.setAlignment(Pos.CENTER);
        lab_discount.setFont(new Font(15));
        lab_total.setAlignment(Pos.CENTER);
        lab_total.setFont(new Font(15));

        cart.setPadding(new Insets(5,5,5,5));
        cart.setSpacing(5);

        but_order.setAlignment(Pos.CENTER);
        but_order.setMaxWidth(Double.MAX_VALUE);
        but_order.setFont(new Font(15));

        but_logOut.setAlignment(Pos.CENTER);
        but_logOut.setMaxWidth(Double.MAX_VALUE);
        but_logOut.setFont(new Font(15));

        menuCart.setDividerPositions(.6);

        but_logOut.setOnAction((event) -> { 
            getChildren().remove(menuCart);
            app.loggedOut();
        });

        but_order.setOnAction((event) -> {
            app.getOrderQueue().addOrder(inProgressOrder);
            showOrderProgress();
        });

        prices.getChildren().addAll(lab_subtotal,lab_discount,lab_total);
        menu.getChildren().addAll(menuList,but_logOut);
        cart.getChildren().addAll( orderList, prices, but_order);
        menuCart.getItems().addAll(menu,cart);
    }

    private void showOrderProgress() {
        orderWaitScreen = new VBox();
        Text txt_Msg = new Text("We're working on your order!");
        Text txt_Pos = new Text("Position in line: " + findPositionInLine());
        Text txt_EstTime = new Text("Estimated Wait Time: " + convertWaitTime());
        Button but_Cancel = new Button("Cancel Order");

        txt_Msg.setFont(new Font(30));
        txt_Pos.setFont(new Font(30));
        txt_EstTime.setFont(new Font(30));
        but_Cancel.setFont(new Font(30));

        but_Cancel.setOnAction((event)->{
            app.getOrderQueue().removeOrder(inProgressOrder);
            getChildren().setAll(menuCart);          
        });

        orderWaitScreen.getChildren().addAll(txt_Msg, txt_Pos, txt_EstTime, but_Cancel);
        orderWaitScreen.setAlignment(Pos.CENTER);
        orderWaitScreen.setSpacing(5);

        getChildren().setAll(orderWaitScreen);
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

    private BorderPane createOrderList(MenuItem item){//Creates MenuItems in the cart
        BorderPane bPane = new BorderPane();
        bPane.setPadding(new Insets(3, 3, 3, 3));

        Label txt_itemName = new Label(item.getName()); 
        txt_itemName.setTextAlignment(TextAlignment.CENTER);
        txt_itemName.setFont(new Font(20));
        
        Button but_RemoveItem = new Button("X");
        but_RemoveItem.setPrefWidth(25);
        but_RemoveItem.setOnAction((event) ->{
            inProgressOrder.removeFromCart(item, true);
            app.getMenu().getItems().remove(item);
            app.getMenu().getItems().add(item);
        });

        Button but_SubtractItem = new Button("-");
        but_SubtractItem.setPrefWidth(25);
        but_SubtractItem.setOnAction((event) ->{
            inProgressOrder.removeFromCart(item, false);
            if (inProgressOrder.isInCart(item)) {
                MenuItem tempItem = inProgressOrder.getItems().remove(0);
                inProgressOrder.getItems().add(0, tempItem);
            } else {
                app.getMenu().getItems().remove(item);
                app.getMenu().getItems().add(item);
            }
        });

        TextField txt_itemAmount = new TextField(Integer.toString(inProgressOrder.getMenuItemAmount(item))); 
        txt_itemAmount.setEditable(false);
        txt_itemAmount.setFont(new Font(20));
        txt_itemAmount.setAlignment(Pos.CENTER);
        txt_itemAmount.setPrefWidth(50);
        txt_itemAmount.setPadding(new Insets(0,5,0,5));

        Button but_AddItem = new Button("+");
        but_AddItem.setPrefWidth(25);
        but_AddItem.setOnAction((event) ->{
            inProgressOrder.addToCart(item);
            MenuItem tempItem = inProgressOrder.getItems().remove(0);
            inProgressOrder.getItems().add(0, tempItem);
        });

        Text txt_itemTotalPrice = new Text();
        txt_itemTotalPrice.setFont(new Font(20));
        txt_itemTotalPrice.setText(String.format("$%.2f",inProgressOrder.priceForItem(item)));
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

    private class CustomCell extends ListCell<MenuItem>{ //Custom Cell class for ListView
        @Override
        public void updateItem(MenuItem item, boolean empty){
            super.updateItem(item,empty);
            setText(null);

            if(empty){ 
                setGraphic(null);
                return;
            }
            BorderPane bPane = createOrderList(item); 
            setGraphic(bPane);
        }
    }
    
    private int findPositionInLine(){//searches Order Queue list for inProgressOrder and return the positionInLine
        int PositionInLine = 1;
        for (Order order: app.getOrderQueue().getOrderQueueList()){
            if (order.equals(inProgressOrder)){
                return PositionInLine;
            }
            PositionInLine++;
        }
        return 0;
    }

    private String convertWaitTime(){//Converts wait time minutes to hours and minutes
        int hours = (int)Math.floor(inProgressOrder.calculateTotalWaitTime()/60.0);
        int minutes = inProgressOrder.calculateTotalWaitTime() % 60;
        if (hours != 0){
            return  hours + " hr " + minutes + " min";
        }
        else
            return minutes + " min";
    }

    private Order findOrder(){
        for(Order order : app.getOrderQueue().getOrderQueueList()){
            if (order.getUsername().equals(app.getLoggedInUser().getUsername()))
                return order;
        }
        return new Order((((Customer)app.getLoggedInUser()).getNumberOfCoupons() > 0), 0.85, app.getLoggedInUser().getUsername());
    }
}