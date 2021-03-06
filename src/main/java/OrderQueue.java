import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class OrderQueue {
    private ArrayList<Order> orderQueue = new ArrayList<>();
    private Menu _menu;
    
    public OrderQueue(Menu menu){
        _menu = menu;
    }

    public void loadFromFile(String file) throws FileNotFoundException {
        //load from file format:
        //hasCoupon,discountPercentage,username
        //menuItem,menuItemAmount,menuItem,menuItemAmount,...
        Scanner scanner = new Scanner(new File(file));
        boolean hasCoupon = false;
        double discountPercentage = 0.0;
        String username = "";
        while(scanner.hasNextLine()){
            String[] sections = scanner.nextLine().split(",");
            for (int i = 0; i < sections.length; i++)
                sections[i] = sections[i].strip();
            if(sections[0].contains("Order")){ //New order creation signaled by textfile
                hasCoupon = Boolean.parseBoolean(sections[1]);
                discountPercentage = Double.parseDouble(sections[2]);
                username = sections[3];
            }
            else{
                Order newOrder = new Order(hasCoupon, discountPercentage, username);
                for(int i = 0; i < sections.length; i+=2){//Import menuItem and amount into Order from text file
                    String menuItemName = sections[i];
                    int menuItemAmount = Integer.parseInt(sections[i+1]);
                    for(int j = 0; j < menuItemAmount; j++){
                        newOrder.addToCart(searchMenuItem(menuItemName));
                    }
                }
                orderQueue.add(newOrder);
            }
        }
        scanner.close();
    }

    public List<Order> getOrderQueueList(){
        return orderQueue;
    }

    public void addOrder(Order order) {
        orderQueue.add(order);
    }

    public void removeOrder(Order order) {
        orderQueue.remove(order);
    }

    private MenuItem searchMenuItem(String itemName){//Searches through Menu list for MenuItem;
        for(MenuItem item: _menu.getItems()){
            if(item.getName().equals(itemName)){
                return item;
            }
        }
        return null;
    }
}