package ru.kir.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class WorkWithDB {
    private static Connection connection;
    private static Statement statement;

    private WorkWithDB() {

    }

    private static synchronized void openConnectionDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:cloud-server/users.db");
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void closeConnectionDB() {
        if (connection != null) {
            try {
                connection.close();
                statement = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized Statement getStatement() {
        if (statement == null) {
            openConnectionDB();
            return statement;
        } else {
            return statement;
        }
    }

}
