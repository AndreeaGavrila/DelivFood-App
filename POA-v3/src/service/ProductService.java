package service;

import model.Product;
import utils.CsvReadWrite;
import utils.Pair;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import utils.DbLayer;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;

public class ProductService {

    private static ProductService instance;

    private final Connection _db;


    public static ProductService getInstance() {
        if (instance == null) {
            instance = new ProductService();
        }
        return instance;
    }

    private ArrayList<Product> products;

    private ProductService() {
        _db = DbLayer.getInstance().getConnection();
        products = new ArrayList<Product>();
    }


    public void addProduct(Product p) {
        products.add(p);
    }


    public void listProducts() {

//        for(var p : this.products) {
//            System.out.println(p.toString());
//        }
        try{
            var var = _db.createStatement();

            var rs = var.executeQuery("SELECT\n" +
                    "BIN_TO_UUID(restaurant_id) AS restaurant_id,\n" +
                    "BIN_TO_UUID(product_id) AS product_id,\n" +
                    "name,\n" +
                    "price,\n" +
                    "ingredients\n" +
                    "FROM product;");
            while(rs.next()) {

                var p = new Product(
                        UUID.fromString(rs.getString("restaurant_id")),
                        UUID.fromString(rs.getString("product_id")),
                        rs.getString("name"),
                        rs.getFloat("price"),
                        rs.getString("ingredients")
                );

                System.out.println(p.toString());
            }

        }
        catch (SQLException e) {
            System.out.println(e.toString());
        }

    }


    public ArrayList<Pair<UUID, String>> overviewProducts() {

        var res = new ArrayList<Pair<UUID, String>>();

//        for (var p : products) {
//            res.add(new Pair(p.getProductId(), p.getName()));
//        }
//        return res;

        try{
            var var = _db.createStatement();
            var rs = var.executeQuery("SELECT\n" +
                    "BIN_TO_UUID(restaurant_id) AS restaurant_id,\n" +
                    "BIN_TO_UUID(product_id) AS product_id,\n" +
                    "name,\n" +
                    "price,\n" +
                    "ingredients\n" +
                    "FROM product;");

            while(rs.next()) {

                var p = new Product(
                        UUID.fromString(rs.getString("restaurant_id")),
                        UUID.fromString(rs.getString("product_id")),
                        rs.getString("name"),
                        rs.getFloat("price"),
                        rs.getString("ingredients")
                );

                res.add(new Pair(p.getProductId(), p.getName()));
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return res;
    }


    public Optional<Product> getProductById(UUID productId) {

//        return this.products.stream()
//                .filter(p -> p.getProductId().equals(productId))
//                .findFirst();

        try{
            String sql = "SELECT\n" +
                    "    BIN_TO_UUID(product_id) AS product_id,\n" +
                    "    name,\n" +
                    "    price,\n" +
                    "    ingredients,\n" +
                    "    BIN_TO_UUID(restaurant_id) AS restaurant_id\n" +
                    "FROM product WHERE product_id = UUID_TO_BIN(?);";

            var var = _db.prepareStatement(sql);
            var.setString(1, productId.toString());

            var rs = var.executeQuery();
            rs.next();

            return  Optional.of(parseResultSetItem(rs));

        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return Optional.empty();
    }


    public void saveAll(String file_path) {

        if(this.products == null) return;
        CsvReadWrite.writeAll(this.products, file_path);
    }


    public void readAll(String file_path) {

        CsvReadWrite.readAll(file_path).ifPresent((csvs) -> {
            var lst = csvs.stream()
                    .map(csv -> Product.parse(csv))
                    .collect(Collectors.toList());
            this.products = new ArrayList(lst);
        });
    }


    public Product parseResultSetItem(ResultSet rs){
        try{
            var restId = UUID.fromString(rs.getString("restaurant_id"));
            var prodId = UUID.fromString(rs.getString("product_id"));
            var name = rs.getString("name");
            var price = rs.getFloat("price");
            var ingredients = rs.getString("ingredients");
            return new Product(restId, prodId, name, price, ingredients);
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return null;
    }


    public void insert(Product prod) {
        try{
            String sql = "INSERT INTO product\n" +
                    "VALUES (UUID_TO_BIN(?), ?, ?, ?, UUID_TO_BIN(?));";

            var var = _db.prepareStatement(sql);
            var.setString(1, prod.getProductId().toString());
            var.setString(2, prod.getName());
            var.setFloat(3,prod.getPrice());
            var.setString(4, prod.printIngredients());
            var.setString(5, prod.getRestaurantId().toString());
            var.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

}
