import java.util.HashMap;
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

    public MenuItem[] getItems(){

    }

    public double priceForItem(MenuItem item){

    }

    public double calculateTotalPrice(){

    }

    public int calculateTotalWaitTime(){

    }
}