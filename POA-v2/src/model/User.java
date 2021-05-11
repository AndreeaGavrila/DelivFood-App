package model;

import service.ProductService;
import utils.ICsvConvertible;

import java.util.UUID;
import java.util.ArrayList;


public class User extends Person implements Comparable<User>, ICsvConvertible<User> {

    private UUID userId;
    Address addressDelivery;
    protected ArrayList<UUID> cart;
    protected ProductService ps = ProductService.getInstance();

    public User(UUID userId,String firstName, String lastName, int age, Address addressDelivery, ArrayList<UUID> cart) {
        super(firstName, lastName, age);
        this.userId = userId;
        this.addressDelivery = addressDelivery;
        this.cart = new ArrayList<UUID>(cart);
    }

    public User(String firstName, String lastName, int age, Address addressDelivery) {
        super(firstName, lastName, age);
        this.userId = UUID.randomUUID();
        this.addressDelivery = addressDelivery;
        this.cart = new ArrayList<UUID>();
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


    public ArrayList<UUID> getCart() {
        return cart;
    }


    public void emptyCart() { this.cart.clear(); }

    public void addToCart(UUID productId) {
        this.cart.add(productId);
    }


    public String printCart() {
        String bag = "";
        bag += "Your Shopping Cart:\n";
        for(var p : this.cart) { bag += ps.getProductById(p).get().printProduct(); }
        return bag;
    }

    @Override
    public int compareTo(User u) {
        int res = this.getLastName().compareTo(u.getLastName());
        if(res == 0){
            return this.getFirstName().compareTo(u.getFirstName());
        }
        return res;
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName;
    }

    @Override
    public String[] stringify() {
        // userId, firstName, lastName, age, street, number, city, numberOfProductsInCart, productId1, ..., productIdn
        ArrayList s = new ArrayList<String>();

        s.add(this.userId.toString());
        s.add(this.firstName);
        s.add(this.lastName);
        s.add(Integer.toString(this.age));
        s.add(this.addressDelivery.getStreet());
        s.add(Integer.toString(this.addressDelivery.getNumber()));
        s.add(this.addressDelivery.getCity());
        s.add(Integer.toString(this.cart.size()));
        for(var p : this.cart){
            s.add(p.toString());
        }

        return (String[])s.toArray(new String[0]);
    }

    public static User parse(String csv) {

        var parts = csv.split(",");
        UUID userId = UUID.fromString(parts[0]);
        String firstName = parts[1];
        String lastName = parts[2];
        int age = Integer.parseInt(parts[3]);
        Address address = new Address(
                parts[4], // street
                Integer.parseInt(parts[5]), // number
                parts[6]); // city
        var cart = new ArrayList<UUID>();
        for (int i = 0; i < Integer.parseInt(parts[7]); i++) {
            cart.add(UUID.fromString(parts[8 + i]));
        }
        return new User(userId, firstName, lastName, age, address, cart);
    }

}
