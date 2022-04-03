import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Order {
    private Map<MenuItem, Integer> _items = new HashMap<MenuItem, Integer>(); //Ian is a bitch and wants me to make this an observable map
    private ObservableList<MenuItem> _observableList;
    private boolean _hasCoupon = false;
    private double _discountPercentage;
    private String _username;

    public Order(boolean hasCoupon, double discountPercentage, String username){
        _hasCoupon = hasCoupon;
        _discountPercentage = discountPercentage;
        _username = username;
        _observableList = getItems();
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

    public ObservableList<MenuItem> getObservableList(){
        return _observableList;
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

    public int getMenuItemAmount(MenuItem item){
        return _items.get(item);
    }
    public void addToCart(MenuItem item){
        // Fetch the current count or put in a 1 if this is the addition
        Integer count =  _items.putIfAbsent(item,1);
        if (count == null)
            return;
        _items.replace(item, count + 1); //Replace old key with new key with incremented value
    }

    public void removeFromCart(MenuItem item, boolean removeAll){
        Integer count = _items.get(item);
        if(count == null)
            return;

        if (count == 1 || removeAll)
            _items.remove(item);
        else  
            _items.replace(item, count - 1);
    }

    public ObservableList<MenuItem> getItems(){//Create a ObservableList of MenuItems in Order
        List<MenuItem> orderItems = new ArrayList<MenuItem>();
        for (Map.Entry<MenuItem,Integer> entry : _items.entrySet()){ 
            orderItems.add(entry.getKey());
        }
        return FXCollections.observableList(orderItems);
    }

    public double priceForItem(MenuItem item){
        return item.getPrice() * _items.get(item); // Item Price * # of items in Order
    }

    public double calculateTotalPrice(){
        double total = 0;
        for (Map.Entry<MenuItem,Integer> entry : _items.entrySet()){ //Add total of all items in Order
            total += entry.getValue()*entry.getKey().getPrice();
        }
        if(_hasCoupon){//Apply coupon discount,if applicaable
            total = total * (1 - _discountPercentage); 
        }
        return total;
    }

    public int calculateTotalWaitTime(){
        int waitTime = 0;
        for (Map.Entry<MenuItem,Integer> entry : _items.entrySet()){//Add total wait time for Order
            waitTime += entry.getValue()*entry.getKey().getPrepareTime();
        }
        return waitTime;
    }

    public boolean isInCart(MenuItem item){
        return _items.containsKey(item);
    }

}