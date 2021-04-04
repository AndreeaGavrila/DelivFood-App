package service;
import model.Driver;
import model.DriverStatus;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class DriverService {
    private final ArrayList<Driver> drivers;

    public DriverService() {
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

    public UUID getFirstAvailableDriver() {
        return this.drivers.stream()
                .filter(d -> d.getStatus() == DriverStatus.Available)
                .map(Driver::getDriverId)
                .findFirst()
                .orElse(null);
    }

    public void increaseCompletedDeliveries(UUID driverId) {
        var driverOpt = this.getDriverById(driverId);
        driverOpt.ifPresentOrElse(
                driver -> driver.setCompletedDeliveries(driver.getCompletedDeliveries() + 1),
                () -> System.out.println("Error - Driver not found!")
        );
    }

}
