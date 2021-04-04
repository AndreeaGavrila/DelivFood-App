package model;
import java.util.UUID;
import java.util.ArrayList;


public class Product {
    private UUID productId = UUID.randomUUID();
    protected String name;
    protected float price;
    protected ArrayList<String> ingredients;

    public Product(String name, float price, ArrayList<String> ingredients) {
        this.name = name;
        this.price = price;
        this.ingredients = new ArrayList<String>(ingredients);
    }

    public Product(Product other) {
        this.productId = other.productId;
        this.name = other.name;
        this.price = other.price;
        this.ingredients = new ArrayList<String>(other.ingredients);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }


    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }


    public String printIngredients() {
        String ing = "";
        for(var i : ingredients) {  ing += i + ", ";  }
        return ing;
    }

    public String printProduct() {
        return this.name +  " Price: " + this.price;
    }

    @Override
    public String toString() {
        return this.name +  "\nPrice: " + this.price +  "\nIngredients: " + this.printIngredients() + "\n";
    }
}
