
 import model.*;
import service.DriverService;
import service.OrderService;
import service.RestaurantService;
import service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static final DriverService ds = new DriverService();
    public static final RestaurantService rs = new RestaurantService();
    public static final OrderService os = new OrderService(ds);
    public static final UserService us = new UserService(os);

    public static void main(String[] args) {

//     =====================================================================================================

        Restaurant r1 = new Restaurant(
                "Fior di Latte",
                new Address("Bucharest", 19, "Bld Primaverii"),
                "Italian Restaurant - Chef Patrizia Paglieri - fine dining and bistro premium restaurant"
        );
        Product p1 = new Product(
                "Toast Premium",
                38,
                new ArrayList<String>(List.of("Avocado", "Salmon", "Scrambled eggs"))
        );
        rs.addRestaurant(r1);
        rs.addProductToRestaurant(p1, r1.getRestaurantId());


        var driver1 = new Driver("Tudor", "Sorin", 44, Transport.Car);
        ds.addDriver(driver1);


        User u1 = new User("Andreea", "Gavrila", 21, new Address("Bucharest", 601, "Unirii"));
        us.addUser(u1);

        var review1 = new Review(u1.getUserId(), r1.getRestaurantId(), 4, "Excellent!");
        rs.addReviewToRestaurant(review1);


        us.addProductToUserCart(p1, u1.getUserId());

        us.createOrder(u1.getUserId());  // PLACED order


        var orders1 = os.getUncompletedOrdersByUserId(u1.getUserId());

        os.assignDriverToOrder(orders1.get(0).getOrderId(), ds.getFirstAvailableDriver());  // DELIVERING order


        os.completeOrder(orders1.get(0).getOrderId());  // COMPLETED order


        System.out.println("");
        System.out.println("");

//     =====================================================================================================

        User u2 = new User("Sofia", "Benji", 22, new Address("Bucharest", 12, "Pipera"));
        us.addUser(u2);
        us.addProductToUserCart(p1, u2.getUserId());

        us.createOrder(u2.getUserId());   // PLACED order

        var orders2 = os.getUncompletedOrdersByUserId(u2.getUserId());

        os.assignDriverToOrder(orders2.get(0).getOrderId(), ds.getFirstAvailableDriver());  // DELIVERING order

//     =====================================================================================================

        Scanner scan = new Scanner(System.in);

        int opt = -1;
        while(opt != 0)   {
            System.out.println("Choose from the options:\n");
            System.out.println("0.Exit");
            System.out.println("1.Register a new user");

            System.out.println("2.Show all available restaurants");
            System.out.println("3.Show the menu for a restaurant");

            System.out.println("4.Add a product to a restaurant's menu");

            System.out.println("5.Add a review for a restaurant");
            System.out.println("6.Show all the reviews");

            System.out.println("7.Add a product to your shop-cart");
            System.out.println("8.Show your shop-cart");

            System.out.println("9.Create an order");
            System.out.println("10.Deliver an order");

            System.out.println("11.Show order Status");

            System.out.println("12.Complete an order - only for placed or delivering order");
            System.out.println("13.Cancel an order - only for placed or delivering order");

            System.out.println("14.Show all the orders");

            System.out.println("15.Add a new driver");
            System.out.println("16.Show all the drivers");

            System.out.println("");

            opt = scan.nextInt();
            System.out.println("");

            switch (opt){
                case 1:{
                    User u3 = new User("Criss", "Ann", 21, new Address("Bucharest", 2, "Romana"));
                    us.addUser(u3);
                    System.out.println("Successfully added user: " + u3.getFirstName() + " "+ u3.getLastName());
                    break;
                }
                case 2:{
                    rs.showRestaurants();
                    break;
                }
                case 3:{
                    rs.showMenu(r1.getRestaurantId());
                    break;
                }
                case 4:{
                    Product p2 = new Product(
                            "Spring Pancakes",
                            24,
                            new ArrayList<String>(List.of("Chocolate Cream", "Fresh Fruit")));

                    rs.addProductToRestaurant(p2, r1.getRestaurantId());
                    System.out.println("Successfully added product: " + p2.getName());
                    break;
                }
                case 5:{
                    var rev2 = new Review(u1.getUserId(), r1.getRestaurantId(), 5, "Excellent eating experience");
                    rs.addReviewToRestaurant(rev2);
                    System.out.println("Successfully added review: " + rev2.getContent() + " - " + rev2.getStars()
                            + " starts for Restaurant: " + r1.getName());
                    break;
                }
                case 6:{
                    System.out.println("Reviews for restaurant: " );
                    rs.showRestaurantReviews(r1.getRestaurantId());
                    break;
                }
                case 7:{
                    Product p2 = new Product(
                            "Spring Pancakes",
                            24,
                            new ArrayList<String>(List.of("Chocolate Cream", "Fresh Fruit")));

                    us.addProductToUserCart(p2, u2.getUserId());
                    System.out.println("Successfully added product: " + p2.getName() + " for user " + u2.getFirstName() + " " + u2.getLastName() );
                    us.showCart(u2.getUserId());
                    break;
                }
                case 8:{
                    us.showCart(u2.getUserId());
                    break;
                }
                case 9:{
                    us.createOrder(u2.getUserId());
                    System.out.println("Successfully placed order ");
                    os.showUserOrderHistory(u2.getUserId());
                    break;
                }
                case 10: {
                    var orders3 = os.getUncompletedOrdersByUserId(u2.getUserId());
                    os.assignDriverToOrder(orders3.get(1).getOrderId(), ds.getFirstAvailableDriver());

                    System.out.println("Successfully delivered order ");
                    os.showUserOrderHistory(u2.getUserId());

                    break;
                }
                case 11:{
                    os.showUserOrderHistory(u2.getUserId());
                    break;
                }
                case 12:{
                    var orders3 = os.getUncompletedOrdersByUserId(u2.getUserId());

                    os.completeOrder(orders3.get(1).getOrderId());  // Works only for placed / delivering order
                    System.out.println("Successfully completed order ");
                    os.showUserOrderHistory(u2.getUserId());
                    break;
                }
                case 13:{
                    var orders3 = os.getUncompletedOrdersByUserId(u2.getUserId());

                    os.cancelOrder(orders3.get(0).getOrderId());  // Works only for placed / delivering order
                    System.out.println("Successfully canceled order ");
                    os.showUserOrderHistory(u2.getUserId());
                    break;
                }
                case 14:{
                    os.showOrders();
                    break;
                }
                case 15:{
                    var driver2 = new Driver("Paul", "Ton", 45, Transport.Car);
                    ds.addDriver(driver2);
                    System.out.println("Successfully added driver: " + driver2.getFirstName() + " " + driver2.getLastName() );
                    break;
                }
                case 16:{
                    ds.showDrivers();
                    break;
                }
                case 0:{
                    System.out.println("Thank you for using our app! :) ");
                    break;
                }
            }
            System.out.println("");
        }
    }
}
