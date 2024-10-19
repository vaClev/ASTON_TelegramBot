package DBBehavirTests;

import org.example.JDBC.DBActions.DBUserBehavior;
import org.example.JDBC.DBEntities.UserEntity;

import java.util.ArrayList;

public class DBTestsUser {
    public static void main(String[] args) {
        //getAllUsersTest();
        //getUserByTelegramChatId(1386864283);
        insertUser();
        getAllUsersTest();
    }
    public static void getAllUsersTest()
    {
        ArrayList<UserEntity> userList =  new DBUserBehavior().getAll();
        for (var user: userList)
        {
            System.out.println(user);
        }
    }
    public static void getUserByTelegramChatId(long telegramChatId)
    {
        UserEntity user =  new DBUserBehavior().getById(telegramChatId);
        System.out.println(user);
    }
    public static void insertUser()
    {
        UserEntity tester = new UserEntity(2, 654654L, "tester");
        boolean isInserted = new DBUserBehavior().insert(tester);
        System.out.println("isInserted =" + isInserted);
    }
}
