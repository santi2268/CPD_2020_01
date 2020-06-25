package sk.fri.uniza;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import sk.fri.uniza.api.WeatherStationService;
import sk.fri.uniza.model.WeatherData;

import java.io.IOException;
import java.util.List;

public class IotNode {
    private final Retrofit retrofit;
    private final WeatherStationService weatherStationService;

    public IotNode() {

        retrofit = new Retrofit.Builder()
                //načtení adresy služby
                .baseUrl("http://ip172-18-0-9-bqv3gslim9m000b5maf0-9000.direct.labs.play-with-docker.com/")
                // Na konvertovanie JSON objektu na java POJO použijeme
                // Jackson knižnicu
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        // Vytvorenie inštancie komunikačného rozhrania
        weatherStationService = retrofit.create(WeatherStationService.class);

    }

    public WeatherStationService getWeatherStationService() {
        return weatherStationService;
    }

    public double getAverageTemperature(String station, String from, String to) {
        double calc = 0;
        int sum = 0;
        Call<List<WeatherData>> allTemperatures = weatherStationService.getHistoryWeather(station, from, to, List.of("airTemperature"));
        try {
            Response<List<WeatherData>> response = allTemperatures.execute();

            if (response.isSuccessful()) { // Dotaz na server bol neúspešný
                //Získání údajů ze třídy WeatherData - seznam lokací
                List<WeatherData> body = response.body();

                int i = 0;
                while (i < body.size()){
                    i++;
                    calc = calc + Double.parseDouble(String.valueOf(body.get(i).getAirTemperature()));
                    sum = i;
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        double average = calc/sum;
        return average;
    }
}