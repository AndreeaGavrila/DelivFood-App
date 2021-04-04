package model;
import java.util.UUID;
import java.util.ArrayList;

public class Restaurant {
    private final UUID restaurantId = UUID.randomUUID();
    protected String name;
    protected Address location;
    protected String description;
    protected ArrayList<Product> products;
    protected ArrayList<Review> reviews;


    public Restaurant(String name, Address location, String description) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.products = new ArrayList<Product>();
        this.reviews = new ArrayList<Review>();
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Address getLocation() {
        return location;
    }

    public void setLocation(Address location) {
        this.location = location;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }


    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }


    @Override
    public String toString() {
        return "Restaurant " + this.name +
                "\nDescription: " + this.description +
                "\nAddress: " + this.location.toString();
    }

}
