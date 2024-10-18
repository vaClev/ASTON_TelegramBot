package org.example.DBActions;

import org.example.DBEntities.RequestResponseEntry;
import org.example.DBEntities.UserEntity;
import org.postgresql.util.PGobject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class DBRequestsResponses implements DBBehavior<RequestResponseEntry, UUID> {
    private final DBHelper db = new DBHelper();
    private ResultSet resultSet;
    private RequestResponseEntry foundedEntry;
    private ArrayList<UserEntity> entries;
    private static final String SQL_QUERY_SELECT_ALL_ENTRIES =
            "SELECT id, datetime, request_text, response_text, userid FROM requests_and_responses;";
    private static final String TEMPLATE_SQL_QUERY_SELECT_ENTRY_BY_ID =
            "SELECT id, datetime, addressid, response_text, userid FROM requests_and_responses2 WHERE id=?;";
    private static final String TEMPLATE_SQL_QUERY_INSERT_REQUEST_RESPONSE_ENTRY = """
            INSERT INTO requests_and_responses (id, datetime, request_text, response_text, userid)
            VALUES (gen_random_uuid(), ?, ?, ?, ?);""";
    private static final String TEMPLATE_SQL_QUERY_SELECT_EQUALS_RESPONSE = """
            SELECT datetime, response_text
            FROM requests_and_responses
            WHERE datetime >= ? and request_text =?;
            """;

    @Override
    public ArrayList<RequestResponseEntry> getAll() {
        return null;
    }

    //TODO переисать класс на работу с таблицей
    /*CREATE TABLE requests_and_responses2
            (
                    id uuid NOT NULL,
                    datetime timestamp NOT NULL,
                    addressid uuid NOT NULL,
                    response_text character varying(500) NOT NULL,
                    userid bigint NOT NULL,
                    FOREIGN KEY (userid) REFERENCES users(telegram_chat_id) ON DELETE CASCADE,
                    FOREIGN KEY (addressid) REFERENCES addresses(id) ON DELETE CASCADE,
                    PRIMARY KEY ( datetime, userid)
            );*/
    private RequestResponseEntry buildRequestResponseEntryFromResultSet() throws SQLException {
        UUID id = UUID.fromString(resultSet.getString("id"));

        String addressId = resultSet.getString("addressid");
        UUID addressUUID = UUID.fromString(addressId);
        String address_text = new DBAddressBehavior().getById(addressUUID).getText();
        return new RequestResponseEntry(
                id,
                resultSet.getTimestamp("datetime"),
                address_text,
                resultSet.getString("response_text"),
                resultSet.getLong("userid")
        );
    }

    @Override
    public RequestResponseEntry getById(UUID id) {
        db.connect();
        foundedEntry = null;
        PreparedStatement statement = db.getPreparedStatement(TEMPLATE_SQL_QUERY_SELECT_ENTRY_BY_ID);
        try {
            PGobject toInsertUUID = new PGobject();
            toInsertUUID.setType("uuid");
            toInsertUUID.setValue(id.toString());
            statement.setObject(1, toInsertUUID);
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
        if (resultSet.next()) {
            return buildRequestResponseEntryFromResultSet();
        }
        return null;
    }

    //TODO переисать на работу с новой таблицей
    @Override
    public boolean insert(RequestResponseEntry entry) {
        if (isNewEntry(entry.getId())) {
            //сделай новую запись в БД
            db.connect();
            PreparedStatement statement = db.getPreparedStatement(TEMPLATE_SQL_QUERY_INSERT_REQUEST_RESPONSE_ENTRY);
            try {
                statement.setTimestamp(1, entry.getDatetimeAsTimestamp());
                statement.setString(2, entry.getRequestText());
                statement.setString(3, entry.getResponseText());
                statement.setLong(4, entry.getUserId());
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
    private boolean isNewEntry(UUID id) {
        if (id == null) return true;

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


    //TODO переисать на работу с новой таблицей
    //вернет ответ на тот же запрос за последние х часов --- если он есть в базе
    public String getEqualResponse(int hours, String requestText) {
        db.connect();
        String response = null;
        LocalDateTime beforeNow = LocalDateTime.now().minusHours(hours);
        Timestamp timestamp = Timestamp.valueOf(beforeNow);
        PreparedStatement statement = db.getPreparedStatement(TEMPLATE_SQL_QUERY_SELECT_EQUALS_RESPONSE);
        try {
            statement.setTimestamp(1, timestamp);
            statement.setString(2, requestText);
            resultSet = statement.executeQuery();
                response = getResponseFromResultSet();
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        db.close();
        return response;
    }
    private String getResponseFromResultSet() throws SQLException {
        if(resultSet.next())
        {
            LocalDateTime dateTime = resultSet.getTimestamp("datetime").toLocalDateTime();
            DateTimeFormatter pattern =  DateTimeFormatter.ofPattern("HH:mm:ss");
            String stringTime = dateTime.format(pattern);

            return stringTime+" "+
                   resultSet.getString("response_text");
        }
        return null;
    }
}
