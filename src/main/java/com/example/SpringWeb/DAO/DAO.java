package com.example.SpringWeb.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAO {
    public static Connection con;
    private String dbClass = "com.mysql.jdbc.Driver";
    static final String urlString = "jdbc:mysql://localhost:3306/test";
    static final String rootString = "root";
    static final String passString = "123456";


    public DAO() {
        if (con == null) {
            try {
                Class.forName(dbClass);
                con = DriverManager.getConnection(urlString, rootString, passString);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        con = getConnection();
    }

    protected Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(urlString, rootString, passString);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return con;
    }
}
