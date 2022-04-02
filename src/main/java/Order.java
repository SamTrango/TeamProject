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
        // Fetch the current count or put in a 1 if this is the addition
        Integer count =  items.putIfAbsent(item,1);
        if (count == null)
            return;
        items.replace(item, count + 1); //Replace old key with ne key with incremented value
    }

    public void removeFromCart(MenuItem item, boolean removeAll){
        Integer count = items.get(item);
        if(count == null)
            return;

        if (count == 1 || removeAll)
            items.remove(item);
        else  
            items.replace(item, count - 1);
    }

    public List<MenuItem> getItems(){//Create a list of MenuItems in Order
        ArrayList<MenuItem> orderItems = new ArrayList<MenuItem>();
        for (Map.Entry<MenuItem,Integer> entry : items.entrySet()){ 
            orderItems.add(entry.getKey());
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
        if(_hasCoupon){//Apply coupon discount,if applicaable
            total = total * (1 - _discountPercentage); 
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