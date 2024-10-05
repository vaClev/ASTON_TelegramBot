import org.example.weather.OpenMeteoRequester;

public class OpenMeteoTests {
    public static void main(String[] args) {
        OpenMeteoRequester openMeteoRequester = new OpenMeteoRequester("56.2414","43.4554");
        openMeteoRequester.getWeatherDescription();
    }
}
