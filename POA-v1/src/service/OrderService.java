package service;
import model.Order;
import model.OrderStatus;
import java.util.*;
import java.util.stream.Collectors;

public class OrderService {
    private final ArrayList<Order> orders;
    private final DriverService driverService;

    public OrderService(DriverService ds) {
        orders = new ArrayList<Order>();
        driverService = ds;
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

    public List<Order> getUncompletedOrdersByUserId(UUID userId){
        return orders.stream()
                .filter(o ->
                        o.getUserId() == userId &&
                                (o.getStatus() == OrderStatus.Placed ||o.getStatus() == OrderStatus.Delivering))
                .collect(Collectors.toList());
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

    public void assignDriverToOrder(UUID orderId, UUID driverId) {
        var orderOpt = this.getOrderById(orderId);
        orderOpt.ifPresentOrElse(
                order -> {
                    order.setDriverId(driverId);
                    order.setStatus(OrderStatus.Delivering);
                },
                () -> System.out.println("Order not found!")
        );
    }

    public void completeOrder(UUID orderId) {
        var orderOpt = this.getOrderById(orderId);
        orderOpt.ifPresentOrElse(
                order -> {
                    order.setStatus(OrderStatus.Completed);
                    driverService.increaseCompletedDeliveries(order.getDriverId());
                },
                () -> System.out.println("Order not found!")
        );
    }
}
