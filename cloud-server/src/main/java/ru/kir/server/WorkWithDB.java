package ru.kir.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class WorkWithDB {
    private static Connection connection;
    private static Statement statement;

    private static void openConnectionDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:cloud-server/users.db");
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnectionDB() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Statement getStatement() {
        if (statement == null) {
            openConnectionDB();
            return statement;
        } else {
            return statement;
        }
    }
}
