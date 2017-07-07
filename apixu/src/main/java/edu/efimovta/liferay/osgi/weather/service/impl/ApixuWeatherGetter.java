package edu.efimovta.liferay.osgi.weather.service.impl;

import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import edu.efimovta.liferay.osgi.weather.dto.Weather;
import edu.efimovta.liferay.osgi.weather.dto.impl.WeatherImpl;
import edu.efimovta.liferay.osgi.weather.service.WeatherGetter;
import edu.efimovta.liferay.osgi.weather.service.WeatherGetterException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.osgi.service.component.annotations.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.apache.http.HttpHeaders.USER_AGENT;

/**
 * Created by eta on 7/5/2017.
 */
@Component(immediate = true)
public class ApixuWeatherGetter implements WeatherGetter {
    private final String source = "apixu.com - json";
    private final String sourceUrl = "http://api.apixu.com/v1/forecast.json";
    double lon = 59.89;
    double lan = 30.26;
    String key = "3579985690364f38a1030507170407";
    String date = "2017-07-05";

    private String getJsonFromSource() throws IOException {
        HttpClient client = HttpClientBuilder.create().build();

        String url = sourceUrl + "?key=" + key + "&q=" + lon + "," + lan + "&dt=" + date;
        HttpGet request = new HttpGet(url);

        request.addHeader("User-Agent", USER_AGENT);
        HttpResponse response = client.execute(request);

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuilder result = new StringBuilder();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    public Weather get() throws WeatherGetterException {
        try {
            String json = getJsonFromSource();
            JSONObject root = JSONFactoryUtil.createJSONObject(json);
            JSONObject location = root.getJSONObject("location");
            JSONObject day = root.getJSONObject("forecast")
                    .getJSONArray("forecastday").getJSONObject(0)
                    .getJSONObject("day");
            JSONObject condition = day.getJSONObject("condition");

            String city = location.getString("name");
            String country = location.getString("country");
            double lat = location.getDouble("lat");
            double lon = location.getDouble("lon");
            long localtime_epoch = location.getLong("localtime_epoch");
            String conditionText = condition.getString("text");
            double avgtemp_c = day.getDouble("avgtemp_c");
            double mintemp_c = day.getDouble("mintemp_c");
            double maxtemp_c = day.getDouble("maxtemp_c");


            System.out.println("WEATHER GETTED");
            return new WeatherImpl(source, city, country, lat, lon, conditionText, avgtemp_c, mintemp_c, maxtemp_c);

        } catch (JSONException | IOException e) {
            throw new WeatherGetterException(e);
        }
    }
}
