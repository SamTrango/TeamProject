public class User {
    String username;
    String password;
    boolean isEmployee;

    public User(String username, String password, boolean isEmployee) {}

    public String getUsername() {
        return "";
    }
    
    public boolean verifyPassword(String password) {
        return true;
    }

    public boolean isEmployee() {
        return false;
    }
}
