/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author daniel
 */

public class CreditCardInfo implements Serializable{

    private String _ccnumber;
    private int _month;
    private int _year;
    private int _code;

    public CreditCardInfo(String ccNum, int month, int year, int secCode) {
        _ccnumber = ccNum;
        _month = month;
        _year = year;
        _code = secCode;
    }

    public String getNumber() {
        return _ccnumber;
    }

    public int getMonth() {
        return _month;
    }

    public int getYear() {
        return _year;
    }

    public int getCode() {
        return _code;
    }

}