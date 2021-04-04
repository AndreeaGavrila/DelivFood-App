package model;

public class Address {
    protected String city;
    protected String street;
    protected int number;

    public Address(String city, int number, String street) {
        this.city = city;
        this.number = number;
        this.street = street;
    }

    @Override
    public String toString() {
        return this.city + " " + this.number + ", " + this.street;
    }
}
