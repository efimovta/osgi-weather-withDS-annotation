package edu.efimovta.liferay.osgi.weather.printer.service.impl;

import edu.efimovta.liferay.osgi.weather.dto.Weather;
import edu.efimovta.liferay.osgi.weather.printer.service.WeatherPrinter;
import edu.efimovta.liferay.osgi.weather.printer.service.WeatherPrinterException;
import org.osgi.service.component.annotations.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by eta on 7/5/2017.
 */
@Component(immediate = true)
public class FileWeatherPrinter implements WeatherPrinter {
    private final String path = "..//..//testFile2.txt";

    public void print(Weather weather) throws WeatherPrinterException {
        try {
            FileOutputStream out = new FileOutputStream(path);

            StringBuilder sb = new StringBuilder();

            sb.append("~~~~~~~~~~# " + weather.getSource() + " #~~~~~~~~~~");
            sb.append("\nCity: ").append(weather.getCity());
            sb.append("\ncountry: ").append(weather.getCountry());
            sb.append("\nlat, lon: ").append(weather.getLat()).append(", ").append(weather.getLon());
            sb.append("\nconditionText: ").append(weather.getCondition());
            sb.append("\navgtemp_c: ").append(weather.getAvgTemp());
            sb.append("\nmintemp_c: ").append(weather.getMinTemp());
            sb.append("\nmaxtemp_c: ").append(weather.getMaxTemp());
            sb.append("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            out.write(sb.toString().getBytes());
            out.close();
        } catch (IOException e) {
            throw new WeatherPrinterException(e);
        }

    }

    @Override
    public void print(List<Weather> weathers) throws WeatherPrinterException {
            try {
                FileOutputStream out = new FileOutputStream(path);
                StringBuilder sb = new StringBuilder();

                for (Weather weather : weathers) {
                    sb.append("\n~~~~~~~~~~# " + weather.getSource() + " #~~~~~~~~~~");
                    sb.append("\nCity: ").append(weather.getCity());
                    sb.append("\ncountry: ").append(weather.getCountry());
                    sb.append("\nlat, lon: ").append(weather.getLat()).append(", ").append(weather.getLon());
                    sb.append("\nconditionText: ").append(weather.getCondition());
                    sb.append("\navgtemp_c: ").append(weather.getAvgTemp());
                    sb.append("\nmintemp_c: ").append(weather.getMinTemp());
                    sb.append("\nmaxtemp_c: ").append(weather.getMaxTemp());
                    sb.append("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
                }

                out.write(sb.toString().getBytes());
                System.out.println("FILE PRINTED");
                out.close();
            } catch (IOException e) {
                throw new WeatherPrinterException(e);
            }
    }
}
