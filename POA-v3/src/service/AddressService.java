package service;

import model.Address;

import utils.DbLayer;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;

public class AddressService {

    private static AddressService instance;

    public static AddressService getInstance() {
        if (instance == null) {
            instance = new AddressService();
        }
        return instance;
    }

//    private Connection _db = DbLayer.getInstance().getConnection();
    public final Connection _db;
    public AddressService() {
        _db = DbLayer.getInstance().getConnection();
    }


    public Integer getId(Address add) {
        try{
            String sql = "SELECT * FROM address WHERE city = ? AND number = ? AND street = ? ;";

            var var = _db.prepareStatement(sql);
            var.setString(1, add.getCity());
            var.setInt(2, add.getNumber());
            var.setString(3, add.getStreet());
            var rs = var.executeQuery();

            rs.next();
            return rs.getInt("address_id");

        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }


    public Address getById(int addId) {
        try{
            String sql = "SELECT * FROM address WHERE address_id = ?;";

            var var = _db.prepareStatement(sql);
            var.setInt(1, addId);
            var rs = var.executeQuery();

            rs.next();
            return  new Address( rs.getString("city"), rs.getInt("number"), rs.getString("street") );

        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }


    public void insert(Address add) {
        try{
            String sql = "INSERT INTO address (city, number, street) VALUES (?, ?, ?)";

            var var = _db.prepareStatement(sql);
            var.setString(1, add.getCity());
            var.setInt(2, add.getNumber());
            var.setString(3, add.getStreet());
            var.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e);
        }
    }


}
