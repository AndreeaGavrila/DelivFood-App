package utils;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbLayer {

    private Connection _connection = null;

    private static DbLayer instance;

    public static DbLayer getInstance() {
        if (instance == null) {
            instance = new DbLayer();
        }
        return instance;
    }

    public DbLayer() {
        try{
//            Class.forName("com.mysql.jdbc.Driver");

            _connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/fdelivery",
                    "root", "root");
        }
        catch(Exception e) {
            System.out.println(e);
        }


    }

    public Connection getConnection() {
        return _connection;
    }



}
