package model;
import java.util.UUID;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;

public class Order implements Comparable<Order> {
    private final UUID orderId = UUID.randomUUID();
    protected final UUID userId;
    protected UUID driverId;
    protected Product[] products;
    protected OrderStatus status;
    protected ZonedDateTime timePlaced;


    public Order(UUID userId, ArrayList<Product> products) {
        this.userId = userId;
        this.products = new Product[products.size()];
        for (int i = 0; i < products.size(); i++) {
            this.products[i] = new Product(products.get(i));
        }
        this.timePlaced = ZonedDateTime.now();
        this.status = OrderStatus.Placed;
    }

    public UUID getOrderId() {
        return orderId;
    }


    public UUID getUserId() {
        return userId;
    }


    public UUID getDriverId() {
        return driverId;
    }

    public void setDriverId(UUID driverId) {
        this.driverId = driverId;
    }


    public Product[] getProducts() {
        return products;
    }

    public void setProducts(Product[] products) {
        this.products = products;
    }


    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }


    public ZonedDateTime getTimePlaced() {
        return timePlaced;
    }

    public void setTimePlaced(ZonedDateTime timePlaced) {
        this.timePlaced = timePlaced;
    }


    @Override
    public String toString() {
        var prod = "";
        for (var p : this.products) {
            prod += p.printProduct();
        }

        return "Order #" + this.orderId +
                "\nTime placed: " + DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm").format(this.timePlaced) +
                "\nStatus: " + this.status +
                "\nItems: " + prod;
    }

    @Override
    public int compareTo(Order other) {
        return this.timePlaced.compareTo(other.timePlaced);
    }

}