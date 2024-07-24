package org.okten;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class JdbcDemo {

    private static String CONNECTION_URL = "jdbc:postgresql://localhost:5432/XXX";

    private static String DB_USERNAME = "postgres";
    private static String DB_PASSWORD = "postgres";

    // JDBC - Java Data Base connectivity

    @SneakyThrows
    public static void main(String[] args) {
        Class.forName("org.postgresql.Driver");

        executeWithStatement("create table if not exists products (id bigint, name varchar(255), price numeric)");

//        addProduct(1L, "test", 5.99);
//        addProduct(2L, "test2", 90D);
        addProductWithPreparedStatement(3L, "new product', 999); delete from products where 1=1; insert into products (id, name, price) values (123, 'new product 2", 999D);
    }

    public static void addProduct(Long id, String name, Double price) {
        executeWithStatement("insert into products (id, name, price) values (%s, '%s', %s)".formatted(id, name, price));
    }

    public static void addProductWithPreparedStatement(Long id, String name, Double price) {
        executeWithPreparedStatement("insert into products (id, name, price) values (?, ?, ?)", id, name, price);
    }

    @SneakyThrows
    public static void executeWithStatement(String query) {
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, DB_USERNAME, DB_PASSWORD)) {
            Statement statement = connection.createStatement();
            System.out.println("Executing: " + query);
            statement.execute(query);
        }
    }

    @SneakyThrows
    public static void executeWithPreparedStatement(String query, Object... params) {
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, DB_USERNAME, DB_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            preparedStatement.execute();
        }
    }
}
