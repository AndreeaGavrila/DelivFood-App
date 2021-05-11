package service;

import model.Order;
import model.User;
import utils.CsvReadWrite;
import utils.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserService {

    private static UserService instance;

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


    private UserService() {
        orderService = OrderService.getInstance();
        productService = ProductService.getInstance();
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
        for (int i = 0; i < this.users.length; i++) {
            res.add(new Pair(this.users[i].getUserId(), this.users[i].toString()));
        }
        return res;
    }


    public void addProductToUserCart(UUID prodId, UUID userId) throws Exception {
        var userOpt = this.getUserById(userId);
        var user = userOpt.orElseThrow(() -> new Exception("Error - User not found"));
        user.addToCart(prodId);
    }


    public void showCart(UUID userId) throws Exception {
        var userOpt = this.getUserById(userId);
        var user = userOpt.orElseThrow(() -> new Exception("Error - User not found"));
        System.out.println(user.printCart());
    }


    public void createOrder(UUID userId) throws Exception {
        var userOpt = this.getUserById(userId);
        var user = userOpt.orElseThrow(() -> new Exception("Error - User not found"));

        var newOrder = new Order(user.getUserId(), user.getCart());
        orderService.addOrder(newOrder);
        user.emptyCart();
    }


    public void listUsers() {
        Arrays.sort(users);
        System.out.println("Users:");
        for (int i = 0; i < users.length; i++) {
            System.out.println(users[i].toString());
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
}

