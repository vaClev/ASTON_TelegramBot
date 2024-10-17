import org.example.DBActions.DBRequestsResponses;
import org.example.DBActions.DBUserBehavior;
import org.example.DBEntities.RequestResponseEntry;
import org.example.DBEntities.UserEntity;

import java.util.UUID;

public class DBTestsRequestResponseEntry {
    public static void main(String[] args) {
        //showByIDTest("7fb75247-83b0-43d5-90f7-ca3cb07f7ff4");
        //insertTest();
        showEqualResponse(6,"moscow");
    }
    public static void showByIDTest(String StringId)
    {
        UUID id = UUID.fromString(StringId);
        RequestResponseEntry RR = new DBRequestsResponses().getById(id);
        System.out.println(RR);
    }
    public static void insertTest()
    {
        UserEntity user = new DBUserBehavior().getById(1386864283L);
        RequestResponseEntry rr = new RequestResponseEntry("hello", "answer", user);
        new DBRequestsResponses().insert(rr);
    }
    public static void showEqualResponse(int hours, String request)
    {
        String equalResponse = new DBRequestsResponses().getEqualResponse(hours,request);
        System.out.println(equalResponse);
    }
}
