import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.image.Image;

public class MenuItem {
    public String _name;
    private ArrayList<String> _ingredients;
    private String _imageSrc;
    private double _price;
    private int _prepareTime;
    private Image image = null;

    public MenuItem(String name, String[] ingredients, String imagSrc, double price, int prepareTime){
        _name = name;
        _ingredients = new ArrayList<String>(Arrays.asList(ingredients));
        _imageSrc = imagSrc;
        _price = price;
        _prepareTime = prepareTime;
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

    }
}
