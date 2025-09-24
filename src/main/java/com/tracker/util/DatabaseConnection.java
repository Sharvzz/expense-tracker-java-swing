package com.tracker.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection {
    public static final String driver = "com.mysql.cj.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost:3306/expense_tracker";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "Sharvesh";

    static{
        try{
            Class.forName(driver);
        }
        catch(ClassNotFoundException e){
            System.err.println("Unable to load the driver class!");
        }
    }

    public static Connection getDBConnection() throws SQLException{
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
