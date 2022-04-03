import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.image.Image;

public class MenuItem {
    public String name;
    private ArrayList<String> ingredients;
    private String imageSrc;
    private double price;
    private int prepareTime;
    private Image image = null;

    public MenuItem(String name, String[] ingredients, String imageSrc, double price, int prepareTime) {
        this.name = name;
        this.ingredients = new ArrayList<>(List.of(ingredients));
        this.imageSrc = imageSrc;
        this.price = price;
        this.prepareTime = prepareTime;
    }

    public String getName(){
        return _name;
    }
    public List<String> getIngredients(){
        return _ingredients;
    }
    public String getImgSrc(){
        return _imageSrc;
    }
    public Image getImg(){
        if(image == null)
            image = new Image(getImgSrc());
        return image;
    }
    public Image getImg(){
        // Prevent the application from crashing if the image is missing
        if (image == null && !getImgSrc().isBlank()) {
            try {
                image = new Image(getImgSrc());
            } catch (IllegalArgumentException ignored) {}
        }

        return image;
    }
    public double getPrice(){
        return _price;
    }
    public int getPrepareTime(){
        return _prepareTime;
    }
    public void setName(String name){

    }
    public void setIngredients(String[] ingredients){

    }
    public void setImgSrc(String source){

    }
    public void setPrice(double price){
        this.price = price;
    }
}
