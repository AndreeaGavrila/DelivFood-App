import model.*;
import service.DriverService;
import service.OrderService;
import service.RestaurantService;
import service.UserService;
import java.util.ArrayList;
import java.util.List;

public class Test {

//    public static final DriverService ds = new DriverService();
//    public static final RestaurantService rs = new RestaurantService();
//    public static final OrderService os = new OrderService(ds);
//    public static final UserService us = new UserService(os);
//
//    public static void main(String[] args) {
//
////     =====================================================================================================
//
//        Restaurant r1 = new Restaurant(
//                "Fior di Latte",
//                new Address("Bucharest", 19, "Bld Primaverii"),
//                "Italian Restaurant - Chef Patrizia Paglieri - fine dining and bistro premium restaurant"
//        );
//        Product p1 = new Product(
//                "Toast Premium",
//                38,
//                new ArrayList<String>(List.of("Avocado", "Salmon", "Scrambled eggs"))
//        );
//        rs.addRestaurant(r1);
//        rs.showRestaurants();
//        System.out.println("");
//
//        rs.addProductToRestaurant(p1, r1.getRestaurantId());
//        rs.showMenu(r1.getRestaurantId());
//        System.out.println("");
//
//        User u1 = new User("Andreea", "Gavrila", 21, new Address("Bucharest", 601, "Unirii"));
//        us.addUser(u1);
//
//        us.addProductToUserCart(p1, u1.getUserId());
//        us.showCart(u1.getUserId());
//        System.out.println("");
//
//        us.createOrder(u1.getUserId());
//        System.out.println("Successfully placed order ");
//        os.showUserOrderHistory(u1.getUserId());   // PLACED
//        System.out.println("");
//
//        var driver1 = new Driver("Tudor", "Sorin", 44, Transport.Car);
//        ds.addDriver(driver1);
//        ds.showDrivers();
//        System.out.println("");
//
//        var orders1 = os.getUncompletedOrdersByUserId(u1.getUserId());
//
//        os.assignDriverToOrder(orders1.get(0).getOrderId(), ds.getFirstAvailableDriver());
//        System.out.println("Successfully delivered order ");
//        os.showUserOrderHistory(u1.getUserId());    // DELIVERING
//
//        os.completeOrder(orders1.get(0).getOrderId());
//        System.out.println("Successfully completed order ");
//        os.showUserOrderHistory(u1.getUserId());    // COMPLETED
//
//
//        var review1 = new Review(u1.getUserId(), r1.getRestaurantId(), 4, "Excellent!");
//        rs.addReviewToRestaurant(review1);
//        rs.showRestaurantReviews(r1.getRestaurantId());
//
//        System.out.println("");
//        System.out.println("");
//
////     =====================================================================================================
//
//        User u2 = new User("Sofia", "Benji", 22, new Address("Bucharest", 12, "Pipera"));
//        us.addUser(u2);
//
//        Product p2 = new Product(
//                "Spring Pancakes",
//                24,
//                new ArrayList<String>(List.of("Chocolate Cream", "Fresh Fruit")));
//
//        us.addProductToUserCart(p2, u2.getUserId());
//
//        us.createOrder(u2.getUserId());
//        System.out.println("Successfully placed order ");
//        os.showUserOrderHistory(u2.getUserId());   // PLACED
//
//        var orders2 = os.getUncompletedOrdersByUserId(u2.getUserId());
//
//        os.assignDriverToOrder(orders2.get(0).getOrderId(), ds.getFirstAvailableDriver());
//        System.out.println("Successfully delivered order ");
//        os.showUserOrderHistory(u2.getUserId());   // DELIVERING
//
//
//        os.cancelOrder(orders2.get(0).getOrderId());
//        System.out.println("Successfully canceled order ");
//        os.showUserOrderHistory(u2.getUserId());   // CANCELED
//
//        os.showOrders();  // ALL the Orders
//
//        System.out.println("");
//        System.out.println("Thank you for using our app! :) ");
//    }
}
