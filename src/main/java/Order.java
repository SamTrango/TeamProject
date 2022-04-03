import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Order {
    private Map<MenuItem, Integer> _items = new HashMap<MenuItem, Integer>();
    private ObservableList<MenuItem> _observableList = FXCollections.observableArrayList();
    private boolean _hasCoupon;
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

    public ObservableList<MenuItem> getItems(){
        return _observableList;
    }

    public void addToCart(MenuItem item){
        // Fetch the current count or put in a 1 if this is the addition
        Integer count =  _items.putIfAbsent(item,1);
        if (count == null){
            _observableList.add(item);
            return;
        }
        _items.replace(item, count + 1); //Replace old key with new key with incremented value
    }

    public void removeFromCart(MenuItem item, boolean removeAll){
        Integer count = _items.get(item);
        if (count == null)
            return;

        if (count == 1 || removeAll){
            _items.remove(item);
            _observableList.remove(item);
        } else {
            _items.replace(item, count - 1);
        }
    }

    public double priceForItem(MenuItem item){
        return item.getPrice() * _items.get(item); // Item Price * # of items in Order
    }

    public double calculateSubtotalPrice(){
        double subtotal = 0;
        for (Map.Entry<MenuItem,Integer> entry : _items.entrySet()){ //Add total of all items in Order
            subtotal += entry.getValue()*entry.getKey().getPrice();
        }
        return subtotal;
    }

    public double calculateDiscountPrice(){
        if (!_hasCoupon)
            return 0.0;

        return calculateSubtotalPrice() * (1-_discountPercentage);
    }

    public double calculateTotalPrice(){
        return calculateSubtotalPrice() - calculateDiscountPrice();
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