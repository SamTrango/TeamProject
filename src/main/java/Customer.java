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
    
    public String getAddress() {
        return address;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public CreditCardInfo getCreditCardInfo() {
        return creditCard;
    }
    
    public int getNumberOfOrders() {
        return 10;
    }
    
    public int getNumberOfCoupons() {
        return numberOfCoupons;
    }
    
    public void addCoupon() {
        numberOfCoupons++;
    }
    
    public void removeCoupon() {
        numberOfCoupons--;
    }
    
    
}
