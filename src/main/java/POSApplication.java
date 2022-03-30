import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

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
        mOrderQueue = new OrderQueue(mMenu);
        mLoggedInUser = null;
        mLoginUI = new LoginUI(this);
        mOrderUI = new OrderUI(this);
        mEmployeeUI = new EmployeeUI(this);
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
        mScene = new Scene(mOrderUI, 640, 480);
        stage.setScene(mScene);
        stage.show();
    }

    public static void main(String[] args) {
        
        launch();
    }
}