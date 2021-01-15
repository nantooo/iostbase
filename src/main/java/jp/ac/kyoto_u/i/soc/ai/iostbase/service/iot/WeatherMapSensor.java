package jp.ac.kyoto_u.i.soc.ai.iostbase.service.iot;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.kyoto_u.i.soc.ai.iostbase.sensor.TemperatureSensor;

public class WeatherMapSensor
extends AbstractSensorService<Double>
implements TemperatureSensor{
	@Override
	public double getTemperature() {
		return getTemp();
	}
	
	private double getTemp() {
		try {
			HttpRequest req = HttpRequest.newBuilder(new URI(
					String.format("https://community-open-weather-map.p.rapidapi.com/weather?lat=%f&lon=%f&units=metric&mode=json",
							getLatLng().getLatitude(), getLatLng().getLongitude())))
					.GET()
					.setHeader("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com")
					.setHeader("x-rapidapi-key", "a6b5e41dbemsh64fdd31e6faed32p1fb68ajsn21b32340e75e")
					.setHeader("useQueryString", "true")
					.timeout(Duration.ofSeconds(10))
					.build();
			HttpClient client = HttpClient.newBuilder()
					.version(HttpClient.Version.HTTP_1_1)
					.followRedirects(HttpClient.Redirect.NORMAL)
					.connectTimeout(Duration.ofSeconds(10))
					.build();
			HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
			String body = res.body();
			return new ObjectMapper().readTree(body).get("main").get("temp").asDouble();
		} catch(URISyntaxException | IOException | InterruptedException e) {
			e.printStackTrace();
			return -1;
		}
	}
}
