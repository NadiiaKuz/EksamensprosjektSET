package org.gruppe4.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDatabase {

    private String url;
    private String username;
    private String password;

    private Connection connection;

    public MySQLDatabase(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Connection startDB() throws MySQLDatabaseException {
        try{
            //Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to MySQLDatabase successfully");
            return connection;
        }
        //catch (ClassNotFoundException e){
            //throw new MySQLDatabaseException("Driver class was not found", e);}
        catch (SQLException e) {
            throw new MySQLDatabaseException("Could not connect to database", e);
        }
    }

    public Connection getConnection(){
        return connection;
    }

    public void stopDB() throws MySQLDatabaseException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("MySQLDatabase connection closed.");
            }
        } catch (SQLException e) {
            throw new MySQLDatabaseException("Could not close database connection", e);
        }
    }
}
