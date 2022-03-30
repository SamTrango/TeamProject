import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
public class MenuItem {
    public String name;
    private ArrayList<String> ingredients;
    private String imageSrc;
    private double price;
    private int prepareTime;
    private Image image = null;

    public MenuItem(String name, ArrayList<String> ingredients, String imageSrc, double price, int prepareTime) {
        this.name = name;
        this.ingredients = ingredients;
        this.imageSrc = imageSrc;
        this.price = price;
        this.prepareTime = prepareTime;
    }

    public String getName(){
        return name;
    }
    public List<String> getIngredients(){
        return ingredients;
    }
    public String getImgSrc(){
        return imageSrc;
    }
    public Image getImg(){
        if (image == null)
            image = new Image(getImgSrc());

        return image;
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
