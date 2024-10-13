package org.example.DBActions;

import org.example.DBEntities.DBUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBuserBehavior implements DBBehavior<DBUser> {
    private final DBHelper db = new DBHelper();
    private ResultSet resultSet;
    private DBUser foundedUser;
    private ArrayList<DBUser> users;

    @Override
    public ArrayList<DBUser> getAll() {
        users = new ArrayList<>();
        resultSet = db.getResultSet("""
                SELECT id, telegram_chat_id, username
                FROM users"""
        );
        try {
            collectUsers();
        } catch (SQLException ignored) {
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
        if(isEmpty) System.out.println("Таблица Members пустая");
    }
    private DBUser buildUserFromResultSet() throws SQLException {
        return new DBUser(
                resultSet.getLong("id"),
                resultSet.getLong("telegram_chat_id"),
                resultSet.getString("username")
        );
    }

    @Override
    public DBUser getById(String id) {
        return null;
    }

    @Override
    public boolean add(DBUser object) {
        return false;
    }

    @Override
    public boolean update(DBUser object) {
        return false;
    }

    @Override
    public boolean delete(DBUser object) {
        return false;
    }
}
