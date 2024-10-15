package org.example.DBActions;

import org.example.DBEntities.UserEntity;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBUserBehavior implements DBBehavior<UserEntity> {
    private final DBHelper db = new DBHelper();
    private ResultSet resultSet;
    private UserEntity foundedUser;
    private ArrayList<UserEntity> users;
    private static final String SQL_QUERY_SELECT_ALL_USERS = "SELECT id, telegram_chat_id, username FROM users;";
    private static final String TEMPLATE_SQL_QUERY_SELECT_USER_BY_TELEGRAM_CHAT_ID = "SELECT id, telegram_chat_id, username FROM users WHERE telegram_chat_id=?;";
    private static final String TEMPLATE_SQL_QUERY_INSERT_USER = "INSERT INTO users (telegram_chat_id, username) VALUES (?, ?);";

    @Override
    public ArrayList<UserEntity> getAll() {
        db.connect();
        users = new ArrayList<>();
        PreparedStatement statement = db.getPreparedStatement(SQL_QUERY_SELECT_ALL_USERS);
        try {
            resultSet = statement.executeQuery();
            collectUsers();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        db.close();
        return users;
    }
    private void collectUsers() throws SQLException {
        boolean isEmpty = true;
        while (resultSet.next()) {
            isEmpty = false;
            foundedUser = buildUserFromResultSet();
            users.add(foundedUser);
        }
        if (isEmpty) System.out.println("Таблица Members пустая");
    }
    private UserEntity buildUserFromResultSet() throws SQLException {
        return new UserEntity(
                resultSet.getLong("id"),
                resultSet.getLong("telegram_chat_id"),
                resultSet.getString("username")
        );
    }

    @Override
    public UserEntity getById(long telegramChatId) {
        db.connect();
        foundedUser = null;
        PreparedStatement statement = db.getPreparedStatement(TEMPLATE_SQL_QUERY_SELECT_USER_BY_TELEGRAM_CHAT_ID);
        try {
            statement.setLong(1, telegramChatId);
            resultSet = statement.executeQuery();
                foundedUser = getUserFromResultSetOrNull();
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        db.close();
        if(foundedUser==null){
            System.out.println("SQL запрос не вернул результата по telegram_chat_id ="+ telegramChatId);
        }
        return foundedUser;
    }
    private UserEntity getUserFromResultSetOrNull() throws SQLException {
        if(resultSet.next()){
            return buildUserFromResultSet();
        }
        return null;
    }

    @Override
    public boolean insert(UserEntity user) {
        if(isNewUser(user.getTelegramChatId())){
            //сделай новую запись в БД
            db.connect();
            PreparedStatement statement = db.getPreparedStatement(TEMPLATE_SQL_QUERY_INSERT_USER);
            try {
                statement.setLong(1, user.getTelegramChatId());
                statement.setString(2, user.getUsername());
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
            db.close();
            return true;
        }
        System.out.println("пользователь уже есть в Базе");
        return false;
    }
    public boolean isNewUser(long telegramChatId) {
        UserEntity request = getById(telegramChatId);
        return request == null;
    }

    @Override
    public boolean update(UserEntity object) {
        return false;
    }

    @Override
    public boolean delete(UserEntity object) {
        return false;
    }
}
