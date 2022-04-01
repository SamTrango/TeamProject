import java.util.ArrayList;
public class MenuItem {
    public String name;
    private ArrayList<String> ingredients;
    private String imageSrc;
    private double price;
    private int prepareTime;
    
    public MenuItem(String name, ArrayList<String> ingredients, String imageSrc, double price, int prepareTime) {
        this.name = name;
        this.ingredients = ingredients;
        this.imageSrc = imageSrc;
        this.price = price;
        this.prepareTime = prepareTime;
    }

    public int getPrepTime(){
        return prepareTime;
    }
    public String getName(){
        return name;
    }
    public String[] getIngredients(){
        return (String[]) ingredients.toArray();
    }
    public String getImgSrc(){
        return imageSrc;
    }
    public double getPrice(){
        return price;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setIngredients(ArrayList<String> ingredients){
        this.ingredients = ingredients;
    }
    public void setImgSrc(String source){
        this.imageSrc = source;
    }
    public void setPrice(double price){
        this.price = price;
    }
}
