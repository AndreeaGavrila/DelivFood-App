package service;

import model.Order;
import model.OrderStatus;
import utils.CsvReadWrite;
import utils.Pair;

import java.util.*;
import java.util.stream.Collectors;


public class OrderService {

    private static OrderService instance;

    public static OrderService getInstance() {
        if (instance == null) {
            instance = new OrderService();
        }
        return instance;
    }

    private ArrayList<Order> orders;

    private final DriverService driverService;


    public OrderService() {

        orders = new ArrayList<Order>();
        driverService = DriverService.getInstance();
    }


    public String getOrderStatus(UUID orderId) {

        return this.orders.stream()
                .filter(o -> o.getOrderId().equals(orderId))
                .map(o -> o.getStatus())
                .findFirst()
                .get()
                .toString();
    }


    public Optional<Order> getOrderById(UUID orderId) {

        return this.orders.stream()
                .filter(o -> o.getOrderId() == orderId)
                .findFirst();
    }


    public void showOrders() {

        System.out.println("ALL the Orders:\n");
        for(var o : this.orders) {
            System.out.println(o.toString());
            System.out.println("");
        }
    }


    public ArrayList<Pair<UUID, String>> getUncompletedOrdersByUserId(UUID userId){

        var lst =  orders.stream()
                .filter(o ->
                        o.getUserId().equals(userId) &&
                                (o.getStatus().equals(OrderStatus.Placed) ||o.getStatus().equals(OrderStatus.Delivering)))
                .map(o -> new Pair<UUID, String>(o.getOrderId(), o.toString()))
                .collect(Collectors.toList());
        return new ArrayList<>(lst);
    }


    public void showUserOrderHistory(UUID userId) {

        var userOrders = orders.stream()
                .filter(o -> o.getUserId() == userId)
                .collect(Collectors.toList());

        Collections.sort(userOrders);
        System.out.println("User Order Status:\n");
        for(var o : userOrders) {
            System.out.println(o.toString());
            System.out.println("");
        }
    }


    public void addOrder(Order order) {
        orders.add(order);
    }


    public void cancelOrder(UUID orderId) {

        var orderOpt = this.getOrderById(orderId);
        orderOpt.ifPresentOrElse(
                order -> order.setStatus(OrderStatus.Cancelled),
                () -> System.out.println("Order not found!")
        );
    }


    public void assignDriverToOrder(UUID orderId, UUID driverId) throws Exception {

        var orderOpt = this.getOrderById(orderId);
        var order = orderOpt.orElseThrow(() -> new Exception("Order not found"));

        order.setDriverId(driverId);
        order.setStatus(OrderStatus.Delivering);
    }


    public void completeOrder(UUID orderId) throws Exception {

        var orderOpt = this.getOrderById(orderId);
        var order = orderOpt.orElseThrow(() -> new Exception("Order not found"));

        order.setStatus(OrderStatus.Completed);
        try {
            driverService.increaseCompletedDeliveries(order.getDriverId());
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
    }


    public void saveAll(String file_path) {

        if(this.orders == null) return;
        CsvReadWrite.writeAll(this.orders, file_path);
    }


    public void readAll(String file_path) {

        CsvReadWrite.readAll(file_path).ifPresent((csvs) -> {
            var lst = csvs.stream()
                    .map(csv -> Order.parse(csv))
                    .collect(Collectors.toList());
            this.orders = new ArrayList(lst);
        });
    }

}
