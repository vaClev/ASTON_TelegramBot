package DBBehavirTests;

import org.example.JDBC.DBActions.DBAddressBehavior;
import org.example.JDBC.DBEntities.Address;

import java.util.UUID;

public class DBAddressBehaviorTests {
    public static void main(String[] args) {
        showByIDTest("743066d7-a965-442e-a6db-78a814511e48");
        isNew("moscow");
        insertTest();
    }
    public static void showByIDTest(String StringId)
    {
        UUID id = UUID.fromString(StringId);
        var object = new DBAddressBehavior().getById(id);
        System.out.println(object);
    }
    public static void isNew(String addressText)
    {
        boolean isExists = new DBAddressBehavior().isNewAddress(addressText);
        System.out.println("check: "+addressText +" "+ isExists);
    }
    public static void insertTest()
    {
        Address address = new Address("minsk");
        System.out.println(address);
        isNew(address.getText());

        boolean isInsert = new DBAddressBehavior().insert(address);
        System.out.println("insert: "+isInsert);
    }
}
