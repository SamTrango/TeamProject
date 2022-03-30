import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class OrderUI extends SplitPane {
    private POSApplication app;
    private Order inProgressOrder;

    public OrderUI(POSApplication app) {
        this.app = app;
        showMenuAndCart();
    }

    private void showMenuAndCart() {
        //Menu UI Components
        VBox menu = new VBox(new Label("Menu"));
        menu.setPrefWidth(50);
        menu.setMinWidth(100);
        menu.setFillWidth(true);
        Button logOut = new Button("Log Out");
        logOut.setMaxWidth(Double.MAX_VALUE);
        
        menu.getChildren().add(logOut);

        logOut.setOnAction((event) -> {
            
        });
        
        //Cart UI Components
        VBox cart = new VBox();
        cart.setMinWidth(100);
        cart.getChildren().add(new Label("Cart"));

        ListView<HBox> orderList = new ListView<HBox>();
        HBox hbox1 = new HBox();
        Label label1 = new Label("Label");
        Button but_RemoveItem = new Button("X");
        Button but_SubtractItem = new Button("-");
        String itemAmount = "";
        Text txt_itemAmount = new Text(itemAmount);
        Button but_AddItem = new Button("+");
        String itemTotalPrice = "";
        Text txt_itemTotalPrice = new Text(itemTotalPrice);
        hbox1.setSpacing(10);
        hbox1.getChildren().addAll(but_RemoveItem, label1, but_SubtractItem, txt_itemAmount, but_AddItem, txt_itemTotalPrice);
        orderList.getItems().addAll(hbox1);
        //extra stuff
        cart.getChildren().add(orderList);
        
        Button orderButton = new Button("Order");
        GridPane.setHgrow(orderButton, Priority.ALWAYS);
        orderButton.setMaxWidth(Double.MAX_VALUE);

        orderButton.setOnAction((event) -> {
            
        });
        cart.getChildren().add(orderButton);
        
        getItems().addAll(menu,cart);
        setDividerPositions(.6);

        
    }

    private void showOrderProgress() {
        Stage stage = new Stage();
        VBox vBox = new VBox();
        Scene scene = new Scene(vBox);
        Text text1 = new Text("We're working on your order!");
        Text text2 = new Text("Position in line: #" );
        Text text3 = new Text("Estimated: ");
        vBox.getChildren().addAll(text1, text2, text3);

    }

    private void showDetailedView(MenuItem item) {

    }
}