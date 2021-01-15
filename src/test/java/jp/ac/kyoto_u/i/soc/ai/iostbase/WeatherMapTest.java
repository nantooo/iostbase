package jp.ac.kyoto_u.i.soc.ai.iostbase;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherMapTest {
	/*
lat:35.0116,        lon:135.7681,        units: 'metric',        mode: 'json'
*/
	@Test
	public void test() throws Throwable{
		HttpRequest req = HttpRequest.newBuilder(
				new URI("https://community-open-weather-map.p.rapidapi.com/weather?lat=35.0116&lon=135.7681&units=metric&mode=json"))
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
		ObjectMapper m = new ObjectMapper();
		System.out.println(m.readTree(body).get("main").get("temp").asDouble());
	}
}
