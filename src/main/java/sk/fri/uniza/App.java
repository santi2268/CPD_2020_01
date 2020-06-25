package sk.fri.uniza;

import retrofit2.Call;
import retrofit2.Response;
import sk.fri.uniza.model.Location;
import sk.fri.uniza.model.WeatherData;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class App {
    public static void main(String[] args) {
        IotNode iotNode = new IotNode();

        Call<Map<String, String>> currentWeather =
                iotNode.getWeatherStationService()
                        .getCurrentWeatherAsMap("station_1",
                                List.of("time", "date",
                                        "airTemperature"));


        try {
            // Odoslanie požiadavky na server pomocou REST rozhranie
            Response<Map<String, String>> response = currentWeather.execute();

            if (response.isSuccessful()) { // Dotaz na server bol neúspešný
                //Získanie údajov vo forme Mapy stringov
                Map<String, String> body = response.body();
                System.out.println("aktualni data ze stanice 1:");
                System.out.println(body);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Vytvorenie požiadavky na získanie údajov o všetkých meteo staniciach
        Call<List<Location>> stationLocations =
                iotNode.getWeatherStationService().getStationLocations();

        try {
            Response<List<Location>> response = stationLocations.execute();

            if (response.isSuccessful()) { // Dotaz na server bol neúspešný
                //Získanie údajov vo forme Zoznam lokacií
                List<Location> body = response.body();
                System.out.println("pozice stanice 1:");
                System.out.println(body);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Vytvorenie požiadavky na získanie údajov o aktuálnom počasí z
        // meteo stanice s ID: station_1
        Call<WeatherData> currentWeatherPojo =
                iotNode.getWeatherStationService()
                        .getCurrentWeather("station_1");


        try {
            // Odoslanie požiadavky na server pomocou REST rozhranie
            Response<WeatherData> response = currentWeatherPojo.execute();

            if (response.isSuccessful()) { // Dotaz na server bol neúspešný
                //Získanie údajov vo forme inštancie triedy WeatherData
                WeatherData body = response.body();
                System.out.println("Aktualni pocasi ze stanice 1 (all):");
                System.out.println(body);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Call<List<WeatherData>> historyWeatherPojo =
                iotNode.getWeatherStationService()
                        .getHistoryWeather("station_1", "22/03/2020 15:00", "11/04/2020 15:00");

        try {
            // Odoslanie požiadavky na server pomocou REST rozhranie
            Response<List<WeatherData>> response = historyWeatherPojo.execute();

            if (response.isSuccessful()) { // Dotaz na server bol neúspešný
                //Získanie údajov vo forme inštancie triedy WeatherData
                List<WeatherData> body = response.body();
                System.out.println(body);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        Call<List<WeatherData>> historyWeatherWithFields =
                iotNode.getWeatherStationService()
                        .getHistoryWeather("station_1", "22/03/2020 15:00", "11/04/2020 15:00", List.of("time", "date",
                                "airTemperature"));

        try {
            //Odeslání požadavku na server
            Response<List<WeatherData>> response = historyWeatherWithFields.execute();

            if (response.isSuccessful()) { // Dotaz na server bol neúspešný
                //Získanie údajov vo forme inštancie triedy WeatherData
                List<WeatherData> body = response.body();
                System.out.println("Historie v zadaném intervalu:");
                System.out.println(body);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        double a = iotNode.getAverageTemperature("station_1", "22/03/2020 15:00", "11/04/2020 15:00");
        System.out.println("Prumerna teplota je: " + a);
    }
}