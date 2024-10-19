package DBBehavirTests;

import org.example.JDBC.DBActions.DBRequestsResponses;
import org.example.JDBC.DBActions.DBUserBehavior;
import org.example.JDBC.DBEntities.RequestResponseEntry;
import org.example.JDBC.DBEntities.UserEntity;

import java.util.LinkedList;
import java.util.UUID;

public class DBTestsRequestResponseEntry {
    public static void main(String[] args) {
        //showByIDTest("de2cb0ac-efba-42b4-a439-d316bc97c9d6");
        //showByIDTest("71e5070f-4c9e-40e0-b078-bbbdca45d2b4");
        //insertTest();
        //showEqualResponse(6,"moscow");
        trySetAnswerFromDB_last6hours("москва");
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
        RequestResponseEntry rr = new RequestResponseEntry("Нижний Новгород", "answer NN", user);
        new DBRequestsResponses().insert(rr);
    }
    public static void showEqualResponse(int hours, String request)
    {
        LinkedList<String> equalResponse = new DBRequestsResponses().getEqualResponses(hours,request);
        System.out.println(equalResponse.toString());
    }
    public static boolean trySetAnswerFromDB_last6hours(String userMessage) {
        LinkedList<String> historyAnswers = new DBRequestsResponses().getEqualResponses(6, userMessage);
        if (historyAnswers == null) {
            return false;
        }
        for (var historyAnswer : historyAnswers) {
            if (historyAnswer.toUpperCase().contains("#Меня уже спрашивали".toUpperCase())) {
                continue;
            }
            System.out.println(historyAnswer);
            return true;
        }
        return false;
    }
}
