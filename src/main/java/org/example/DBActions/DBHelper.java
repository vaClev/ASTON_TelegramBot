package org.example.DBActions;
import java.sql.*;

class DBHelper {
    private static final String jdbcUrl = "jdbc:postgresql://localhost:5432/telegram_OV_first_bot";
    private static final String username = "postgres";
    private static final String password = "1234";
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;

    void connect(){
        // Register the PostgreSQL driver
        try {
            Class.forName("org.postgresql.Driver");
        }  catch (ClassNotFoundException e) {
            System.out.println("ошибка регистрации драйвера PostgreSQL");
        }
        // Connect to the database
        try {
            this.connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            System.out.println("ошибка подключения к базе данных" + jdbcUrl + " " + username + " " + password);
        }
    }
    PreparedStatement getPreparedStatement(String templateSQLQuery) {
        try {
            this.preparedStatement = this.connection.prepareStatement(templateSQLQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  this.preparedStatement;
    }
    void close() {
        try {
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
