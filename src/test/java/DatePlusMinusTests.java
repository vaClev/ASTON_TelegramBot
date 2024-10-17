import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatePlusMinusTests {
    public static void main(String[] args) {
        //plusMinusTest();
        showTimeFromTimeStamp();
    }
    public static void plusMinusTest(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beforeNow6 = now.minusHours(6);

        System.out.println(now);
        System.out.println(beforeNow6);

        Timestamp timestamp = Timestamp.valueOf(beforeNow6);
    }
    public static void showTimeFromTimeStamp()
    {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        DateTimeFormatter pattern =  DateTimeFormatter.ofPattern("HH:mm:ss");
        String stringTime = timestamp.toLocalDateTime().format(pattern);
        System.out.println(stringTime);
    }
}
