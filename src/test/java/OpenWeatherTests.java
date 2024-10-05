import org.example.weather.badAPIServices.OpenWeatherRequest;

public class OpenWeatherTests {
    public static void main(String[] args) {
        Test1();
    }

    public static void Test1() {
        String URLstring = "https://api.open-meteo.com/v1/forecast?latitude=51.5085&longitude=-0.1257&daily=temperature_2m_max,temperature_2m_min&timezone=GMT&forecast_days=1";
        OpenWeatherRequest openWeatherRequest = new OpenWeatherRequest();

        try {
            String RequestText = openWeatherRequest.getHTML(URLstring);
            System.out.println(RequestText);
        } catch (Exception e) {
            System.out.println("error");
        }
    }
    public static void Test2(){
        OpenWeatherRequest openWeatherRequest = new OpenWeatherRequest();
        openWeatherRequest.getWeatherNowStringDescription("London");
    }
}
