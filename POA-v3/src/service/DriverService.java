package service;

import model.Driver;
import model.DriverStatus;
import model.Transport;

import utils.CsvReadWrite;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import utils.DbLayer;
import java.sql.Connection;
import java.sql.SQLException;

public class DriverService {

    private static DriverService instance;

    private Connection _db =DbLayer.getInstance().getConnection();

    public static DriverService getInstance() {
        if (instance == null) {
            instance = new DriverService();
        }
        return instance;
    }

    private ArrayList<Driver> drivers;

    private DriverService() {
        this.drivers = new ArrayList<Driver>();
    }


    public Optional<Driver> getDriverById(UUID driverId) {

        return this.drivers.stream()
                .filter(r -> r.getDriverId() == driverId)
                .findFirst();
    }


    public void addDriver(Driver driver) {
        this.drivers.add(driver);
    }


    public void showDrivers() {

        for(var d : this.drivers) {
            System.out.println("Drivers:");
            System.out.println(d.toString());
        }
    }


    public void listDrivers() {

//        for(var d : drivers) {
//            System.out.println("Drivers:");
//            System.out.println(d.toString());
//        }

        System.out.println("List Drivers:");
        try{
            var var = _db.createStatement();
            var rs = var.executeQuery("SELECT\n" +
                    "    BIN_TO_UUID(driver_id) AS driver_id,\n" +
                    "    first_name,\n" +
                    "       last_name,\n" +
                    "       age,\n" +
                    "       transport,\n" +
                    "       status,\n" +
                    "       completed_deliveries\n" +
                    "FROM driver;");
            while(rs.next()) {

                var d = new Driver(
                        UUID.fromString(rs.getString("driver_id")),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        Transport.valueOf(rs.getString("transport")),
                        DriverStatus.valueOf(rs.getString("status")),
                        rs.getInt("completed_deliveries"));

                System.out.println(d.toString());
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        }

    }


    public UUID getFirstAvailableDriver() {

//        return this.drivers.stream()
//                .filter(d -> d.getStatus().equals(DriverStatus.Available))
//                .map(Driver::getDriverId)
//                .findFirst()
//                .orElse(null);

        try{
            var sql = "SELECT BIN_TO_UUID(driver_id) AS driver_id\n" +
                    "FROM driver WHERE status = 'Available'\n" +
                    "LIMIT 1;";
            var var = _db.createStatement();
            var rs = var.executeQuery(sql);
            rs.next();
            return UUID.fromString(rs.getString("driver_id"));
        }
        catch (SQLException e) {
            System.out.println(e.toString());
        }
        return null;
    }


    public void increaseCompletedDeliveries(UUID driverId) throws Exception {

//        var driverOpt = this.getDriverById(driverId);
//        var driver = driverOpt.orElseThrow(() -> new Exception("Error - Driver not found"));
//        driver.setCompletedDeliveries(driver.getCompletedDeliveries() + 1);

        try{
            String sql = "UPDATE driver\n" +
                    "SET completed_deliveries = completed_deliveries + 1\n" +
                    "WHERE BIN_TO_UUID(driver_id) = ?;";

            var var = _db.prepareStatement(sql);
            var.setString(1, driverId.toString());
            var.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.toString());
        }

    }


    public void saveAll(String file_path) {

        if(this.drivers == null) return;
        CsvReadWrite.writeAll(this.drivers, file_path);
    }


    public void readAll(String file_path) {

        CsvReadWrite.readAll(file_path).ifPresent((csvs) -> {
            var lst = csvs.stream()
                    .map(csv -> Driver.parse(csv))
                    .collect(Collectors.toList());
            this.drivers = new ArrayList(lst);
        });
    }


    public void insert(Driver driver) {
        try{
            String sql = "INSERT INTO driver\n" +
                    "VALUES (UUID_TO_BIN(?), ?, ?, ?, ?, ?, ?);";

            var var = _db.prepareStatement(sql);
            var.setString(1, driver.getDriverId().toString());
            var.setString(2, driver.getFirstName());
            var.setString(3, driver.getLastName());
            var.setInt(4, driver.getAge());
            var.setString(5, driver.getTransport().toString());
            var.setString(6, driver.getStatus().toString());
            var.setInt(7, driver.getCompletedDeliveries());
            var.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.toString());
        }
    }


}
