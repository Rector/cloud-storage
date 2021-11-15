package ru.kir.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class WorkWithDB {
    private Connection connection;
    private Statement statement;

    private void openConnectionDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:cloud-server/users.db");
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnectionDB() {
        if (connection != null) {
            try {
                connection.close();
                statement = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Statement getStatement() {
        if (statement == null) {
            openConnectionDB();
            return statement;
        } else {
            return statement;
        }
    }
}
