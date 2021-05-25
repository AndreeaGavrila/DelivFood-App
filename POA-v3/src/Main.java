import model.Driver;
import model.*;
import service.*;
import utils.*;

import java.util.*;
import java.util.stream.Stream;
import java.sql.*;


public class Main {

    public static final AddressService as = AddressService.getInstance();
    public static final DriverService ds = DriverService.getInstance();
    public static final OrderService os = OrderService.getInstance();
    public static final ProductService ps = ProductService.getInstance();
    public static final RestaurantService rstS = RestaurantService.getInstance();
    public static final ReviewService revS = ReviewService.getInstance();
    public static final UserService us = UserService.getInstance();

    private static final Scanner scanner = new Scanner(System.in);
    private static final Logger logger = new Logger("logs.csv");
    private static final MenuGeneric genericMenu = new MenuGeneric();

    //     =====================================================================================================
    //     =====================================================================================================

    public static void show_CSV_Menu() {

        genericMenu.addMenuItem("L", "Load data", () -> {

            us.readAll("users.csv");
            ds.readAll("drivers.csv");

            ps.readAll("products.csv");
            os.readAll("orders.csv");

            rstS.readAll("restaurants.csv");
            revS.readAll("reviews.csv");

            logger.write("Loaded data");
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("S", "Save changes", () -> {

            us.saveAll("users.csv");
            ds.saveAll("drivers.csv");

            ps.saveAll("products.csv");
            os.saveAll("orders.csv");

            rstS.saveAll("restaurants.csv");
            revS.saveAll("reviews.csv");

            logger.write("Saved data");
        });

        //     =====================================================================================================
        //     =====================================================================================================

        genericMenu.addMenuItem("add-ur", "Add a new user", () -> {

            System.out.print("First name: ");
            var firstName = scanner.nextLine();

            System.out.print("Last name: ");
            var lastName = scanner.nextLine();

            System.out.print("Age: ");
            var age = scanner.nextInt();

            scanner.nextLine();

            System.out.print("Street: ");
            var street = scanner.nextLine();

            System.out.print("Number: ");
            var number = scanner.nextInt();

            scanner.nextLine();

            System.out.print("City: ");
            var city = scanner.nextLine();


            us.addUser(new User(firstName, lastName, age, new Address(city, number, street)));

            logger.write("Added user");
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("list-ur", "List users", () -> {

            us.listUsers();
            logger.write("Listed users");
        });

        //     =====================================================================================================
        //     =====================================================================================================

        genericMenu.addMenuItem("add-res", "Add a new restaurant", () -> {

            System.out.print("Name: ");
            var name = scanner.nextLine();

            System.out.print("Description: ");
            var desc = scanner.nextLine();


            System.out.print("Street: ");
            var street = scanner.nextLine();

            System.out.print("Number: ");
            var number = scanner.nextInt();

            scanner.nextLine();

            System.out.print("City: ");
            var city = scanner.nextLine();


            rstS.addRestaurant(new Restaurant(name, desc, new Address(city, number, street)));

            logger.write("Added restaurant");
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("list-res", "List restaurants", () -> {

            rstS.showRestaurants();

            logger.write("Retrieved restaurants");
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("add-pr", "Add a new product", () -> {

            System.out.println("Which restaurant does the product belong to?");

            var restLst = rstS.overviewRestaurants();

            UUID restId = null;

            for (int i = 0; i < restLst.size(); i++) {
                System.out.println((i+1) + ") " + restLst.get(i));
            }

            int restChoice = scanner.nextInt();
            scanner.nextLine();

            while(restChoice <= 0 || restChoice > restLst.size()){
                restChoice = scanner.nextInt();
                scanner.nextLine();
            }

//            restId = rstS.getRestaurantIdFromName(restLst.get(restChoice - 1)).get();
            restId = restLst.get(restChoice - 1).second;

            System.out.print("Product name: ");
            var productName = scanner.nextLine();

            System.out.print("Price: ");
            var price = scanner.nextFloat();

            scanner.nextLine();
            System.out.print("Ingredients (separated by ',' ): ");

            var ingredients = scanner.nextLine().split(",");
            var lst = Stream.of(ingredients)
                    .map(i -> i.strip())
                    .toArray(String[]::new);

            ps.addProduct(new Product(restId, productName, price, lst));

            logger.write("Added product");
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("list-pr", "List products", () -> {

            ps.listProducts();

            logger.write("Retrieved products");
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("show-menu", "Show the menu of a restaurant", () -> {

            System.out.println("Restaurant:");

            var restLst = rstS.overviewRestaurants();

            UUID restId = null;

            for (int i = 0; i < restLst.size(); i++) {
                System.out.println((i+1) + ") " + restLst.get(i));
            }

            int restChoice = scanner.nextInt();
            scanner.nextLine();

            while(restChoice <= 0 || restChoice > restLst.size()){
                restChoice = scanner.nextInt();
                scanner.nextLine();
            }

//            restId = rstS.getRestaurantIdFromName(restLst.get(restChoice - 1)).get();
            restId = restLst.get(restChoice - 1).second;

            try {
                rstS.showMenu(restId);
            }
            catch(Exception e) {
                System.out.println(e);
            }

            logger.write("Retrieved restaurant menu");
        });

        //     =====================================================================================================
        //     =====================================================================================================

        genericMenu.addMenuItem("add-rw", "Add a review to a restaurant", () -> {

            System.out.println("Which user does the review belong to?");

            var usrLst = us.overviewUsers();

            if(usrLst == null) return;

            UUID userId = null;

            for (int i = 0; i < usrLst.size(); i++) {
                System.out.println((i+1) + ") " + usrLst.get(i).second);
            }

            int usrChoice = scanner.nextInt();
            scanner.nextLine();

            while(usrChoice <= 0 || usrChoice > usrLst.size()){
                usrChoice = scanner.nextInt();
                scanner.nextLine();
            }

            userId = usrLst.get(usrChoice - 1).first;

            System.out.println("Which restaurant does the review belong to?");

            var restLst = rstS.overviewRestaurants();

            UUID restId = null;

            for (int i = 0; i < restLst.size(); i++) {
                System.out.println((i+1) + ") " + restLst.get(i));
            }

            int restChoice = scanner.nextInt();
            scanner.nextLine();

            while(restChoice <= 0 || restChoice > restLst.size()){
                restChoice = scanner.nextInt();
                scanner.nextLine();
            }

//            restId = rstS.getRestaurantIdFromName(restLst.get(restChoice - 1)).get();
            restId = restLst.get(restChoice - 1).second;

            System.out.print("Stars (1-5): ");
            int stars = scanner.nextInt();

            scanner.nextLine();

            while(stars < 1 || stars > 5){
                stars = scanner.nextInt();
                scanner.nextLine();
            }

            System.out.println("Content (no ',' just one line in a row):");

            String content = scanner.nextLine();

            revS.addReview(new Review(userId, restId, stars, content));

            logger.write("Added review to restaurant");
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("list-rw", "Show the reviews of a restaurant", () -> {

            System.out.println("Restaurant:");

            var restLst = rstS.overviewRestaurants();

            UUID restId = null;

            for (int i = 0; i < restLst.size(); i++) {
                System.out.println((i+1) + ") " + restLst.get(i));
            }

            int restChoice = scanner.nextInt();
            scanner.nextLine();

            while(restChoice <= 0 || restChoice > restLst.size()){
                restChoice = scanner.nextInt();
                scanner.nextLine();
            }

//            restId = rstS.getRestaurantIdFromName(restLst.get(restChoice - 1)).get();
            restId = restLst.get(restChoice - 1).second;

            try {
                rstS.showRestaurantReviews(restId);
            }
            catch(Exception e) {
                System.out.println(e);
            }

            logger.write("Retrieved restaurant reviews");
        });

        //     =====================================================================================================
        //     =====================================================================================================

        genericMenu.addMenuItem("add-prod-to-ur", "Add a product to a user's cart", () -> {

            System.out.println("Which user does the cart belong to?");

            var usrLst = us.overviewUsers();
            if(usrLst == null) return;

            UUID userId = null;
            for (int i = 0; i < usrLst.size(); i++) {
                System.out.println((i+1) + ") " + usrLst.get(i).second);
            }

            int usrChoice = scanner.nextInt();
            scanner.nextLine();

            while(usrChoice <= 0 || usrChoice > usrLst.size()){
                usrChoice = scanner.nextInt();
                scanner.nextLine();
            }

            userId = usrLst.get(usrChoice - 1).first;

            System.out.println("Add products to the user's cart (write -1 to finish adding)");

            var prodLst = ps.overviewProducts();

            if(prodLst == null) return;

            while(true) {
                UUID prodId = null;

                for (int i = 0; i < prodLst.size(); i++) {
                    System.out.println((i+1) + ") " + prodLst.get(i).second);
                }

                int restChoice = scanner.nextInt();
                scanner.nextLine();

                if(restChoice == -1){
                    logger.write("Added products to user");
                    return;
                }

                while(restChoice <= 0 || restChoice > prodLst.size()){
                    restChoice = scanner.nextInt();
                    scanner.nextLine();
                }

                prodId = prodLst.get(restChoice - 1).first;

                try{
                    us.addProductToUserCart(prodId, userId);
                }
                catch(Exception e) {
                    System.out.println(e);
                }
            }
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("show-ur-cart", "Show a user's cart", () -> {

            System.out.println("Which user does the cart belong to?");

            var usrLst = us.overviewUsers();

            if(usrLst == null) return;

            UUID userId = null;

            for (int i = 0; i < usrLst.size(); i++) {
                System.out.println((i+1) + ") " + usrLst.get(i).second);
            }

            int restChoice = scanner.nextInt();
            scanner.nextLine();

            while(restChoice <= 0 || restChoice > usrLst.size()){
                restChoice = scanner.nextInt();
                scanner.nextLine();
            }

            userId = usrLst.get(restChoice - 1).first;

            try{
                us.showCart(userId);
            }
            catch(Exception e) {
                System.out.println(e);
            }

            logger.write("Retrieved user cart");
        });

        //     =====================================================================================================
        //     =====================================================================================================

        genericMenu.addMenuItem("list-ord", "List orders", () -> {

            os.showOrders();
            logger.write("Retrieved orders");
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("create-ord", "Create an order", () -> {

            System.out.println("Which user does the order belong to?");

            var usrLst = us.overviewUsers();

            if(usrLst == null) return;

            UUID userId = null;

            for (int i = 0; i < usrLst.size(); i++) {
                System.out.println((i+1) + ") " + usrLst.get(i).second);
            }

            int restChoice = scanner.nextInt();
            scanner.nextLine();

            while(restChoice <= 0 || restChoice > usrLst.size()){
                restChoice = scanner.nextInt();
                scanner.nextLine();
            }

            userId = usrLst.get(restChoice - 1).first;

            try{
                us.createOrder(userId);
            }
            catch(Exception e) {
                System.out.println(e);
            }

            logger.write("Created order");
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("cancel-ord", "Cancel an order", () -> {

            System.out.println("Which user does the order belong to?");

            var usrLst = us.overviewUsers();

            if(usrLst == null) return;

            UUID userId = null;

            for (int i = 0; i < usrLst.size(); i++) {
                System.out.println((i+1) + ") " + usrLst.get(i).second);
            }

            int restChoice = scanner.nextInt();
            scanner.nextLine();

            while(restChoice <= 0 || restChoice > usrLst.size()){
                restChoice = scanner.nextInt();
                scanner.nextLine();
            }

            userId = usrLst.get(restChoice - 1).first;

            System.out.println("Which order do you want to cancel?");

            var ordLst = os.getUncompletedOrdersByUserId(userId);

            if(ordLst == null) return;

            UUID ordId = null;

            for (int i = 0; i < ordLst.size(); i++) {
                System.out.println((i+1) + ") " + ordLst.get(i).second);
            }

            int ordChoice = scanner.nextInt();
            scanner.nextLine();

            while(ordChoice <= 0 || ordChoice > ordLst.size()){
                ordChoice = scanner.nextInt();
                scanner.nextLine();
            }

            ordId = ordLst.get(ordChoice - 1).first;
            os.cancelOrder(ordId);

            logger.write("Cancel order");
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("ord-status", "Show the status of a certain order", () -> {

            System.out.println("Which user does the order belong to?");

            var usrLst = us.overviewUsers();

            if(usrLst == null) return;

            UUID userId = null;

            for (int i = 0; i < usrLst.size(); i++) {
                System.out.println((i+1) + ") " + usrLst.get(i).second);
            }

            int usrChoice = scanner.nextInt();
            scanner.nextLine();

            while(usrChoice <= 0 || usrChoice > usrLst.size()){
                usrChoice = scanner.nextInt();
                scanner.nextLine();
            }

            userId = usrLst.get(usrChoice - 1).first;

            System.out.println("Which order do you want to get the status of?");

            var ordLst = os.getUncompletedOrdersByUserId(userId);
            if(ordLst == null) return;

            UUID ordId = null;

            for (int i = 0; i < ordLst.size(); i++) {
                System.out.println((i+1) + ") " + ordLst.get(i).second);
            }

            int ordChoice = scanner.nextInt();
            scanner.nextLine();

            while(ordChoice <= 0 || ordChoice > ordLst.size()){
                ordChoice = scanner.nextInt();
                scanner.nextLine();
            }

            ordId = ordLst.get(ordChoice - 1).first;
            os.getOrderStatus(ordId);

            logger.write("Retrieved order status");
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("complete-ord", "Complete a certain order", () -> {

            System.out.println("Which user does the order belong to?");

            var usrLst = us.overviewUsers();

            if(usrLst == null) return;

            UUID userId = null;

            for (int i = 0; i < usrLst.size(); i++) {
                System.out.println((i+1) + ") " + usrLst.get(i).second);
            }

            int usrChoice = scanner.nextInt();
            scanner.nextLine();

            while(usrChoice <= 0 || usrChoice > usrLst.size()){
                usrChoice = scanner.nextInt();
                scanner.nextLine();
            }

            userId = usrLst.get(usrChoice - 1).first;

            System.out.println("Which order do you want to complete?");

            var ordLst = os.getUncompletedOrdersByUserId(userId);

            if(ordLst == null) return;

            UUID ordId = null;

            for (int i = 0; i < ordLst.size(); i++) {
                System.out.println((i+1) + ") " + ordLst.get(i).second);
            }

            int ordChoice = scanner.nextInt();
            scanner.nextLine();

            while(ordChoice <= 0 || ordChoice > ordLst.size()){
                ordChoice = scanner.nextInt();
                scanner.nextLine();
            }

            ordId = ordLst.get(ordChoice - 1).first;

            try {
                os.completeOrder(ordId);
            }
            catch(Exception e) {
                System.out.println(e);
            }

            logger.write("Completed order");
        });

        //     =====================================================================================================
        //     =====================================================================================================

        genericMenu.addMenuItem("add-dr", "Add a new driver", () -> {

            System.out.print("First name: ");
            var firstName = scanner.nextLine();

            System.out.print("Last name: ");
            var lastName = scanner.nextLine();

            System.out.print("Age: ");
            var age = scanner.nextInt();

            scanner.nextLine();
            System.out.print("Transport " + Arrays.toString(Transport.values()) + ": ");

            var transport = scanner.nextLine();

            Transport enumTransport = null;
            while(enumTransport == null) {
                try{
                    enumTransport = Transport.valueOf(transport);
                    break;
                }
                catch(IllegalArgumentException ex) {
                    System.out.print("Transport " + Arrays.toString(Transport.values()) + ": ");
                    transport = scanner.nextLine();
                }
            }

            ds.addDriver(new Driver(firstName, lastName, age, enumTransport));

            logger.write("Added driver");
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("list-dr", "List drivers", () -> {

            ds.listDrivers();

            logger.write("Retrieved drivers");
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("assign-dr-ord", "Assign a driver to an order", () -> {

            System.out.println("Which user does the order belong to?");

            var usrLst = us.overviewUsers();

            if(usrLst == null) return;

            UUID userId = null;

            for (int i = 0; i < usrLst.size(); i++) {
                System.out.println((i+1) + ") " + usrLst.get(i).second);
            }

            int usrChoice = scanner.nextInt();
            scanner.nextLine();

            while(usrChoice <= 0 || usrChoice > usrLst.size()){
                usrChoice = scanner.nextInt();
                scanner.nextLine();
            }

            userId = usrLst.get(usrChoice - 1).first;

            System.out.println("Which order do you want to assign a driver to?");

            var ordLst = os.getUncompletedOrdersByUserId(userId);

            if(ordLst == null) return;

            UUID ordId = null;

            for (int i = 0; i < ordLst.size(); i++) {
                System.out.println((i+1) + ") " + ordLst.get(i).second);
            }

            int ordChoice = scanner.nextInt();
            scanner.nextLine();

            while(ordChoice <= 0 || ordChoice > ordLst.size()){
                ordChoice = scanner.nextInt();
                scanner.nextLine();
            }

            ordId = ordLst.get(ordChoice - 1).first;

            try {
                os.assignDriverToOrder(ordId, ds.getFirstAvailableDriver());
            }
            catch(Exception e) {
                System.out.println(e);
            }

            logger.write("Assigned driver to order");
        });

        //     =====================================================================================================

        genericMenu.initMenu();
    }

    //     =====================================================================================================
    //     =====================================================================================================

    public static void show_Menu() {

        //     =====================================================================================================

        genericMenu.addMenuItem("add-ur", "Add a new user", () -> {

            System.out.print("First name: ");
            var firstName = scanner.nextLine();

            System.out.print("Last name: ");
            var lastName = scanner.nextLine();

            System.out.print("Age: ");
            var age = scanner.nextInt();

            scanner.nextLine();

            System.out.print("Street: ");
            var street = scanner.nextLine();

            System.out.print("Number: ");
            var number = scanner.nextInt();

            scanner.nextLine();

            System.out.print("City: ");
            var city = scanner.nextLine();

            us.insert(new User(firstName, lastName, age, new Address(city, number, street)));

            logger.write("Added user");
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("list-ur", "List users", () -> {

            us.listUsers();
            logger.write("Listed users");
        });

        //     =====================================================================================================
        //     =====================================================================================================

        genericMenu.addMenuItem("add-res", "Add a new restaurant", () -> {

            System.out.print("Name: ");
            var name = scanner.nextLine();

            System.out.print("Description: ");
            var desc = scanner.nextLine();


            System.out.print("Street: ");
            var street = scanner.nextLine();

            System.out.print("Number: ");
            var number = scanner.nextInt();

            scanner.nextLine();

            System.out.print("City: ");
            var city = scanner.nextLine();

            rstS.insert(new Restaurant(name, desc, new Address(city, number, street)));

            logger.write("Added restaurant");
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("list-res", "List restaurants", () -> {

            rstS.showRestaurants();
            logger.write("Retrieved restaurants");
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("add-pr", "Add a new product", () -> {

            System.out.println("Which restaurant does the product belong to?");

            var restLst = rstS.overviewRestaurants();

            UUID restId = null;

            for (int i = 0; i < restLst.size(); i++) {
                System.out.println((i+1) + ") " + restLst.get(i));
            }

            int restChoice = scanner.nextInt();
            scanner.nextLine();

            while(restChoice <= 0 || restChoice > restLst.size()){
                restChoice = scanner.nextInt();
                scanner.nextLine();
            }

//            restId = rstS.getRestaurantIdFromName(restLst.get(restChoice - 1)).get();
            restId = restLst.get(restChoice - 1).second;

            System.out.print("Product name: ");
            var productName = scanner.nextLine();

            System.out.print("Price: ");
            var price = scanner.nextFloat();

            scanner.nextLine();
            System.out.print("Ingredients (separated by ',' ): ");

            var ingredients = scanner.nextLine().split(",");
            var lst = Stream.of(ingredients)
                    .map(i -> i.strip())
                    .toArray(String[]::new);

            ps.insert(new Product(restId, productName, price, lst));

            logger.write("Added product");
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("list-pr", "List products", () -> {

            ps.listProducts();
            logger.write("Retrieved products");
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("show-menu", "Show the menu of a restaurant", () -> {

            System.out.println("Restaurant:");

            var restLst = rstS.overviewRestaurants();

            UUID restId = null;

            for (int i = 0; i < restLst.size(); i++) {
                System.out.println((i+1) + ") " + restLst.get(i));
            }

            int restChoice = scanner.nextInt();
            scanner.nextLine();

            while(restChoice <= 0 || restChoice > restLst.size()){
                restChoice = scanner.nextInt();
                scanner.nextLine();
            }

//            restId = rstS.getRestaurantIdFromName(restLst.get(restChoice - 1)).get();
            restId = restLst.get(restChoice - 1).second;

            try {
                rstS.showMenu(restId);
            }
            catch(Exception e) {
                System.out.println(e);
            }

            logger.write("Retrieved restaurant menu");
        });

        //     =====================================================================================================
        //     =====================================================================================================

        genericMenu.addMenuItem("add-rw", "Add a review to a restaurant", () -> {

            System.out.println("Which user does the review belong to?");

            var usrLst = us.overviewUsers();

            if(usrLst == null) return;

            UUID userId = null;

            for (int i = 0; i < usrLst.size(); i++) {
                System.out.println((i+1) + ") " + usrLst.get(i).second);
            }

            int usrChoice = scanner.nextInt();
            scanner.nextLine();

            while(usrChoice <= 0 || usrChoice > usrLst.size()){
                usrChoice = scanner.nextInt();
                scanner.nextLine();
            }

            userId = usrLst.get(usrChoice - 1).first;

            System.out.println("Which restaurant does the review belong to?");

            var restLst = rstS.overviewRestaurants();

            UUID restId = null;

            for (int i = 0; i < restLst.size(); i++) {
                System.out.println((i+1) + ") " + restLst.get(i));
            }

            int restChoice = scanner.nextInt();
            scanner.nextLine();

            while(restChoice <= 0 || restChoice > restLst.size()){
                restChoice = scanner.nextInt();
                scanner.nextLine();
            }

//            restId = rstS.getRestaurantIdFromName(restLst.get(restChoice - 1)).get();
            restId = restLst.get(restChoice - 1).second;

            System.out.print("Stars (1-5): ");
            int stars = scanner.nextInt();

            scanner.nextLine();

            while(stars < 1 || stars > 5){
                stars = scanner.nextInt();
                scanner.nextLine();
            }

            System.out.println("Content (no ',' just one line in a row):");

            String content = scanner.nextLine();

            revS.insert(new Review(userId, restId, stars, content));

            logger.write("Added review to restaurant");
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("list-rw", "Show the reviews of a restaurant", () -> {

            System.out.println("Restaurant:");

            var restLst = rstS.overviewRestaurants();

            UUID restId = null;

            for (int i = 0; i < restLst.size(); i++) {
                System.out.println((i+1) + ") " + restLst.get(i));
            }

            int restChoice = scanner.nextInt();
            scanner.nextLine();

            while(restChoice <= 0 || restChoice > restLst.size()){
                restChoice = scanner.nextInt();
                scanner.nextLine();
            }

//            restId = rstS.getRestaurantIdFromName(restLst.get(restChoice - 1)).get();
            restId = restLst.get(restChoice - 1).second;

            try {
                rstS.showRestaurantReviews(restId);
            }
            catch(Exception e) {
                System.out.println(e);
            }

            logger.write("Retrieved restaurant reviews");
        });

        //     =====================================================================================================
        //     =====================================================================================================

        genericMenu.addMenuItem("add-prod-to-ur", "Add a product to a user's cart", () -> {

            System.out.println("Which user does the cart belong to?");

            var usrLst = us.overviewUsers();

            if(usrLst == null) return;

            UUID userId = null;

            for (int i = 0; i < usrLst.size(); i++) {
                System.out.println((i+1) + ") " + usrLst.get(i).second);
            }

            int usrChoice = scanner.nextInt();
            scanner.nextLine();

            while(usrChoice <= 0 || usrChoice > usrLst.size()){
                usrChoice = scanner.nextInt();
                scanner.nextLine();
            }

            userId = usrLst.get(usrChoice - 1).first;

            System.out.println("Add products to the user's cart (write -1 to finish adding)");

            var prodLst = ps.overviewProducts();

            if(prodLst == null) return;

            while(true) {
                UUID prodId = null;

                for (int i = 0; i < prodLst.size(); i++) {
                    System.out.println((i+1) + ") " + prodLst.get(i).second);
                }

                int restChoice = scanner.nextInt();
                scanner.nextLine();

                if(restChoice == -1){
                    logger.write("Added products to user");
                    return;
                }

                while(restChoice <= 0 || restChoice > prodLst.size()){
                    restChoice = scanner.nextInt();
                    scanner.nextLine();
                }

                prodId = prodLst.get(restChoice - 1).first;

                try{
                    us.addProductToUserCart(prodId, userId);
                }
                catch(Exception e) {
                    System.out.println(e);
                }
            }
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("show-ur-cart", "Show a user's cart", () -> {

            System.out.println("Which user does the cart belong to?");

            var usrLst = us.overviewUsers();

            if(usrLst == null) return;

            UUID userId = null;

            for (int i = 0; i < usrLst.size(); i++) {
                System.out.println((i+1) + ") " + usrLst.get(i).second);
            }

            int restChoice = scanner.nextInt();
            scanner.nextLine();

            while(restChoice <= 0 || restChoice > usrLst.size()){
                restChoice = scanner.nextInt();
                scanner.nextLine();
            }

            userId = usrLst.get(restChoice - 1).first;

            try{
                us.showCart(userId);
            }
            catch(Exception e) {
                System.out.println(e);
            }

            logger.write("Retrieved user cart");
        });

        //     =====================================================================================================
        //     =====================================================================================================

        genericMenu.addMenuItem("list-ord", "List orders", () -> {

            os.showOrders();
            logger.write("Retrieved orders");
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("create-ord", "Create an order", () -> {

            System.out.println("Which user does the order belong to?");

            var usrLst = us.overviewUsers();

            if(usrLst == null) return;

            UUID userId = null;

            for (int i = 0; i < usrLst.size(); i++) {
                System.out.println((i+1) + ") " + usrLst.get(i).second);
            }

            int restChoice = scanner.nextInt();
            scanner.nextLine();

            while(restChoice <= 0 || restChoice > usrLst.size()){
                restChoice = scanner.nextInt();
                scanner.nextLine();
            }

            userId = usrLst.get(restChoice - 1).first;

            try{
                us.createOrder(userId);
            }
            catch(Exception e) {
                System.out.println(e);
            }
            logger.write("Created order");
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("cancel-ord", "Cancel an order", () -> {

            System.out.println("Which user does the order belong to?");

            var usrLst = us.overviewUsers();

            if(usrLst == null) return;

            UUID userId = null;

            for (int i = 0; i < usrLst.size(); i++) {
                System.out.println((i+1) + ") " + usrLst.get(i).second);
            }

            int restChoice = scanner.nextInt();
            scanner.nextLine();

            while(restChoice <= 0 || restChoice > usrLst.size()){
                restChoice = scanner.nextInt();
                scanner.nextLine();
            }

            userId = usrLst.get(restChoice - 1).first;

            System.out.println("Which order do you want to cancel?");

            var ordLst = os.getUncompletedOrdersByUserId(userId);

            if(ordLst == null) return;

            UUID ordId = null;

            for (int i = 0; i < ordLst.size(); i++) {
                System.out.println((i+1) + ") " + ordLst.get(i).second);
            }

            int ordChoice = scanner.nextInt();
            scanner.nextLine();

            while(ordChoice <= 0 || ordChoice > ordLst.size()){
                ordChoice = scanner.nextInt();
                scanner.nextLine();
            }

            ordId = ordLst.get(ordChoice - 1).first;
            os.cancelOrder(ordId);

            logger.write("Cancel order");
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("ord-status", "Show the status of a certain order", () -> {

            System.out.println("Which user does the order belong to?");

            var usrLst = us.overviewUsers();

            if(usrLst == null) return;

            UUID userId = null;

            for (int i = 0; i < usrLst.size(); i++) {
                System.out.println((i+1) + ") " + usrLst.get(i).second);
            }

            int usrChoice = scanner.nextInt();
            scanner.nextLine();

            while(usrChoice <= 0 || usrChoice > usrLst.size()){
                usrChoice = scanner.nextInt();
                scanner.nextLine();
            }

            userId = usrLst.get(usrChoice - 1).first;

            System.out.println("Which order do you want to get the status of?");

            var ordLst = os.getUncompletedOrdersByUserId(userId);
            if(ordLst == null) return;

            UUID ordId = null;

            for (int i = 0; i < ordLst.size(); i++) {
                System.out.println((i+1) + ") " + ordLst.get(i).second);
            }

            int ordChoice = scanner.nextInt();
            scanner.nextLine();

            while(ordChoice <= 0 || ordChoice > ordLst.size()){
                ordChoice = scanner.nextInt();
                scanner.nextLine();
            }

            ordId = ordLst.get(ordChoice - 1).first;
            os.getOrderStatus(ordId);

            logger.write("Retrieved order status");
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("complete-ord", "Complete a certain order", () -> {

            System.out.println("Which user does the order belong to?");

            var usrLst = us.overviewUsers();

            if(usrLst == null) return;

            UUID userId = null;

            for (int i = 0; i < usrLst.size(); i++) {
                System.out.println((i+1) + ") " + usrLst.get(i).second);
            }

            int usrChoice = scanner.nextInt();
            scanner.nextLine();

            while(usrChoice <= 0 || usrChoice > usrLst.size()){
                usrChoice = scanner.nextInt();
                scanner.nextLine();
            }

            userId = usrLst.get(usrChoice - 1).first;

            System.out.println("Which order do you want to complete?");

            var ordLst = os.getUncompletedOrdersByUserId(userId);

            if(ordLst == null) return;

            UUID ordId = null;

            for (int i = 0; i < ordLst.size(); i++) {
                System.out.println((i+1) + ") " + ordLst.get(i).second);
            }

            int ordChoice = scanner.nextInt();
            scanner.nextLine();

            while(ordChoice <= 0 || ordChoice > ordLst.size()){
                ordChoice = scanner.nextInt();
                scanner.nextLine();
            }

            ordId = ordLst.get(ordChoice - 1).first;

            try {
                os.completeOrder(ordId);
            }
            catch(Exception e) {
                System.out.println(e);
            }

            logger.write("Completed order");
        });

        //     =====================================================================================================
        //     =====================================================================================================

        genericMenu.addMenuItem("add-dr", "Add a new driver", () -> {

            System.out.print("First name: ");
            var firstName = scanner.nextLine();

            System.out.print("Last name: ");
            var lastName = scanner.nextLine();

            System.out.print("Age: ");
            var age = scanner.nextInt();

            scanner.nextLine();
            System.out.print("Transport " + Arrays.toString(Transport.values()) + ": ");

            var transport = scanner.nextLine();

            Transport enumTransport = null;
            while(enumTransport == null) {
                try{
                    enumTransport = Transport.valueOf(transport);
                    break;
                }
                catch(IllegalArgumentException ex) {
                    System.out.print("Transport " + Arrays.toString(Transport.values()) + ": ");
                    transport = scanner.nextLine();
                }
            }

            ds.insert(new Driver(firstName, lastName, age, enumTransport));

            logger.write("Added driver");
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("list-dr", "List drivers", () -> {

            ds.listDrivers();
            logger.write("Retrieved drivers");
        });

        //     =====================================================================================================

        genericMenu.addMenuItem("assign-dr-ord", "Assign a driver to an order", () -> {

            System.out.println("Which user does the order belong to?");

            var usrLst = us.overviewUsers();

            if(usrLst == null) return;

            UUID userId = null;

            for (int i = 0; i < usrLst.size(); i++) {
                System.out.println((i+1) + ") " + usrLst.get(i).second);
            }

            int usrChoice = scanner.nextInt();
            scanner.nextLine();

            while(usrChoice <= 0 || usrChoice > usrLst.size()){
                usrChoice = scanner.nextInt();
                scanner.nextLine();
            }

            userId = usrLst.get(usrChoice - 1).first;

            System.out.println("Which order do you want to assign a driver to?");

            var ordLst = os.getUncompletedOrdersByUserId(userId);

            if(ordLst == null) return;

            UUID ordId = null;

            for (int i = 0; i < ordLst.size(); i++) {
                System.out.println((i+1) + ") " + ordLst.get(i).second);
            }

            int ordChoice = scanner.nextInt();
            scanner.nextLine();

            while(ordChoice <= 0 || ordChoice > ordLst.size()){
                ordChoice = scanner.nextInt();
                scanner.nextLine();
            }

            ordId = ordLst.get(ordChoice - 1).first;

            try {
                os.assignDriverToOrder(ordId, ds.getFirstAvailableDriver());
            }
            catch(Exception e) {
                System.out.println(e);
            }

            logger.write("Assigned driver to order");
        });

        //     =====================================================================================================

        genericMenu.initMenu();
    }


    //     =====================================================================================================
    //     =====================================================================================================


    public static void main(String[] args) throws SQLException {

//        show_CSV_Menu();

        show_Menu();

//     =====================================================================================================

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
//        rs.addProductToRestaurant(p1, r1.getRestaurantId());
//
//
//        var driver1 = new Driver("Tudor", "Sorin", 44, Transport.Car);
//        ds.addDriver(driver1);
//
//
//        User u1 = new User("Andreea", "Gavrila", 21, new Address("Bucharest", 601, "Unirii"));
//        us.addUser(u1);
//
//        var review1 = new Review(u1.getUserId(), r1.getRestaurantId(), 4, "Excellent!");
//        rs.addReviewToRestaurant(review1);
//
//
//        us.addProductToUserCart(p1, u1.getUserId());
//
//        us.createOrder(u1.getUserId());  // PLACED order
//
//
//        var orders1 = os.getUncompletedOrdersByUserId(u1.getUserId());
//
//        os.assignDriverToOrder(orders1.get(0).getOrderId(), ds.getFirstAvailableDriver());  // DELIVERING order
//
//
//        os.completeOrder(orders1.get(0).getOrderId());  // COMPLETED order
//
//
//        System.out.println("");
//        System.out.println("");
//
////     =====================================================================================================
//
//        User u2 = new User("Sofia", "Benji", 22, new Address("Bucharest", 12, "Pipera"));
//        us.addUser(u2);
//        us.addProductToUserCart(p1, u2.getUserId());
//
//        us.createOrder(u2.getUserId());   // PLACED order
//
//        var orders2 = os.getUncompletedOrdersByUserId(u2.getUserId());
//
//        os.assignDriverToOrder(orders2.get(0).getOrderId(), ds.getFirstAvailableDriver());  // DELIVERING order
//
////     =====================================================================================================
//
//        Scanner scan = new Scanner(System.in);
//
//        int opt = -1;
//        while(opt != 0)   {
//            System.out.println("Choose from the options:\n");
//            System.out.println("0.Exit");
//            System.out.println("1.Register a new user");
//
//            System.out.println("2.Show all available restaurants");
//            System.out.println("3.Show the menu for a restaurant");
//
//            System.out.println("4.Add a product to a restaurant's menu");
//
//            System.out.println("5.Add a review for a restaurant");
//            System.out.println("6.Show all the reviews");
//
//            System.out.println("7.Add a product to your shop-cart");
//            System.out.println("8.Show your shop-cart");
//
//            System.out.println("9.Create an order");
//            System.out.println("10.Deliver an order");
//
//            System.out.println("11.Show order Status");
//
//            System.out.println("12.Complete an order - only for placed or delivering order");
//            System.out.println("13.Cancel an order - only for placed or delivering order");
//
//            System.out.println("14.Show all the orders");
//
//            System.out.println("15.Add a new driver");
//            System.out.println("16.Show all the drivers");
//
//            System.out.println("");
//
//            opt = scan.nextInt();
//            System.out.println("");
//
//            switch (opt){
//                case 1:{
//                    User u3 = new User("Criss", "Ann", 21, new Address("Bucharest", 2, "Romana"));
//                    us.addUser(u3);
//                    System.out.println("Successfully added user: " + u3.getFirstName() + " "+ u3.getLastName());
//                    break;
//                }
//                case 2:{
//                    rs.showRestaurants();
//                    break;
//                }
//                case 3:{
//                    rs.showMenu(r1.getRestaurantId());
//                    break;
//                }
//                case 4:{
//                    Product p2 = new Product(
//                            "Spring Pancakes",
//                            24,
//                            new ArrayList<String>(List.of("Chocolate Cream", "Fresh Fruit")));
//
//                    rs.addProductToRestaurant(p2, r1.getRestaurantId());
//                    System.out.println("Successfully added product: " + p2.getName());
//                    break;
//                }
//                case 5:{
//                    var rev2 = new Review(u1.getUserId(), r1.getRestaurantId(), 5, "Excellent eating experience");
//                    rs.addReviewToRestaurant(rev2);
//                    System.out.println("Successfully added review: " + rev2.getContent() + " - " + rev2.getStars()
//                            + " starts for Restaurant: " + r1.getName());
//                    break;
//                }
//                case 6:{
//                    System.out.println("Reviews for restaurant: " );
//                    rs.showRestaurantReviews(r1.getRestaurantId());
//                    break;
//                }
//                case 7:{
//                    Product p2 = new Product(
//                            "Spring Pancakes",
//                            24,
//                            new ArrayList<String>(List.of("Chocolate Cream", "Fresh Fruit")));
//
//                    us.addProductToUserCart(p2, u2.getUserId());
//                    System.out.println("Successfully added product: " + p2.getName() + " for user " + u2.getFirstName() + " " + u2.getLastName() );
//                    us.showCart(u2.getUserId());
//                    break;
//                }
//                case 8:{
//                    us.showCart(u2.getUserId());
//                    break;
//                }
//                case 9:{
//                    us.createOrder(u2.getUserId());
//                    System.out.println("Successfully placed order ");
//                    os.showUserOrderHistory(u2.getUserId());
//                    break;
//                }
//                case 10: {
//                    var orders3 = os.getUncompletedOrdersByUserId(u2.getUserId());
//                    os.assignDriverToOrder(orders3.get(1).getOrderId(), ds.getFirstAvailableDriver());
//
//                    System.out.println("Successfully delivered order ");
//                    os.showUserOrderHistory(u2.getUserId());
//
//                    break;
//                }
//                case 11:{
//                    os.showUserOrderHistory(u2.getUserId());
//                    break;
//                }
//                case 12:{
//                    var orders3 = os.getUncompletedOrdersByUserId(u2.getUserId());
//
//                    os.completeOrder(orders3.get(1).getOrderId());  // Works only for placed / delivering order
//                    System.out.println("Successfully completed order ");
//                    os.showUserOrderHistory(u2.getUserId());
//                    break;
//                }
//                case 13:{
//                    var orders3 = os.getUncompletedOrdersByUserId(u2.getUserId());
//
//                    os.cancelOrder(orders3.get(0).getOrderId());  // Works only for placed / delivering order
//                    System.out.println("Successfully canceled order ");
//                    os.showUserOrderHistory(u2.getUserId());
//                    break;
//                }
//                case 14:{
//                    os.showOrders();
//                    break;
//                }
//                case 15:{
//                    var driver2 = new Driver("Paul", "Ton", 45, Transport.Car);
//                    ds.addDriver(driver2);
//                    System.out.println("Successfully added driver: " + driver2.getFirstName() + " " + driver2.getLastName() );
//                    break;
//                }
//                case 16:{
//                    ds.showDrivers();
//                    break;
//                }
//                case 0:{
//                    System.out.println("Thank you for using our app! :) ");
//                    break;
//                }
//            }
//            System.out.println("");
//        }
    }
}
