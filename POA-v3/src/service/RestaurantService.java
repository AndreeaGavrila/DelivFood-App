package service;

import model.Product;
import model.Restaurant;
import model.Review;

import utils.CsvReadWrite;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import utils.Pair;

import utils.DbLayer;
import java.sql.Connection;
import java.sql.SQLException;

public class RestaurantService {

    private static RestaurantService instance;

    private final Connection _db;

    public static RestaurantService getInstance() {
        if (instance == null) {
            instance = new RestaurantService();
        }
        return instance;
    }

    private ProductService ps;
    private ReviewService revS;
    private AddressService as;

    private ArrayList<Restaurant> restaurants;

    private RestaurantService() {

        this.ps = ProductService.getInstance();
        this.revS = ReviewService.getInstance();
        this.as = as = AddressService.getInstance();
        this._db = DbLayer.getInstance().getConnection();
        this.restaurants = new ArrayList<Restaurant>();
    }


    public void addRestaurant(Restaurant rest) {
        restaurants.add(rest);
    }


    public Optional<Restaurant> getRestaurantById(UUID restId) {

        return this.restaurants.stream()
                .filter(r -> r.getRestaurantId().equals(restId))
                .findFirst();
    }


    public Optional<UUID> getRestaurantIdFromName(String restName) {

//        return this.restaurants.stream()
//                .filter(r -> r.getName().equals(restName))
//                .map(r -> r.getRestaurantId())
//                .findFirst();

        try{
            String sql = "SELECT BIN_TO_UUID(restaurant_id) AS restaurant_id " +
                    "FROM restaurant " +
                    "WHERE name = ?;";

            var var = _db.prepareStatement(sql);
            var.setString(1, restName);

            var rs = var.executeQuery();
            rs.next();

            return Optional.of(UUID.fromString(rs.getString("restaurant_id")));

        }
        catch (SQLException e) {
            System.out.println(e);
        }

        return Optional.empty();

    }


    public Optional<ArrayList<Product>> getMenuByRestaurantId(UUID restId) {

        var prodIds =  this.restaurants.stream()
                .filter(r -> r.getRestaurantId().equals(restId))
                .findFirst()
                .map(Restaurant::getProducts)
                .get();

        var products = prodIds.stream().map(prodId -> ps.getProductById(prodId).get()).collect(Collectors.toList());

        return Optional.of(new ArrayList<Product>(products));
    }


//    public ArrayList<String> overviewRestaurants() {
//
//        var res = new ArrayList<String>();
//        for(var r : this.restaurants) {
//            res.add(r.getName());
//        }
//        return res;
//    }

    public ArrayList<Pair<String, UUID>> overviewRestaurants() {

        var res = new ArrayList<Pair<String, UUID>>();
//        for(var r : this.restaurants) {
//            res.add(r.getName());
//        }

        try{
            var var = _db.createStatement();

            var rs = var.executeQuery("SELECT\n" +
                    "    BIN_TO_UUID(restaurant_id) AS restaurant_id,\n" +
                    "    name,\n" +
                    "    address_id,\n" +
                    "    description\n" +
                    "FROM restaurant;");

            while(rs.next()) {

                var r = new Restaurant(
                        UUID.fromString(rs.getString("restaurant_id")),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("address_id"));

                res.add(new Pair(r.getName(), r.getRestaurantId()));
            }

        }
        catch (SQLException e) {
            System.out.println(e.toString());
        }
        return res;
    }


    public void showRestaurants() {

//        for(var r : restaurants) {
//            System.out.println("All Restaurants:\n");
//            System.out.println(r.toString());
//        }
        try{
            var var = _db.createStatement();

            var rs = var.executeQuery("SELECT\n" +
                    "    BIN_TO_UUID(restaurant_id) AS restaurant_id,\n" +
                    "    address_id,\n" +
                    "    name,\n" +
                    "    description\n" +
                    "FROM restaurant;");

            while(rs.next()) {

                var r = new Restaurant(
                        UUID.fromString(rs.getString("restaurant_id")),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("address_id"));

                System.out.println(r.toString());
            }

        }
        catch (SQLException e) {
            System.out.println(e.toString());
        }

    }


    public void showMenu(UUID restId) throws Exception {

        System.out.println("Menu of the day:\n");

//        var menuOpt = this.getMenuByRestaurantId(restId);
//        var menu = menuOpt.orElseThrow(() -> new Exception("Error - Restaurant not found"));

//        for(var prod : menu) {
//            System.out.println(prod.toString());
//        }

        ArrayList<Product> menu = new ArrayList<Product>();
        try{
            String sql = "SELECT\n" +
                    "    BIN_TO_UUID(product_id) AS product_id,\n" +
                    "    name,\n" +
                    "    price,\n" +
                    "    ingredients,\n" +
                    "    BIN_TO_UUID(restaurant_id) AS restaurant_id\n" +
                    "FROM product WHERE restaurant_id = UUID_TO_BIN(?);";

            var var = _db.prepareStatement(sql);
            var.setString(1, restId.toString());

            var rs = var.executeQuery();

            while(rs.next())
            {
                var p = ps.parseResultSetItem(rs);
                menu.add(p);
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }

        for(var prod : menu) {
            System.out.println(prod.toString());
        }
    }


    public void addProductToRestaurant(Product prod, UUID restId) throws Exception {

        var restOpt = this.getRestaurantById(restId);
        var rest = restOpt.orElseThrow(() -> new Exception("Error - Restaurant not found"));
        rest.getProducts().add(prod.getProductId());
    }


    public void addReviewToRestaurant(Review rev) throws Exception {

        var restOpt = this.getRestaurantById(rev.getRestaurantId());
        var rest = restOpt.orElseThrow(() -> new Exception("Error - Restaurant not found"));

        revS.addReview(rev);
        rest.addReview(rev.getUserId());
    }


    public void showRestaurantReviews(UUID restId) throws Exception {

//        var restOpt = this.getRestaurantById(restId);
//        var rest = restOpt.orElseThrow(() -> new Exception("Error - Restaurant not found"));
//
//        System.out.println(rest.getName() + " Reviews:");
//        for(var rev : rest.getReviews()) {
//            System.out.println(rev.toString());
//        }

        try{
            var sql = "SELECT\n" +
                    "    BIN_TO_UUID(user_id) AS user_id,\n" +
                    "    BIN_TO_UUID(restaurant_id) AS restaurant_id,\n" +
                    "    stars,\n" +
                    "    content\n" +
                    "FROM review\n" +
                    "WHERE BIN_TO_UUID(restaurant_id) = ?;";

            var var = _db.prepareStatement(sql);
            var.setString(1, restId.toString());

            var rs = var.executeQuery();
            while(rs.next()) {

                var r = new Review(
                        UUID.fromString(rs.getString("user_id")),
                        UUID.fromString(rs.getString("restaurant_id")),
                        rs.getInt("stars"),
                        rs.getString("content")
                );

                System.out.println(r.toString());
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

    }


    public void saveAll(String file_path) {

        if(this.restaurants == null) return;
        CsvReadWrite.writeAll(this.restaurants, file_path);
    }


    public void readAll(String file_path) {

        CsvReadWrite.readAll(file_path).ifPresent((csvs) -> {
            var lst = csvs.stream()
                    .map(csv -> Restaurant.parse(csv))
                    .collect(Collectors.toList());
            this.restaurants = new ArrayList(lst);
        });
    }


    public void insert(Restaurant rest) {
        try{
            var add = rest.getLocation();
            as.insert(add);
            var addId = as.getId(add);

            String sql = "INSERT INTO restaurant\n" +
                    "VALUES (UUID_TO_BIN(?), ?, ?, ?);";

            var var = _db.prepareStatement(sql);
            var.setString(1, rest.getRestaurantId().toString());
            var.setInt(2, addId);
            var.setString(3, rest.getName());
            var.setString(4, rest.getDescription());
            var.executeUpdate();
        }
        catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }


}
