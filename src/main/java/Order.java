import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order {
    private Map<MenuItem, Integer> items = new HashMap<MenuItem, Integer>();
    private boolean hasCoupon = false;
    private double discountPercentage;
    private String username;

    public Order(boolean hasCoupon, double discoutPercentage){

    }

    public void addToCart(MenuItem item){

    }

    public void removeFromCart(MenuItem item){

    }

    public List<MenuItem> getItems(){
        return new ArrayList<>();
    }

    public double priceForItem(MenuItem item){
        return 0;
    }

    public double calculateTotalPrice(){
        return 0;
    }

    public int calculateTotalWaitTime(){
        return 0;
    }
}