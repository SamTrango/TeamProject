import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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

    private static final String USERS_FILE = "./users.ser";
    private static final String MENU_FILE = "./menu.csv";
    private static final String ORDERS_FILE = "./orders.csv";

    public POSApplication() {
        mUserDatabase = new UserDatabase();
        mMenu = new Menu();
        mOrderQueue = new OrderQueue(mMenu);
        mLoggedInUser = null;

        // Try to load users, or create a default admin account
        if (!mUserDatabase.loadFromFile(USERS_FILE))
            mUserDatabase.addUser(new User("admin", "admin", true));

        // Try to load the menu, if it exists
        try {
            mMenu.loadFromFile(MENU_FILE);
        } catch (Exception ignored) {}

        // Try to load the orders, if they exist
        try {
            mOrderQueue.loadFromFile(ORDERS_FILE);
        } catch (Exception ignored) {}
    }

    public void loggedIn(User user) {
        mLoggedInUser = user;

        if (user.isEmployee()) {
            mEmployeeUI.resetUI();
            mScene.setRoot(mEmployeeUI);
            mStage.setTitle("POS - Employee View");
        } else {
            mOrderUI.resetUI();
            mScene.setRoot(mOrderUI);
            mStage.setTitle("POS - Customer View");
        }
    }

    public void loggedOut() {
        mLoggedInUser = null;
        mLoginUI.resetUI();
        mScene.setRoot(mLoginUI);
        mStage.setTitle("POS - Login");
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
        mScene = new Scene(mLoginUI, 720, 600);
        stage.setScene(mScene);
        stage.show();

        // Call logged out to reset the UI to the login screen
        loggedOut();
    }

    @Override
    public void stop() {
        // Try to store users to a file
        try {
            mUserDatabase.storeToFile(USERS_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Try to store the menu to a file
        try {
            mMenu.storeToFile(MENU_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}