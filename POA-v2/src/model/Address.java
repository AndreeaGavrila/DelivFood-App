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


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }


    @Override
    public String toString() {
        return this.city + " " + this.number + ", " + this.street;
    }
}
