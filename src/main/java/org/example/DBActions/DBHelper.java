package org.example.DBActions;
import java.sql.*;

class DBHelper {
    private static final String jdbcUrl = "jdbc:postgresql://localhost:5432/telegram_OV_first_bot";
    private static final String username = "postgres";
    private static final String password = "1234";
    private Connection connection = null;
    private Statement statement = null;

    private Statement getStatement() {
        try {
            // Register the PostgreSQL driver
            Class.forName("org.postgresql.Driver");
            // Connect to the database
            this.connection = DriverManager.getConnection(jdbcUrl, username, password);
            this.statement = connection.createStatement();
            return statement;
        } catch (ClassNotFoundException e) {
            System.out.println("ошибка регистрации драйвера PostgreSQL");
        } catch (SQLException e) {
            System.out.println("ошибка подключения к базе данных" + jdbcUrl + " " + username + " " + password);
        }
        throw new RuntimeException();
    }
    ResultSet getResultSet(String SQLQuery){
        ResultSet resultSet = null;
        try {
            resultSet = getStatement().executeQuery(SQLQuery);
        }catch (SQLException e)
        {
            System.out.println("ошибка в тексте запроса "+SQLQuery);
        }
        return resultSet;
    }
    void executeUpdate(String SQLQuery) throws SQLException {
        getStatement().executeUpdate(SQLQuery);
        close();
    }
    void printResultSet(ResultSet resultSet) {
        try {
            int columns = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i < columns; i++) {
                    System.out.print(resultSet.getString(i) + " ");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("ошибка вывода ResultSet");
        }
    }
    void close() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
