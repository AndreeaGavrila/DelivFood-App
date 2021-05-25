package service;

import model.Order;
import model.User;
import model.Product;
import model.Address;

import utils.CsvReadWrite;
import utils.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import utils.DbLayer;
import java.sql.Connection;
import java.sql.SQLException;

public class UserService {

    private static UserService instance;

    private static final AddressService as = AddressService.getInstance();

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    static final private int MAX_NUM_USERS = 100;
    private int currentNumUsers = 0;

    private User[] users = null;

    private final OrderService orderService;
    private final ProductService productService;

    public final Connection _db;

    private UserService() {
        orderService = OrderService.getInstance();
        productService = ProductService.getInstance();

        _db = DbLayer.getInstance().getConnection();
    }


    public Optional<User> getUserById(UUID userId) {

        User user = null;
        for (int i = 0; i < this.users.length && user == null; i++) {
            if(this.users[i].getUserId().equals(userId)) {
                user = this.users[i];
            }
        }
        if(user == null) {
            return Optional.empty();
        }
        return Optional.of(user);
    }


    public void addUser(User user) {
        if(this.users == null) {
            this.users = new User[]{user};
        }
        else {
            if(this.users.length == MAX_NUM_USERS) {
                System.out.println("Error - Maximum user capacity reached.");
                return;
            }
            this.users = Arrays.copyOf(this.users, this.users.length+1);
            this.users[this.users.length-1] = user;
        }
    }


    public ArrayList<Pair<UUID, String>> overviewUsers() {

        var res = new ArrayList<Pair<UUID, String>>();

//        for (int i = 0; i < this.users.length; i++) {
//            res.add(new Pair(this.users[i].getUserId(), this.users[i].toString()));
//        }
//        return res;

        try{
            var var = _db.createStatement();

            var rs = var.executeQuery("SELECT\n" +
                    "    BIN_TO_UUID(user_id) AS user_id,\n" +
                    "    first_name,\n" +
                    "    last_name,\n" +
                    "    age,\n" +
                    "    address_id\n" +
                    "FROM user;");
            while(rs.next()) {

                var u = new User(
                        UUID.fromString(rs.getString("user_id")),
                        rs.getString("last_name"),
                        rs.getString("first_name"),
                        rs.getInt("age"),
                        rs.getInt("address_id"));

                res.add(new Pair(u.getUserId(), u.toString()));
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return res;

    }


    public void addProductToUserCart(UUID prodId, UUID userId) throws Exception {

//        var userOpt = this.getUserById(userId);
//        var user = userOpt.orElseThrow(() -> new Exception("Error - User not found"));
//        user.addToCart(prodId);

        try{
            String sql = "INSERT INTO user_product\n" +
                    "VALUES (UUID_TO_BIN(?), UUID_TO_BIN(?));";

            var var = _db.prepareStatement(sql);
            var.setString(1, userId.toString());
            var.setString(2, prodId.toString());
            var.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.toString());
        }
    }


    public void showCart(UUID userId) throws Exception {

//        var userOpt = this.getUserById(userId);
//        var user = userOpt.orElseThrow(() -> new Exception("Error - User not found"));
//        System.out.println(user.printCart());

        String res = "";

//        res += "SHOPPING CART:\n";
//        for(var p : this.cart) {
//            res += ps.getProductById(p).get().summary();
//        }

        try{
            var sql = "SELECT\n" +
                    "       BIN_TO_UUID(P.product_id) AS product_id,\n" +
                    "       P.name as name,\n" +
                    "       P.price as price,\n" +
                    "       P.ingredients as ingredients,\n" +
                    "       BIN_TO_UUID(P.restaurant_id) as restaurant_id\n" +
                    "FROM product AS P, user_product UP\n" +
                    "WHERE\n" +
                    "      UP.product_id = P.product_id AND\n" +
                    "      UP.user_id = UUID_TO_BIN(?);";

            var var = _db.prepareStatement(sql);
            var.setString(1, userId.toString());

            var rs = var.executeQuery();

            while(rs.next()) {

                var p = new Product(
                        UUID.fromString(rs.getString("restaurant_id")),
                        UUID.fromString(rs.getString("product_id")),
                        rs.getString("name"),
                        rs.getFloat("price"),
                        rs.getString("ingredients"));

                res += p.printProduct();
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        System.out.println(res);

    }



    public ArrayList<UUID> getCart(UUID userId){

        ArrayList<UUID> res = new ArrayList<UUID>();

        try{
            var sql = "SELECT\n" +
                    "       BIN_TO_UUID(P.product_id) AS product_id\n" +
                    "FROM product AS P, user_product UP\n" +
                    "WHERE\n" +
                    "      UP.product_id = P.product_id AND\n" +
                    "      UP.user_id = UUID_TO_BIN(?);";

            var var = _db.prepareStatement(sql);
            var.setString(1, userId.toString());

            var rs = var.executeQuery();

            while(rs.next()) {
                res.add(UUID.fromString(rs.getString("product_id")));
            }

        }
        catch (SQLException e) {
            System.out.println(e.toString());
        }
        return res;
    }


    public void emptyCart(UUID userId) {

        try{
            String sql = "DELETE\n" +
                    "FROM user_product\n" +
                    "WHERE user_id = UUID_TO_BIN(?);";

            var var = _db.prepareStatement(sql);
            var.setString(1, userId.toString());
            var.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.toString());
        }
    }


    public void createOrder(UUID userId) throws Exception {

//        var userOpt = this.getUserById(userId);
//        var user = userOpt.orElseThrow(() -> new Exception("Error - User not found"));
//        var newOrder = new Order(user.getUserId(), user.getCart());

        var newOrder = new Order(userId, getCart(userId));

//        orderService.addOrder(newOrder);
        orderService.insert(newOrder);

//        user.emptyCart();
        emptyCart(userId);
    }



    public void listUsers() {

//        Arrays.sort(users);
//        System.out.println("Users:");
//        for (int i = 0; i < users.length; i++) {
//            System.out.println(users[i].toString());
//        }

        try{
            var var = _db.createStatement();

            var rs = var.executeQuery("SELECT\n" +
                    "    BIN_TO_UUID(user_id) AS user_id,\n" +
                    "    first_name,\n" +
                    "    last_name,\n" +
                    "    age,\n" +
                    "    address_id\n" +
                    "FROM user;");
            while(rs.next()) {

                var u = new User(
                        UUID.fromString(rs.getString("user_id")),
                        rs.getString("last_name"),
                        rs.getString("first_name"),
                        rs.getInt("age"),
                        rs.getInt("address_id"));

                System.out.println(u.toString());
            }

        }
        catch (SQLException e) {
            System.out.println(e.toString());
        }
    }


    public void saveAll(String file_path) {
        if(this.users == null) return;
        CsvReadWrite.writeAll(new ArrayList(Arrays.asList(this.users)), file_path);
    }


    public void readAll(String file_path) {

        CsvReadWrite.readAll(file_path).ifPresent((csvs) -> {
            var lst = csvs.stream()
                    .map(csv -> User.parse(csv))
                    .collect(Collectors.toList());
            this.users = (User[])new ArrayList(lst).toArray(new User[0]);
        });
    }


    public void insert(User user) {

        try{
            var add = user.getAddressDelivery();
            as.insert(add);
            var addId = as.getId(add);

            String sql = "INSERT INTO user\n" +
                    "VALUES (UUID_TO_BIN(?), ?, ?, ?, ?);";

            var var = _db.prepareStatement(sql);
            var.setString(1, user.getUserId().toString());
            var.setString(2, user.getFirstName());
            var.setString(3, user.getLastName());
            var.setInt(4, user.getAge());
            var.setInt(5, addId);
            var.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.toString());
        }
    }


}

