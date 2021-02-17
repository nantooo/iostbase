package jp.ac.kyoto_u.i.soc.ai.iostbase.service.iot;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.kyoto_u.i.soc.ai.iostbase.sensor.HumiditySensor;

public class WeatherforcastService
extends AbstractSensorService<Double>
implements HumiditySensor{
	public String getConf() {
		return conf;
	}
	
	public void setConf(String conf) {
		this.conf = conf;
	}
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	
	@Override
	public double getHumidity() {
		return getHumid();
	}
	
	private double getHumid() {
		try {
			HttpRequest req = HttpRequest.newBuilder(new URI(
					String.format("https://www.life-socket.jp/api/v1/weather/location/lat=%f/lon=%f?lang=en",
							getLatLng().getLatitude(), getLatLng().getLongitude())))
					.GET()
					.setHeader("x-access-key", key)
					.timeout(Duration.ofSeconds(10))
					.build();
			HttpClient client = HttpClient.newBuilder()
					.version(HttpClient.Version.HTTP_1_1)
					.followRedirects(HttpClient.Redirect.NORMAL)
					.connectTimeout(Duration.ofSeconds(10))
					.build();
			HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
			String body = res.body();
			return new ObjectMapper().readTree(body).get("Daily").get(0).get(conf).asDouble();
		} catch(URISyntaxException | IOException | InterruptedException e) {
			e.printStackTrace();
			return -1;
		}
	}
	private String conf;
	private String key;
}
