import org.example.DBActions.DBuserBehavior;
import org.example.DBEntities.DBUser;

import java.util.ArrayList;

public class DBTests {
    public static void main(String[] args) {
        ArrayList<DBUser> userList =  new DBuserBehavior().getAll();
        System.out.println(userList.get(0));
    }
}
