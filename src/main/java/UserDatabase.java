import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.*;

public class UserDatabase {
    Map<String, User> users = new HashMap<String, User>();

    void loadFromFile(String filename){
        //username, password, isEmployee, address; phoneNumber; creditCard; numberOfOrders numberOfCoupons
    
    }
    
    void storeToFile(String filename) {

    }
    
    void addUser() {

    }
    
    User lookupUser(String toLookup) {
        return new User();
    }

    /**
     * 
     * @return a list (collection) of USERS.
     */
    Collection<User> getUser() {
        return users.values();
        //return new User();
    }
}
