import org.example.DBActions.DBUserBehavior;
import org.example.DBEntities.UserEntity;

import java.util.ArrayList;

public class DBTestsUser {
    public static void main(String[] args) {
        getAllUsersTest();
        getAllUserByTelegramChatId(1386864283);
        insertUser();
    }
    public static void getAllUsersTest()
    {
        ArrayList<UserEntity> userList =  new DBUserBehavior().getAll();
        for (var user: userList)
        {
            System.out.println(user);
        }
    }
    public static void getAllUserByTelegramChatId(long telegramChatId)
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
