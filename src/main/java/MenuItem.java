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

    public int getPrepTime(){
        return prepareTime;
    }
    public String getName(){
        return this.name;
    }

    public List<String> getIngredients(){
        return this.ingredients;
    }
    public String getImgSrc(){
        return this.imageSrc;
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
        return this.price;
    }
    public int getPrepareTime(){
        return this.prepareTime;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setIngredients(String[] ingredients){
        this.ingredients.clear();
        this.ingredients.addAll(List.of(ingredients));
    }
    public void setImgSrc(String source){
        this.imageSrc = source;
    }
    public void setPrice(double price){
        this.price = price;
    }
}
