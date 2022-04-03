import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Menu {
    private ObservableList<MenuItem> items = FXCollections.observableArrayList();

    public void loadFromFile(String file){

    }

    public void storeToFile(String file){

    }

    public void addItem(MenuItem name){
        items.add(name);
    }

    public void removeItem(MenuItem name){
        items.remove(name);
    }

    public ObservableList<MenuItem> getItems(){
        return items;
    }

}
