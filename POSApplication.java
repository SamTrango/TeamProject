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
    private UserDatabase mUserDatabase;
    private Menu mMenu;
    private OrderQueue mOrderQueue;
    private User mLoggedInUser;
    private LoginUI mLoginUI;
    private OrderUI mOrderUI;
    private EmployeeUI mEmployeeUI;

    public POSApplication() {
        mLoginUI = new LoginUI(this);
    }

    public void loggedIn(User user) {

    }

    public void loggedOut() {

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
        Scene scene = new Scene(mLoginUI, 640, 480);
        stage.setScene(scene);
        stage.show();
        
    }

    public static void main(String[] args) {
        launch();
    }
}