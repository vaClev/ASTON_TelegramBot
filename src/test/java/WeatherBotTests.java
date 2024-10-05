import org.example.weather.GeoCoordinates;
import org.example.weather.OpenMeteoRequester;
import org.example.weather.YandexGeoCoderRequester;

public class WeatherBotTests {
    public static void main(String[] args) {
        String userMessage = "Moscow";
        String answer =  getAnswer(userMessage);
        System.out.println(userMessage+" "+answer);
    }
    static String getAnswer(String userMessage)
    {
        YandexGeoCoderRequester yandex = new YandexGeoCoderRequester();
        GeoCoordinates coord = yandex.getCoordinatesByAddress(userMessage);
        if(coord.getLatitude().equals("0") && coord.getLongitude().equals("0"))
        {
            return "не могу найти это место на карте";
        }
        OpenMeteoRequester openMeteoRequester = new OpenMeteoRequester(coord.getLatitude(),coord.getLongitude());
        return openMeteoRequester.getWeatherDescription();
    }
}
