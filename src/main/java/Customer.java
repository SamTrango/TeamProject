/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author daniel
 * 
 */
public class Customer extends User {
    private String _address;
    private String _phoneNumber;
    private CreditCardInfo _creditCard;
    private int _numberOfOrders = 0;
    private int _numberOfCoupons = 0;

    public Customer(String username, String password, String address, String phoneNumber, CreditCardInfo ccInfo) {
        super(username, password, false);

        _address = address;
        _phoneNumber = phoneNumber;
        _creditCard = ccInfo;
    }
    
    public String getAddress() {
        return _address;
    }
    
    public String getPhoneNUmber() {
        return _phoneNumber;

    }
    
    public CreditCardInfo getCreditCardInfo() {
        return _creditCard;
    }
    
    public int getNumberOfOrders() {
        return _numberOfOrders;
        
    }
    
    public int getNumberOfCoupons() {
        return _numberOfCoupons;
    }
    
    public void addCoupon() {
        _numberOfCoupons++;
    }
    
    public void removeCoupon() {
        _numberOfCoupons--;
        
    }
    
    
}
