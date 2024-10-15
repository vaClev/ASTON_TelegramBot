package org.example.DBActions;

import org.example.DBEntities.RequestResponseEntry;
import org.example.DBEntities.UserEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBRequestsResponses implements DBBehavior<RequestResponseEntry> {
    private final DBHelper db = new DBHelper();
    private ResultSet resultSet;
    private RequestResponseEntry foundedEntry;
    private ArrayList<UserEntity> entries;
    private static final String SQL_QUERY_SELECT_ALL_ENTRIES = "SELECT id, datetime, request_text, response_text, userid FROM requests_and_responses;";
    private static final String TEMPLATE_SQL_QUERY_SELECT_ENTRY_BY_ID = "SELECT id, datetime, request_text, response_text, userid FROM requests_and_responses WHERE id=?;";
    private static final String TEMPLATE_SQL_QUERY_INSERT_REQUEST_RESPONSE_ENTRY = """
            INSERT INTO requests_and_responses (id, datetime, request_text, response_text, userid)
            VALUES (?, ?, ?, ?, ?);""";

    @Override
    public ArrayList<RequestResponseEntry> getAll() {
        return null;
    }
    private RequestResponseEntry buildRequestResponseEntryFromResultSet() throws SQLException {
        return new RequestResponseEntry(
                resultSet.getString("id"),
                resultSet.getTimestamp("datetime"),
                resultSet.getString("request_text"),
                resultSet.getString("response_text"),
                resultSet.getLong("userid")
        );
    }

    @Override
    public RequestResponseEntry getById(long id) {
        return null;
    }
    public RequestResponseEntry getById(String id) {
        db.connect();
        foundedEntry = null;
        PreparedStatement statement = db.getPreparedStatement(TEMPLATE_SQL_QUERY_SELECT_ENTRY_BY_ID);
        try {
            statement.setString(1, id);
            resultSet = statement.executeQuery();
            foundedEntry = getEntryFromResultSetOrNull();
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        db.close();
        if (foundedEntry == null) {
            System.out.println("SQL запрос не вернул результата по id =" + id);
        }
        return foundedEntry;
    }
    private RequestResponseEntry getEntryFromResultSetOrNull() throws SQLException {
        if(resultSet.next()){
            return buildRequestResponseEntryFromResultSet();
        }
        return null;
    }


    @Override
    public boolean insert(RequestResponseEntry entry) {
        if(isNewEntry(entry.getId())){
            //сделай новую запись в БД
            db.connect();
            PreparedStatement statement = db.getPreparedStatement(TEMPLATE_SQL_QUERY_INSERT_REQUEST_RESPONSE_ENTRY);
            try {
                statement.setString(1, entry.getId());
                statement.setTimestamp(2, entry.getDatetimeAsTimestamp());
                statement.setString(3, entry.getRequestText());
                statement.setString(4, entry.getResponseText());
                statement.setLong(5, entry.getUserId());
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
            db.close();
            return true;
        }
        return false;
    }
    private boolean isNewEntry(String id) {
        RequestResponseEntry request = getById(id);
        return request == null;
    }

    @Override
    public boolean update(RequestResponseEntry object) {
        return false;
    }

    @Override
    public boolean delete(RequestResponseEntry object) {
        return false;
    }
}
