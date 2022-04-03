import java.util.ArrayList;
import java.util.List;
public class MenuItem {
    public String name;
    private ArrayList<String> ingredients;
    private String imageSrc;
    private double price;
    private int prepareTime;

    public String getName(){
        return name;
    }
    public List<String> getIngredients(){
        return ingredients;
    }
    public String getImgSrc(){
        return imageSrc;
    }
    public double getPrice(){
        return price;
    }
    public void setName(String name){

    }
    public void setIngredients(String[] ingredients){

    }
    public void setImgSrc(String source){

    }
    public void setPrice(double price){

    }
}
