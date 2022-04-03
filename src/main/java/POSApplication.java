import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

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

    public POSApplication() throws IOException {
        mUserDatabase = new UserDatabase();
        mMenu = new Menu();
        mOrderQueue = new OrderQueue(mMenu);
        mLoggedInUser = null;
    }

    public void loggedIn(User user) {
        mLoggedInUser = user;

        if (user.isEmployee()) {
            mEmployeeUI.resetUI();
            mScene.setRoot(mEmployeeUI);
            mStage.setTitle("POS - Employee View");
        } else {
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

    public static void main(String[] args) {
        launch();
    }
}