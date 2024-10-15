import org.example.DBActions.DBRequestsResponses;
import org.example.DBActions.DBUserBehavior;
import org.example.DBEntities.RequestResponseEntry;
import org.example.DBEntities.UserEntity;

public class DBTestsRequestResponseEntry {
    public static void main(String[] args) {
        showByIDTest("2004-10-19 10:23:54+02 1386864283");
        insertTest();
    }
    public static void showByIDTest(String id)
    {
        RequestResponseEntry RR = new DBRequestsResponses().getById(id);
        System.out.println(RR);
    }

    public static void insertTest()
    {
        UserEntity user = new DBUserBehavior().getById(1386864283);
        RequestResponseEntry rr = new RequestResponseEntry("hello", "answer", user);
        new DBRequestsResponses().insert(rr);
    }
}
