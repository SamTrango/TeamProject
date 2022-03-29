import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order {
    private Map<MenuItem, Integer> items = new HashMap<MenuItem, Integer>();
    private boolean _hasCoupon = false;
    private double _discountPercentage;
    private String _username;

    public Order(boolean hasCoupon, double discountPercentage, String username){
        _hasCoupon = hasCoupon;
        _discountPercentage = discountPercentage;
        _username = username;
    }

    public void setHasCoupon(boolean hasCoupon){
        _hasCoupon = hasCoupon;
    }

    public void setDiscountPercentage(double discountPercentage){
        _discountPercentage = discountPercentage;
    }

    public void setUsername(String username){
        _username = username;
    }

    public boolean getHasCoupon(){
        return _hasCoupon;
    }

    public double getDiscountPercentage(){
        return _discountPercentage;
    }

    public String getUsername(){
        return _username;
    }

    public void addToCart(MenuItem item){
        if(items.putIfAbsent(item, 1) != null){ //Create a key if key doesn't exist and return null
            items.merge(item, 1, (a,b) -> a+b); //Replace old key with new key with incremented value
        }
    }

    public void removeFromCart(MenuItem item){
        if(items.containsKey(item)){//Check if key exists in Map
            if (items.get(item) != 1){
                items.merge(item, 1, (a,b) -> a-b);//Replace old key with new key with decremented value
            }
            else{//removes key from Order
                items.remove(item);
            }
        }
    }

    public List<MenuItem> getItems(){//Create a list of MenuItems in Order
        ArrayList<MenuItem> orderItems = new ArrayList<MenuItem>();
        for (Map.Entry<MenuItem,Integer> entry : items.entrySet()){ 
            orderItems.add(entry.getKey());;
        } 
        return orderItems;
    }

    public double priceForItem(MenuItem item){
        return item.getPrice() * items.get(item); // Item Price * # of items in Order
    }

    public double calculateTotalPrice(){
        double total = 0;
        for (Map.Entry<MenuItem,Integer> entry : items.entrySet()){ //Add total of all items in Order
            total += entry.getValue()*entry.getKey().getPrice();
        }
        if(_hasCoupon){//Apply coupon discount,if applicaable, and Round appropiately to 2 decimal places
            int decimalPlaces = 2;
            total = Math.round((total * (1 - _discountPercentage)) * Math.pow(10,decimalPlaces))/Math.pow(10,decimalPlaces); 
        }
        return total;
    }

    public int calculateTotalWaitTime(){
        int waitTime = 0;
        for (Map.Entry<MenuItem,Integer> entry : items.entrySet()){//Add total wait time for Order
            waitTime += entry.getValue()*entry.getKey().getPrepareTime();
        }
        return waitTime;
    }
}