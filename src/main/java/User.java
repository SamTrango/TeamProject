/**
 * @Title: User.java
 * @Author: Sam Kacprowicz
 * @Date:3/31/2022
 * @Description: This class will represent a USER of the POS system. It will feature methods
 * that will RETURN the username, verify the USER's password, and check to see if the USER is
 * an EMPLOYEE. The CUSTOMER class is a child of this class, however the EMPLOYEE class
 * is not.
 */

import java.io.Serializable;

public class User implements Serializable {
    private String username;    //Stores the name of the USER.
    private String password;    //Stores the password of the USER.
    private boolean isEmployee; //TRUE if the USER is an EMPLOYEE.

    /**
     * Constructor for USER object.
     * @param un - desired username.
     * @param pw - desired password.
     * @param employee - TRUE if USER will be employee, FALSE if user is CUSTOMER.
     */
    public User(String un, String pw, boolean employee) {
        this.username = un;
        this.password = pw;
        this.isEmployee = employee;
    }

    /**
     * @Description: This method will access, and RETURN the username that is
     * stored in each USER object.
     * @return the String instance variable representing the username.
     */
    public String getUsername() {
        return this.username;
    }
    
    /**
     * @Description: This method will evaluate the password a USER is attempting to use
     * to log in under a specific username. The method will check if the password
     * they are attempting to use matches the "password" field in the object that represents
     * the USER.
     * @param pfEntered: the password the USER is attempting to log in with.
     * @return: TRUE - if pfEntered equals the String stored in the field "password".
     *          FALSE -  if pfEntered does not equal the String stored in the field "password".
     */
    public boolean verifyPassword(String pfEntered) {
        return pfEntered.equals(this.password);
    }

    /**
     * @description: This method is used to descern whether the USER object is an 
     * EMPLOYEE. It will do so by RETURNing the value assigned to the USER object's
     * instance variable, "isEmployee".
     * @return: The value "isEmployee" is set.
     */
    public int isEmployee() {
        int rv = this.isEmployee ? 1 : 0;
        return rv;
    }
    
    /**
     * @Description: This method returns the User's password.
     * @return: password field from USER object.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * @Description: Standard toString method.
     */
    public @Override String toString() {
        return this.getUsername() + "\n" + this.getPassword() + "\n" + this.isEmployee() + "\n";
    }
}