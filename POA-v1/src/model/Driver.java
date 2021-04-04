package model;
import java.util.UUID;

public class Driver extends Person {
    private final UUID driverId = UUID.randomUUID();
    protected Transport transport;
    protected DriverStatus status;
    protected int completedDeliveries = 0;


    public Driver(String firstName, String lastName, int age, Transport transport) {
        super(firstName, lastName, age);
        this.transport = transport;
        this.status = DriverStatus.Available;
    }

    public UUID getDriverId() {
        return driverId;
    }


    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }


    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }


    public int getCompletedDeliveries() {
        return completedDeliveries;
    }

    public void setCompletedDeliveries(int completedDeliveries) {
        this.completedDeliveries = completedDeliveries;
    }


    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + " , completed deliveries: " + this.completedDeliveries ;
    }
}

