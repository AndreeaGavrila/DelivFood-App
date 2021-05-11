package service;

import model.Product;
import model.Restaurant;
import model.Review;
import utils.CsvReadWrite;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


public class RestaurantService {

    private static RestaurantService instance;

    public static RestaurantService getInstance() {
        if (instance == null) {
            instance = new RestaurantService();
        }
        return instance;
    }

    private ProductService ps;
    private ReviewService revS;
    private ArrayList<Restaurant> restaurants;


    private RestaurantService() {

        this.ps = ProductService.getInstance();
        this.revS = ReviewService.getInstance();
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

        return this.restaurants.stream()
                .filter(r -> r.getName().equals(restName))
                .map(r -> r.getRestaurantId())
                .findFirst();
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


    public ArrayList<String> overviewRestaurants() {

        var res = new ArrayList<String>();
        for(var r : this.restaurants) {
            res.add(r.getName());
        }
        return res;
    }


    public void showRestaurants() {

        for(var r : restaurants) {
            System.out.println("All Restaurants:\n");
            System.out.println(r.toString());
        }
    }


    public void showMenu(UUID restId) throws Exception {

        System.out.println("Menu of the day:\n");
        var menuOpt = this.getMenuByRestaurantId(restId);
        var menu = menuOpt.orElseThrow(() -> new Exception("Error - Restaurant not found"));

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

        var restOpt = this.getRestaurantById(restId);
        var rest = restOpt.orElseThrow(() -> new Exception("Error - Restaurant not found"));

        System.out.println(rest.getName() + " Reviews:");
        for(var rev : rest.getReviews()) {
            System.out.println(rev.toString());
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
}
