package service;

import model.Order;
import model.OrderStatus;

import utils.CsvReadWrite;
import utils.Pair;
import utils.DbLayer;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.time.ZoneId;
import java.time.ZonedDateTime;


public class OrderService {

    private static OrderService instance;

    private Connection _db = DbLayer.getInstance().getConnection();

    public static OrderService getInstance() {
        if (instance == null) {
            instance = new OrderService();
        }
        return instance;
    }

    private ArrayList<Order> orders;

    private final DriverService driverService;


    public OrderService() {

        orders = new ArrayList<Order>();
        driverService = DriverService.getInstance();
    }


    public String getOrderStatus(UUID orderId) {

//        return this.orders.stream()
//                .filter(o -> o.getOrderId().equals(orderId))
//                .map(o -> o.getStatus())
//                .findFirst()
//                .get()
//                .toString();

        try{
            String sql = "SELECT\n" +
                    "    status\n" +
                    "FROM orders\n" +
                    "WHERE BIN_TO_UUID(order_id) = ?;";

            var var = _db.prepareStatement(sql);
            var.setString(1, orderId.toString());
            var rs = var.executeQuery();

            rs.next();
            return rs.getString("status");

        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }


    public Optional<Order> getOrderById(UUID orderId) {

//        return this.orders.stream()
//                .filter(o -> o.getOrderId() == orderId)
//                .findFirst();

        try{
            var sql = "SELECT\n" +
                    "    BIN_TO_UUID(order_id) AS order_id,\n" +
                    "    BIN_TO_UUID(user_id) AS user_id,\n" +
                    "    BIN_TO_UUID(driver_id) as driver_id,\n" +
                    "    time_placed,\n" +
                    "    status\n" +
                    "FROM orders WHERE BIN_TO_UUID(order_id) = ?;";

            var var = _db.prepareStatement(sql);
            var.setString(1, orderId.toString());

            var rs = var.executeQuery();
            rs.next();
            var o = parseResultSetItem(rs);
            return Optional.of(o);

        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return Optional.empty();
    }


    public Order parseResultSetItem(ResultSet rs) {
        try {
            var orderId = UUID.fromString(rs.getString("order_id"));
            var userId = UUID.fromString(rs.getString("user_id"));
            var driverIdStr = rs.getString("driver_id");

            UUID driverId = null;
            if (driverIdStr != null) {
                driverId = UUID.fromString(driverIdStr);
            }

            var timePlaced = rs.getTimestamp("time_placed");
            var status = OrderStatus.valueOf(rs.getString("status"));
            var products = new ArrayList<UUID>();

            String sql2 = "SELECT\n" +
                    "    BIN_TO_UUID(P.product_id) AS product_id\n" +
                    "FROM product P, orders_product OP, orders O\n" +
                    "WHERE\n" +
                    "        O.order_id = UUID_TO_BIN(?) AND\n" +
                    "        P.product_id = OP.product_id AND\n" +
                    "        O.order_id = OP.order_id;";

            var var2 = _db.prepareStatement(sql2);
            var2.setString(1, orderId.toString());

            var rs2 = var2.executeQuery();
            while (rs2.next()) {
                var prodId = UUID.fromString(rs2.getString("product_id"));
                products.add(prodId);
            }

            return new Order( orderId, userId, driverId,
                    ZonedDateTime.ofInstant(timePlaced.toInstant(), ZoneId.of("UTC")),
                    status, products
            );
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return null;
    }


    public void showOrders() {

        System.out.println("ALL the Orders:\n");

//        for(var o : this.orders) {
//            System.out.println(o.toString());
//            System.out.println("");
//        }

        try{
            var var2 = _db.createStatement();

            var rs = var2.executeQuery("SELECT\n" +
                    "    BIN_TO_UUID(order_id) AS order_id,\n" +
                    "    BIN_TO_UUID(user_id) AS user_id,\n" +
                    "    BIN_TO_UUID(driver_id) as driver_id,\n" +
                    "    time_placed,\n" +
                    "    status\n" +
                    "FROM orders;");

            while(rs.next()) {
                var o = parseResultSetItem(rs);

                System.out.println(o.toString());
            }

        }
        catch (SQLException e) {
            System.out.println(e.toString());
        }
    }


    public ArrayList<Pair<UUID, String>> getUncompletedOrdersByUserId(UUID userId){

//        var lst =  orders.stream()
//                .filter(o ->
//                        o.getUserId().equals(userId) &&
//                                (o.getStatus().equals(OrderStatus.Placed) ||o.getStatus().equals(OrderStatus.Delivering)))
//                .map(o -> new Pair<UUID, String>(o.getOrderId(), o.toString()))
//                .collect(Collectors.toList());
//        return new ArrayList<>(lst);

        ArrayList<Pair<UUID, String>> res = new ArrayList<Pair<UUID, String>>();
        try{
            String sql = "SELECT\n" +
                    "    BIN_TO_UUID(order_id) AS order_id,\n" +
                    "    BIN_TO_UUID(user_id) AS user_id,\n" +
                    "    BIN_TO_UUID(driver_id) as driver_id,\n" +
                    "    time_placed,\n" +
                    "    status\n" +
                    "FROM orders\n" +
                    "WHERE status = 'Placed' OR status = 'Delivering' AND BIN_TO_UUID(user_id) = ?;";

            var var = _db.prepareStatement(sql);
            var.setString(1, userId.toString());
            var rs = var.executeQuery();

            while(rs.next()) {
                var o = parseResultSetItem(rs);
                res.add(new Pair<UUID, String>(o.getOrderId(), o.toString()));
            }

        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return res;
    }


    public ArrayList<Pair<UUID, String>> getOrdersByUserId(UUID userId){

        ArrayList<Pair<UUID, String>> res = new ArrayList<Pair<UUID, String>>();

        try{
            String sql = "SELECT\n" +
                    "    BIN_TO_UUID(order_id) AS order_id,\n" +
                    "    BIN_TO_UUID(user_id) AS user_id,\n" +
                    "    BIN_TO_UUID(driver_id) as driver_id,\n" +
                    "    time_placed,\n" +
                    "    status\n" +
                    "FROM orders\n" +
                    "WHERE BIN_TO_UUID(user_id) = ?;";

            var var = _db.prepareStatement(sql);
            var.setString(1, userId.toString());
            var rs = var.executeQuery();

            while(rs.next()) {
                var o = parseResultSetItem(rs);
                res.add(new Pair<UUID, String>(o.getOrderId(), o.toString()));
            }

        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return res;
    }



    public void showUserOrderHistory(UUID userId) {

        var userOrders = orders.stream()
                .filter(o -> o.getUserId() == userId)
                .collect(Collectors.toList());

        Collections.sort(userOrders);
        System.out.println("Show a user order Status:\n");
        for(var o : userOrders) {
            System.out.println(o.toString());
            System.out.println("");
        }
    }


    public void addOrder(Order order) {
        orders.add(order);
    }


    public void cancelOrder(UUID orderId) {

//        var orderOpt = this.getOrderById(orderId);
//        orderOpt.ifPresentOrElse(
//                order -> order.setStatus(OrderStatus.Cancelled),
//                () -> System.out.println("Order not found!")
//        );

        try{
            String sql = "UPDATE orders\n" +
                    "SET status = 'Cancelled'\n" +
                    "WHERE BIN_TO_UUID(order_id) = ?;";

            var var = _db.prepareStatement(sql);
            var.setString(1, orderId.toString());
            var.executeUpdate();

        }
        catch (SQLException e) {
            System.out.println(e.toString());
        }

    }


    public void assignDriverToOrder(UUID orderId, UUID driverId) throws Exception {

        var orderOpt = this.getOrderById(orderId);
        var order = orderOpt.orElseThrow(() -> new Exception("Order not found"));

//        order.setDriverId(driverId);
//        order.setStatus(OrderStatus.Delivering);

        try{
            String sql = "UPDATE orders\n" +
                    "SET status = 'Delivering', driver_id = UUID_TO_BIN(?)\n" +
                    "WHERE BIN_TO_UUID(order_id) = ?;";

            var var = _db.prepareStatement(sql);
            var.setString(1, driverId.toString());
            var.setString(2, orderId.toString());
            var.executeUpdate();

        }
        catch (SQLException e) {
            System.out.println(e.toString());
        }

    }


    public void completeOrder(UUID orderId) throws Exception {

//        var orderOpt = this.getOrderById(orderId);
//        var order = orderOpt.orElseThrow(() -> new Exception("Order not found"));
//
//        order.setStatus(OrderStatus.Completed);
//        try {
//            driverService.increaseCompletedDeliveries(order.getDriverId());
//        }
//        catch(Exception e) {
//            System.out.println(e);
//        }

        try{
            String sql = "UPDATE orders\n" +
                    "SET status = 'Completed'\n" +
                    "WHERE BIN_TO_UUID(order_id) = ?;";

            var var = _db.prepareStatement(sql);
            var.setString(1, orderId.toString());
            var.executeUpdate();

            String sql2 = "SELECT BIN_TO_UUID(driver_id) AS driver_id FROM orders WHERE BIN_TO_UUID(order_id) = ?";

            var var2 = _db.prepareStatement(sql2);
            var2.setString(1, orderId.toString());
            var rs = var2.executeQuery();

            rs.next();
            var driverId = UUID.fromString(rs.getString("driver_id"));

            driverService.increaseCompletedDeliveries(driverId);
        }
        catch (SQLException e) {
            System.out.println(e.toString());
        }

    }


    public void saveAll(String file_path) {

        if(this.orders == null) return;
        CsvReadWrite.writeAll(this.orders, file_path);
    }


    public void readAll(String file_path) {

        CsvReadWrite.readAll(file_path).ifPresent((csvs) -> {
            var lst = csvs.stream()
                    .map(csv -> Order.parse(csv))
                    .collect(Collectors.toList());
            this.orders = new ArrayList(lst);
        });
    }


    public void insert(Order order) {
        try{
            String sql = "INSERT INTO orders\n" +
                    "VALUES (UUID_TO_BIN(?), UUID_TO_BIN(?), UUID_TO_BIN(?), ?, ?);";

            var var = _db.prepareStatement(sql);
            var.setString(1, order.getOrderId().toString());
            var.setString(2, order.getUserId().toString());

            if(order.getDriverId() != null) {
                var.setString(3, order.getDriverId().toString());
            }
            else{
                var.setNull(3, Types.NULL);
            }

            var.setTimestamp(4, Timestamp.from(order.getTimePlaced().toInstant()));
            var.setString(5, order.getStatus().toString());
            var.executeUpdate();

            for(var prod : order.getProducts()) {
                String sql2 = "INSERT INTO orders_product\n" +
                        "VALUES (UUID_TO_BIN(?), UUID_TO_BIN(?));";

                var var2 = _db.prepareStatement(sql2);
                var2.setString(1, order.getOrderId().toString());
                var2.setString(2, prod.toString());
                var2.executeUpdate();
            }
        }
        catch (SQLException e) {
            System.out.println(e.toString());
        }
    }


}
