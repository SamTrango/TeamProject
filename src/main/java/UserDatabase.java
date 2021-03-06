/**
 * @Title: UserDatabase.java
 * @Author: Sam Kacprowicz 
 * @Date: 3/31/2022
 * @Description: This class will contain a HashMap that will function as the database used to store the USER objects.
 * The class contains functionality to write the HashMap to a file, and read from that file, each via serialization.
 */


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;

public class UserDatabase {
    HashMap<String, User> users = new HashMap<>();

    /**
     * In order to use the loadFromFile and storeToFile methods, you need to add the phrase 'implements Serializable'
     * to the USER, CUSTOMER, and CREDICARDINFO classes. In addition, the file written should be of type '.ser', a
     * universal filetype, e.g., "UserList.ser".
     * @throws ClassNotFoundException, IOException
     */

    /**
     * @Description: This method will load the HashMap from the serialized file via 
     * deserialization.
     * @param filename - the name of the file that contains the serialized HashMap.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    boolean loadFromFile(String filename) {
        //--------- HashMap implements Serializable already -------
        try {
            FileInputStream fileIn = new FileInputStream(filename);     //Stream used to read from file.
            ObjectInputStream objectIn = new ObjectInputStream(fileIn); //Stream used to interpret the object's serialization.
            users = (HashMap<String, User>) objectIn.readObject();      //Must CAST the serialied object to the proper datatype to avoid ClassNotFoundException.
            objectIn.close();                                           //Close stream.
            fileIn.close();                                             //Close stream.
            return true;
        } catch (Exception ignored) {}
        return false;
    }

    /**
     * @Description: This method will use serialization to store the HashMap into 
     * a local file. It is reccomended to use the extension ".ser" as the file that
     * will contain the serialized HashMap, as ".ser" is universallly recognised 
     * in this context.
     * @param filename - the name of the file to be written to.
     * @throws IOException
     */
    void storeToFile(String filename) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(filename);  //Stream used to write to file.
        ObjectOutputStream out = new ObjectOutputStream(fileOut);   //Stream used to format the object's serialization.
        out.writeObject(users);                                     //Write object.
        out.close();                                                //Close stream.
        fileOut.close();                                            //Close stream.
    }
    
    /**
    * @Description: This method will add a USER object to the HashMap.
    * @param newUser - the USER to be added to the HashMap.
    */
    void addUser(User newUser) {
        users.put(newUser.getUsername(), newUser);
    }

    /**
     * @Description: This method will return a USER object from the HashMap, users.
     * @param toLookup - the username of the USER object to be returned.
     * @return
     */
    User lookupUser(String toLookup) {
        return users.get(toLookup);
    }

    /**
     * @Description: This method will return the USERS in the form of a COLLECTION.
     * @return: a list (collection) of USERS.
     */
    Collection<User> getUsers() {
        return users.values();
    }
}
