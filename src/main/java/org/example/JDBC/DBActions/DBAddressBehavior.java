package org.example.JDBC.DBActions;

import org.example.JDBC.DBEntities.Address;
import org.postgresql.util.PGobject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class DBAddressBehavior implements DBBehavior<Address, UUID> {
    private final DBHelper db = new DBHelper();
    private ResultSet resultSet;
    private Address foundedAddress;
    private ArrayList<Address> addresses;
    private static final String TEMPLATE_SQL_QUERY_SELECT_ADDRESS_BY_ID =
            "SELECT id, address_text FROM addresses WHERE id=?;";
    private static final String TEMPLATE_SQL_QUERY_IS_EXIST_BY_ADDRESS_TEXT =
            "SELECT COUNT(1) FROM addresses WHERE address_text = ?;";
    private static final String TEMPLATE_SQL_QUERY_INSERT_ADDRESS =
            "INSERT INTO addresses (id, address_text) VALUES (gen_random_uuid(),?)";
    private static final String TEMPLATE_SQL_QUERY_SELECT_ID_BY_ADDRESS =
            "SELECT id, address_text FROM addresses WHERE address_text=?;";

    //TODO getAll
    @Override
    public ArrayList<Address> getAll() {
        return null;
    }

    @Override
    public Address getById(UUID id) {
        if (id == null) return null;
        db.connect();
        foundedAddress = null;
        PreparedStatement statement = db.getPreparedStatement(TEMPLATE_SQL_QUERY_SELECT_ADDRESS_BY_ID);
        try {
            PGobject toInsertUUID = new PGobject();
            toInsertUUID.setType("uuid");
            toInsertUUID.setValue(id.toString());
            statement.setObject(1, toInsertUUID);
            resultSet = statement.executeQuery();
            foundedAddress = getAddressFromResultSetOrNull();
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage() + Arrays.toString(e.getStackTrace()));
        }
        db.close();
        if (foundedAddress == null) {
            System.out.println("SQL запрос не вернул результата по id =" + id);
        }
        return foundedAddress;
    }
    private Address getAddressFromResultSetOrNull() throws SQLException {
        if (resultSet.next()) {
            return buildAddressFromResultSet();
        }
        return null;
    }
    private Address buildAddressFromResultSet() throws SQLException {
        return new Address(
                UUID.fromString(resultSet.getString("id")),
                resultSet.getString("address_text")
        );
    }

    @Override
    public boolean insert(Address address) {
        if(isNewAddress(address.getText())){
            db.connect();
            PreparedStatement statement = db.getPreparedStatement(TEMPLATE_SQL_QUERY_INSERT_ADDRESS);
            try {
                statement.setString(1, address.getText());
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
            db.close();
            return true;
        }
        System.out.println("попытка добавить уже существующий адресс");
        return false;
    }
    public boolean isNewAddress(String addressText) {
        if (addressText == null) return false;
        db.connect();
        int answer = 0;
        PreparedStatement statement = db.getPreparedStatement(TEMPLATE_SQL_QUERY_IS_EXIST_BY_ADDRESS_TEXT);
        try {
            statement.setString(1, addressText.toLowerCase());
            resultSet = statement.executeQuery();
                if (resultSet.next()) answer = resultSet.getByte("count");
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage() + Arrays.toString(e.getStackTrace()));
        }
        db.close();
        return answer == 0;
    }

    //TODO update and delete
    @Override
    public boolean update(Address object) {
        return false;
    }

    @Override
    public boolean delete(Address object) {
        return false;
    }

    public UUID getIdByAddressText(String addressText) {
        if (addressText == null) return null;
        db.connect();
        UUID foundedID;
        PreparedStatement statement = db.getPreparedStatement(TEMPLATE_SQL_QUERY_SELECT_ID_BY_ADDRESS);
        try {
            statement.setObject(1, addressText);
            resultSet = statement.executeQuery();
                foundedID = getIdFromResultSetOrNull();
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage() + Arrays.toString(e.getStackTrace()));
        }
        db.close();
        if (foundedID == null) {
            System.out.println("SQL запрос не вернул результата по address_text =" + addressText);
        }
        return foundedID;
    }
    private UUID getIdFromResultSetOrNull() throws SQLException {
        if (resultSet.next()) {
            return UUID.fromString(resultSet.getString("id"));
        }
        return null;
    }
}
