package model;
import java.util.UUID;
import java.util.ArrayList;

public class User extends Person {
    private final UUID userId = UUID.randomUUID();
    Address addressDelivery;
    protected ArrayList<Product> cart;

    public User(String firstName, String lastName, int age, Address addressDelivery) {
        super(firstName, lastName, age);
        this.addressDelivery = addressDelivery;
        cart = new ArrayList<Product>();
    }

    public UUID getUserId() {
        return userId;
    }


    public Address getAddressDelivery() {
        return addressDelivery;
    }

    public void setAddressDelivery(Address addressDelivery) {
        this.addressDelivery = addressDelivery;
    }


    public ArrayList<Product> getCart() {
        return cart;
    }


    public void emptyCart() { this.cart.clear(); }


    public String printCart() {
        String bag = "";
        bag += "Your Shopping Cart:\n";
        for(var p : this.cart) { bag += p.toString(); }
        return bag;
    }
}
