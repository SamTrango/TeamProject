import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;

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
    private final OrderUI mOrderUI;
    private final EmployeeUI mEmployeeUI;

    private Scene mScene;

    public POSApplication() {
        mUserDatabase = new UserDatabase();
        mMenu = new Menu();
        mOrderQueue = new OrderQueue();
        mLoggedInUser = null;
        mLoginUI = new LoginUI(this);
        mOrderUI = new OrderUI(this);
        mEmployeeUI = new EmployeeUI(this);

        ArrayList<String> ingredients1 = new ArrayList<>(Arrays.asList("Tomatoes", "Potatoes"));
        mMenu.addItem(new MenuItem("BItem 1", ingredients1, "./food_images/salsa.jpg", 1.00, 10));
        mMenu.addItem(new MenuItem("CItem 2", ingredients1, "./food_images/salsa.jpg", 5.00, 5));
        mMenu.addItem(new MenuItem("AItem 3", ingredients1, "./food_images/salsa.jpg", 2.50, 20));
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

    public UserDatabase getUsers() {
        return mUserDatabase;
    }

    public User getLoggedInUser() {
        return mLoggedInUser;
    }

    @Override
    public void start(Stage stage) {
        mScene = new Scene(mEmployeeUI, 640, 480);
        stage.setScene(mScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}