/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.tikapeharjoitustyo2.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    public static Connection getConnection() {
        try {
            String dbUrl = System.getenv("JDBC_DATABASE_URL");
            if (dbUrl != null && dbUrl.length() > 0) {
                return DriverManager.getConnection(dbUrl);
            }

            return DriverManager.getConnection("jdbc:sqlite:huonekalut.db");
        } catch(Exception e) {
            System.out.println("Can't connect to DB");
            return null;
        }
    }
}