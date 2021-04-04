package service;
import model.Product;
import model.Restaurant;
import model.Review;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;


public class RestaurantService {
    private final ArrayList<Restaurant> restaurants;

    public RestaurantService() {
        this.restaurants = new ArrayList<Restaurant>();
    }

    public Optional<Restaurant> getRestaurantById(UUID restId) {
        return this.restaurants.stream()
                .filter(r -> r.getRestaurantId() == restId)
                .findFirst();
    }

    public Optional<ArrayList<Product>> getMenuByRestaurantId(UUID restId) {
        return this.restaurants.stream()
                .filter(r -> r.getRestaurantId() == restId)
                .findFirst()
                .map(r -> r.getProducts());
    }

    public void addRestaurant(Restaurant rest) {
        restaurants.add(rest);
    }

    public void showRestaurants() {
        for(var r : restaurants) {
            System.out.println("All Restaurants:\n");
            System.out.println(r.toString());
        }
    }

    public void showMenu(UUID restId) {
        System.out.println("Menu of the day:\n");
        var menuOpt = this.getMenuByRestaurantId(restId);
        menuOpt.ifPresentOrElse(
                menu -> {
                    for(var prod : menu) {
                        System.out.println(prod.toString());
                    }
                },
                () -> System.out.println("Error - Restaurant not found!"));
    }

    public void addProductToRestaurant(Product prod, UUID restId) {
        var restOpt = this.getRestaurantById(restId);
        restOpt.ifPresentOrElse(
                rest -> rest.getProducts().add(prod),
                () -> System.out.println("Error - Restaurant not found!"));
    }

    public void addReviewToRestaurant(Review rev) {
        var restOpt = this.getRestaurantById(rev.getRestaurantId());
        restOpt.ifPresentOrElse(
                rest -> rest.getReviews().add(rev),
                () -> System.out.println("Error - Restaurant not found!"));
    }

    public void showRestaurantReviews(UUID restId) {
        var restOpt = this.getRestaurantById(restId);
        restOpt.ifPresentOrElse(
                rest -> {
                    System.out.println(rest.getName() + "  Reviews:");
                    for(var rev : rest.getReviews()) {
                        System.out.println(rev.toString());
                    }
                },
                () -> System.out.println("Error - Restaurant not found!"));
    }
}
