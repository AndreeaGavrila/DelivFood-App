package model;

import utils.ICsvConvertible;

import java.util.ArrayList;
import java.util.UUID;


public class Driver extends Person implements ICsvConvertible<Driver> {

    private final UUID driverId;
    protected Transport transport;
    protected DriverStatus status;
    protected int completedDeliveries = 0;

    public Driver(UUID driverId,
                  String firstName, String lastName,
                  int age,
                  Transport transport,
                  DriverStatus status,
                  int completedDeliveries) {

        super(firstName, lastName, age);
        this.driverId = driverId;
        this.transport = transport;
        this.status = status;
        this.completedDeliveries = completedDeliveries;
    }

    public Driver(String firstName, String lastName,
                  int age, Transport transport) {

        super(firstName, lastName, age);
        this.driverId = UUID.randomUUID();
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

    @Override
    public String[] stringify() {
        // driverId, firstName, lastName, age, transport, status, completedDeliveries
        ArrayList s = new ArrayList<String>();

        s.add(this.driverId.toString());
        s.add(this.firstName);
        s.add(this.lastName);
        s.add(Integer.toString(this.age));
        s.add(this.transport.toString());
        s.add(this.status.toString());
        s.add(Integer.toString(this.completedDeliveries));

        return (String[])s.toArray(new String[0]);
    }

    public static Driver parse(String csv) {

        var parts = csv.split(",");
        UUID driverId = UUID.fromString(parts[0]);
        String firstName = parts[1];
        String lastName = parts[2];
        int age = Integer.parseInt(parts[3]);
        Transport transport = Transport.valueOf(parts[4]);
        DriverStatus status = DriverStatus.valueOf(parts[5]);
        int completedDeliveries = Integer.parseInt(parts[6]);

        return new Driver(driverId, firstName, lastName, age, transport, status, completedDeliveries);
    }
}

