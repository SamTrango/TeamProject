import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.PasswordAuthentication;
import java.util.*;
import java.util.Map.Entry;

public class UserDatabase {
    Map<String, User> users = new HashMap<String, User>();


    void loadFromFile(String filename) throws FileNotFoundException {
        //username, password, isEmployee, address; phoneNumber; creditCard(year, month, date); numberOfOrders numberOfCoupons
        String readUsername = "";
        String readPassword = "";
        String readAddress = "";
        String readPhoneNumber = "";
        String tempBool = "";
        String num = "";        
        short readCode = 0;
        int readYear;
        int readMonth;         
        int readNumberOfOrders;
        int readnumberOfCupons;
        boolean readIsEmployee;
        
        Scanner read = new Scanner(new File(filename));
        while (read.hasNext()) {
            User newUser;
            readIsEmployee = false;
            readUsername = read.nextLine();
            readPassword = read.nextLine();
            tempBool = read.nextLine();            
            readIsEmployee = (tempBool.equals("true")) ? true : false;
            //The folowing inits will occur if the USER is a CUSTOMER.
            if (!readIsEmployee) {                                
                readAddress = read.nextLine();
                readPhoneNumber = read.nextLine();
                //ccnum
                num = read.nextLine();
                //code   
                readCode = read.nextShort();             
                readYear = read.nextInt();
                readMonth = read.nextInt();
                Date date = new Date(readYear, readMonth, 0);          
                readNumberOfOrders = read.nextInt();
                readnumberOfCupons = read.nextInt();
                CreditCardInfo ccinfo = new CreditCardInfo(num, date, readCode);
                //Make newUser a CUSTOMER.
                newUser = new Customer(readUsername, readPassword, readAddress, readPhoneNumber, ccinfo);                                
            }
            else {
                //Make newUser a USER (employee).
                newUser = new User(readUsername, readPassword, readIsEmployee);                
            }
            addUser(newUser);
        }
    }

    /**
     * 
     * @param filename
     */
    void storeToFile(String filename) throws IOException {        
        FileWriter fw = new FileWriter(filename);            
        List<String> keylist = new ArrayList<String>(users.keySet());
        for(int i = 0; i < users.size(); i++){
            fw.write(users.get(keylist.get(i)).getPassword());

        }
        //username, password, isEmployee, address; phoneNumber; creditCard(year, month,); numberOfOrders numberOfCoupons
        users.forEach((k, v) -> {
            if (v instanceof Customer) {
                Customer currentCust = (Customer) v;
                try{
                fw.write(currentCust.getUsername());
                fw.write(currentCust.getPassword());
                fw.write(currentCust.isEmployee());
                fw.write(currentCust.getAddress());
                fw.write(currentCust.getPhoneNUmber());
                CreditCardInfo cred = currentCust.getCreditCardInfo();                
                //fw.write(currentCust.getCreditCardInfo().toString());
                fw.write(cred.getNumber());
                fw.write(cred.getCode());
                fw.write(cred.getDate().getYear());
                fw.write(cred.getDate().getMonth());                
                fw.write(currentCust.getNumberOfOrders());                
                    fw.write(currentCust.getNumberOfCoupons());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if(v instanceof User){
                User currentUser = (User) v;
                try{
                    fw.write(currentUser.getUsername());
                    fw.write(currentUser.getPassword());
                    fw.write(currentUser.isEmployee());                
                }
                catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        );
    fw.close();
    }
    
    /**
    * 
    * @param newUser
    */
    void addUser(User newUser) {
        users.put(newUser.getUsername(), newUser);
    }

    /**
     * 
     * @param toLookup
     * @return
     */
    User lookupUser(String toLookup) {
        return users.get(toLookup);
    }

    /**
     * 
     * @return a list (collection) of USERS.
     */
    Collection<User> getUser() {
        return users.values();
    }
}
