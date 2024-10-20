package org.example.JDBC.DBActions;

import org.example.JDBC.DBEntities.RequestResponseEntry;
import org.example.JDBC.DBEntities.UserEntity;
import org.postgresql.util.PGobject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

public class DBRequestsResponses implements DBBehavior<RequestResponseEntry, UUID> {
    private final DBHelper db = new DBHelper();
    private ResultSet resultSet;
    private RequestResponseEntry foundedEntry;
    private ArrayList<UserEntity> entries;
    private static final String SQL_QUERY_SELECT_ALL_ENTRIES =
            "SELECT id, datetime, request_text, response_text, userid FROM requests_and_responses;";
    private static final String TEMPLATE_SQL_QUERY_SELECT_ENTRY_BY_ID =
            "SELECT id, datetime, addressid, response_text, userid FROM requests_and_responses WHERE id=?;";
    private static final String TEMPLATE_SQL_QUERY_INSERT_REQUEST_RESPONSE_ENTRY = """
            INSERT INTO requests_and_responses (id, datetime, addressid, response_text, userid)
            VALUES (gen_random_uuid(), ?, ?, ?, ?);""";
    private static final String TEMPLATE_SQL_QUERY_SELECT_EQUALS_RESPONSE = """
            SELECT datetime, response_text
            FROM requests_and_responses
            WHERE datetime >= ? and addressid =?;
            """;

    //TODO
    @Override
    public ArrayList<RequestResponseEntry> getAll() {
        return null;
    }
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

    @Override
    public boolean insert(RequestResponseEntry entry) {
        if (isNewEntry(entry.getId())) {
            UUID addressUUID = getAddressIdFromDBorNull(entry.getRequestText());
            if(addressUUID==null) return false;

            //сделай новую запись в БД
            db.connect();
            PreparedStatement statement = db.getPreparedStatement(TEMPLATE_SQL_QUERY_INSERT_REQUEST_RESPONSE_ENTRY);
            try {
                statement.setTimestamp(1, entry.getDatetimeAsTimestamp());

                PGobject toInsertUUID = new PGobject();
                toInsertUUID.setType("uuid");
                toInsertUUID.setValue(addressUUID.toString());
                statement.setObject(2, toInsertUUID);

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
    private UUID getAddressIdFromDBorNull(String addressText) {
        UUID addressUUID = new DBAddressBehavior().getIdByAddressText(addressText);
        if (addressUUID == null) {
            System.out.println("Адресс "+ addressText+"не найден в таблице addresses");
        }
        return addressUUID;
    }
    private boolean isNewEntry(UUID id) {
        if (id == null) return true;

        RequestResponseEntry request = getById(id);
        return request == null;
    }

    //TODO
    @Override
    public boolean update(RequestResponseEntry object) {
        return false;
    }

    //TODO
    @Override
    public boolean delete(RequestResponseEntry object) {
        return false;
    }



    //вернет ответ на тот же запрос за последние х часов --- если он есть в базе
    public LinkedList<String> getEqualResponses(int hours, String requestText) {
        UUID addressID = getAddressIdFromDBorNull(requestText);
        if(addressID==null) return null;

        db.connect();
        LinkedList<String> responses;
        LocalDateTime beforeNow = LocalDateTime.now().minusHours(hours);
        Timestamp timestamp = Timestamp.valueOf(beforeNow);
        PreparedStatement statement = db.getPreparedStatement(TEMPLATE_SQL_QUERY_SELECT_EQUALS_RESPONSE);
        try {
            statement.setTimestamp(1, timestamp);
                PGobject toInsertUUID = new PGobject();
                toInsertUUID.setType("uuid");
                toInsertUUID.setValue(addressID.toString());
                statement.setObject(2, toInsertUUID);
            resultSet = statement.executeQuery();
                responses = getResponseFromResultSet();
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        db.close();
        return responses;
    }
    private LinkedList<String> getResponseFromResultSet() throws SQLException {
        LinkedList<String> lastTimeResponses = new LinkedList<>();
        while (resultSet.next()) {
            LocalDateTime dateTime = resultSet.getTimestamp("datetime").toLocalDateTime();
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("HH:mm:ss");
            String stringTime = dateTime.format(pattern);

            lastTimeResponses.add(stringTime + " " +resultSet.getString("response_text"));
        }
        return lastTimeResponses;
    }
}
