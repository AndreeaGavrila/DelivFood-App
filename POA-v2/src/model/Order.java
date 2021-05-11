package model;

import utils.ICsvConvertible;

import java.util.UUID;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import service.ProductService;


public class Order implements Comparable<Order>, ICsvConvertible<Order>  {

    private final UUID orderId;
    protected final UUID userId;
    protected UUID driverId;

    protected ZonedDateTime timePlaced;
    protected OrderStatus status;

    protected UUID[] products;

    protected ProductService ps = ProductService.getInstance();


    public Order(UUID orderId, UUID userId, UUID driverId,
                 ZonedDateTime timePlaced, OrderStatus status,
                 ArrayList<UUID> products) {

        this.orderId = orderId;
        this.userId = userId;
        this.driverId = driverId;
        this.timePlaced = ZonedDateTime.now();
        this.status = OrderStatus.Placed;
        this.products = new UUID[products.size()];
        for (int i = 0; i < products.size(); i++) {
            this.products[i] = products.get(i);
        }
    }

    public Order(UUID userId, ArrayList<UUID> products) {

        this.orderId = UUID.randomUUID();
        this.userId = userId;
        this.timePlaced = ZonedDateTime.now();
        this.status = OrderStatus.Placed;
        this.products = new UUID[products.size()];
        for (int i = 0; i < products.size(); i++) {
            this.products[i] = products.get(i);
        }
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


    public UUID[] getProducts() {
        return products;
    }

    public void setProducts(UUID[] products) {
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
            prod += ps.getProductById(p).get().printProduct();
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

    @Override
    public String[] stringify() {
        // orderId, userId, driverId, timePlaced, status, products, productId1, ..., productIdn
        ArrayList s = new ArrayList<String>();

        s.add(this.orderId.toString());
        s.add(this.userId.toString());
        if(this.driverId != null)
            s.add(this.driverId.toString());
        else
            s.add("null");
        s.add(this.timePlaced.toString());
        s.add(this.status.toString());
        s.add(Integer.toString(this.products.length));
        for(var p : this.products){
            s.add(p.toString());
        }

        return (String[])s.toArray(new String[0]);
    }

    public static Order parse(String csv) {

        var parts = csv.split(",");
        UUID orderId = UUID.fromString(parts[0]);
        UUID userId = UUID.fromString(parts[1]);
        UUID driverId = null;
        if(!parts[2].equals("null"))
            driverId = UUID.fromString(parts[2]);
        ZonedDateTime timePlaced = ZonedDateTime.parse(parts[3]);
        OrderStatus status = OrderStatus.valueOf(parts[4]);
        var products = new ArrayList<UUID>();
        for (int i = 0; i < Integer.parseInt(parts[5]); i++) {
            products.add(UUID.fromString(parts[6 + i]));
        }

        return new Order(orderId, userId, driverId, timePlaced, status, products);
    }
}