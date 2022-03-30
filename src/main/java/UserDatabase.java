import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserDatabase {
    Map<String, User> users = new HashMap<String, User>();

    void loadFromFile(String filename){

    }
    
    void storeToFile(String filename) {

    }
    
    void addUser() {

    }
    
    User lookupUser(User toLookup) {
        return new User();
    }

    Collection<User> getUsers() {
        return users.values();
    }
}
