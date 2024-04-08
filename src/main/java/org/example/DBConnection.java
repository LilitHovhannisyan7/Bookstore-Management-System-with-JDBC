package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String address = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "password";
        return DriverManager.getConnection(address, username, password);
    }
}
