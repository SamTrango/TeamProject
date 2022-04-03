import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * JavaFX App
 */
public class POSApplication extends Application 
{
    private final UserDatabase mUserDatabase;
    private final Menu mMenu;
    private final OrderQueue mOrderQueue;
    private User mLoggedInUser;
    private LoginUI mLoginUI;
    private OrderUI mOrderUI;
    private EmployeeUI mEmployeeUI;

    private Stage mStage;
    private Scene mScene;

    public POSApplication() {
        mUserDatabase = new UserDatabase();
        mMenu = new Menu();
        mOrderQueue = new OrderQueue(mMenu);
        mLoggedInUser = new User("IanGay", "tanner", false);

        String[] ingredients1 = {"Tomatoes", "Potatoes"};
        mMenu.addItem(new MenuItem("BItem 1", ingredients1, "./food_images/salsa.jpg", 1.00, 10));
        mMenu.addItem(new MenuItem("CItem 2", ingredients1, "./food_images/salsa.jpg", 5.00, 5));
        mMenu.addItem(new MenuItem("AItem 3", ingredients1, "./food_images/salsa.jpg", 2.50, 20));
        Order order = new Order(false, 0.0, "IanGay");
        order.addToCart(new MenuItem("BItem 1", ingredients1, "./food_images/salsa.jpg", 1.00, 10));
        mOrderQueue.addOrder(order);
    }

    public void loggedIn(User user) {
        mLoggedInUser = user;
        mScene.setRoot(mOrderUI);
    }

    public void loggedOut() {
        mLoggedInUser = null;
        mScene.setRoot(mLoginUI);
        mLoginUI.startLogin();
    }

    public Menu getMenu() {
        return mMenu;
    }

    public OrderQueue getOrderQueue() {
        return mOrderQueue;
    }

    public UserDatabase getUsers() {
        return mUserDatabase;
    }

    public User getLoggedInUser() {
        return mLoggedInUser;
    }

    public Window getWindow() { return mStage; }

    @Override
    public void start(Stage stage) {
        mStage = stage;

        // Create user interface classes
        mLoginUI = new LoginUI(this);
        mOrderUI = new OrderUI(this);
        mEmployeeUI = new EmployeeUI(this);

        // Create a new scene and show the UI
        mScene = new Scene(mOrderUI, 720, 600);
        stage.setScene(mScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}