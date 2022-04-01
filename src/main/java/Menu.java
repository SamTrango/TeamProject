import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.jar.Attributes.Name;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
public class Menu {
    private ArrayList<MenuItem> items;
    String line;
    int reading = 0;
    int itemCount = 0;
    int ingredientCount = 0;
    String temp;
    public void loadFromFile(String file) throws FileNotFoundException{ 
    Scanner read = new Scanner (new File(file)); 
    read.useDelimiter(",");
            String Name, imageSrc;
            ArrayList<String> ingredients = new ArrayList<String>();
            int price, prepareTime;
            for(int i = 0; i < itemCount;i++){

            while (read.hasNext()) //order of file is price, name, preptime, image source then ingredients
            {
               price = read.nextInt();
               Name = read.nextLine();
               imageSrc = read.next();
               prepareTime = read.nextInt();
               temp = read.nextLine();
               while(temp != null){
                   ingredients.add(ingredientCount,temp.substring(0, temp.indexOf(",")));
                   temp = temp.substring(temp.indexOf(","));
                   ingredientCount++;
               }
               MenuItem item = new MenuItem(Name,ingredients,imageSrc,price,prepareTime);
               addItem(item);
            }

        }

    }
    public void storeToFile(String file) throws IOException{
        FileWriter fw = new FileWriter(file);
        for(int i = 0; i < items.size();i++){
            fw.write((int) items.get(i).getPrice());
            fw.write(items.get(i).getName());
            fw.write(items.get(i).getImgSrc());
            fw.write(items.get(i).getPrepTime());
            fw.write(items.get(i).getIngredients().toString());
        }
        fw.close();
    }
    public void addItem(MenuItem name){        
        items.add(name);
    }
    public void removeItem(MenuItem name){
        items.remove(name);
    }
    public List<MenuItem> getItems(){
        return items;
    }

}
