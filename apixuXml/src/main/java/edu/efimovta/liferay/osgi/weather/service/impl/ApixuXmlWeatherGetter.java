package edu.efimovta.liferay.osgi.weather.service.impl;

import edu.efimovta.liferay.osgi.weather.dto.Weather;
import edu.efimovta.liferay.osgi.weather.dto.impl.WeatherImpl;
import edu.efimovta.liferay.osgi.weather.service.WeatherGetter;
import edu.efimovta.liferay.osgi.weather.service.WeatherGetterException;
import org.osgi.service.component.annotations.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by eta on 7/5/2017.
 */
@Component(immediate = true)
public class ApixuXmlWeatherGetter implements WeatherGetter {
    private final String source = "apixu.com - xml";
    private final String sourceUrl = "http://api.apixu.com/v1/forecast.xml";
    double lon = 59.89;
    double lan = 30.26;
    String key = "3579985690364f38a1030507170407";
    String date = "2017-07-05";

    public Weather get() throws WeatherGetterException {
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            String url = sourceUrl + "?key=" + key + "&q=" + lon + "," + lan + "&dt=" + date;
            Document document = builder.parse(url);
            Element root = document.getDocumentElement();

            Element location = (Element) root.getElementsByTagName("location").item(0);
            Element forecast = (Element) root.getElementsByTagName("forecast").item(0);
            Element forecastday = (Element) forecast.getElementsByTagName("forecastday").item(0);
            Element day = (Element) forecastday.getElementsByTagName("day").item(0);
            Element condition = (Element) day.getElementsByTagName("condition").item(0);

            String city = location.getElementsByTagName("name").item(0).getTextContent();
            String country = location.getElementsByTagName("country").item(0).getTextContent();
            double lat = Double.parseDouble(location.getElementsByTagName("lat").item(0).getTextContent());
            double lon = Double.parseDouble(location.getElementsByTagName("lon").item(0).getTextContent());
            String conditionText = condition.getElementsByTagName("text").item(0).getTextContent();
            double avgtemp_c = Double.parseDouble(day.getElementsByTagName("avgtemp_c").item(0).getTextContent());
            double mintemp_c = Double.parseDouble(day.getElementsByTagName("mintemp_c").item(0).getTextContent());
            double maxtemp_c = Double.parseDouble(day.getElementsByTagName("maxtemp_c").item(0).getTextContent());

            System.out.println("WEATHER GETTED");
            return new WeatherImpl(source, city, country, lat, lon, conditionText, avgtemp_c, mintemp_c, maxtemp_c);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new WeatherGetterException(e);
        }
    }
}
