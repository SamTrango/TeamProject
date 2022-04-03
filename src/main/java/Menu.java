import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Menu{
    private ObservableList<MenuItem> items = FXCollections.observableArrayList();
   
    public void loadFromFile(String file) throws FileNotFoundException{ 
        //load from file format:
        //itemName,itemPrice,itemWaitTime,itemImgSrc
        //itemIngredient1,itemIngredient2,...
        Scanner scanner = new Scanner (new File(file));
        String itemName = ""; 
        String itemImgSrc = "";
        double itemPrice = 0.0;
        int itemPrepTime = 0;
        String[] itemIgredients;
        boolean ingredientsLineRead = false;
        while(scanner.hasNext()){
            String[] sections = scanner.nextLine().split(",");
            if(!ingredientsLineRead){
                itemName = sections[0];
                itemPrice = Double.parseDouble(sections[1]);
                itemPrepTime = Integer.parseInt(sections[2]);
                itemImgSrc = sections[3];
                ingredientsLineRead = true;
                
            }
            else{
                itemIgredients = new String[sections.length];
                for(int i = 0; i < sections.length; i++){
                    itemIgredients[i] = sections[i];
                }
                items.add(new MenuItem(itemName, itemIgredients, itemImgSrc, itemPrice, itemPrepTime));
                ingredientsLineRead = false;
            }
        }
    }

    public void storeToFile(String file) throws IOException{
        FileWriter fw = new FileWriter(file);
        for(int i = 0; i < items.size();i++){
            fw.write(items.get(i).getName() + "," +
            Double.toString(items.get(i).getPrice()) + "," +
            items.get(i).getPrepTime() + "," +
            items.get(i).getImgSrc() + "\n");
            int ingredientCount = 0;
            for(String str : items.get(i).getIngredients()){
                ingredientCount++;
                fw.write(str);
                if(items.get(i).getIngredients().size() == ingredientCount)
                    break;
                fw.write(",");
            }
            fw.write("\n");
        }
        fw.close();
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
