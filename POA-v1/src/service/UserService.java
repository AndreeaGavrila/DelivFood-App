package service;
import model.Order;
import model.Product;
import model.User;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class UserService {
    private final ArrayList<User> users;
    private final OrderService orderService;

    public UserService(OrderService os) {
        orderService = os;
        this.users = new ArrayList<User>();
    }

    public Optional<User> getUserById(UUID userId) {
        return this.users.stream()
                .filter(u -> u.getUserId() == userId)
                .findFirst();
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void addProductToUserCart(Product prod, UUID userId) {
        var userOpt = this.getUserById(userId);
        userOpt.ifPresentOrElse(
                user -> user.getCart().add(prod),
                () -> System.out.println("Error - User not found!"));
    }

    public void showCart(UUID userId) {
        var userOpt = this.getUserById(userId);
        userOpt.ifPresentOrElse(
                user -> System.out.println(user.printCart()),
                () -> System.out.println("Error - User not found!"));
    }

    public void createOrder(UUID userId) {
        var userOpt = this.getUserById(userId);
        userOpt.ifPresentOrElse(
                user -> {
                    var newOrder = new Order(user.getUserId(), user.getCart());
                    orderService.addOrder(newOrder);
                    user.emptyCart();
                },
                () -> System.out.println("Error - User not found!"));
    }
}

