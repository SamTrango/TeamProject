/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author daniel
 */
public class Customer extends User {
    private String address;
    private String phoneNumber;
    private CreditCardInfo creditCard;
    private int numberOfOrders = 0;
    private int numberOfCoupons = 0;

    public Customer(String un, String pw, boolean employee) {
        super(un, pw, employee);
        address = "69 E gay place";
        phoneNumber = "132313131";
        creditCard = new CreditCardInfo("121", 11);
    }

    public String getAddress() {
        return "";
    }
    
    public String getPhoneNUmber() {
        return "";

    }
    
    public CreditCardInfo getCreditCardInfo() {
        return null;
    }
    
    public int getNumberOfOrders() {
        return 0;
        
    }
    
    public int getNumberOfCoupons() {
        return 0;
    }
    
    public void addCoupon() {
        
    }
    
    public void removeCoupon() {
        
    }
    
    
}
