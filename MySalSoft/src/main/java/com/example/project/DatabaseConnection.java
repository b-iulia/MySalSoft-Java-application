package com.example.project;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public Connection databaseLink;
    public Connection getDatabaseLink() {
        String databaseName = "mysalsoft";
        String databaseUser = "root";
        String databasePassword = "";
        //String url = "jdbc:mysql://127.0.0.1/" + databaseName;
        String url = Pproperties.dbLink + databaseName;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return databaseLink;
    }
}
