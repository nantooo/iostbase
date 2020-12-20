package jp.ac.kyoto_u.i.soc.ai.iostbase.service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

import jp.ac.kyoto_u.i.soc.ai.iostbase.sensor.Sensor;
import jp.ac.kyoto_u.i.soc.ai.iostbase.sensor.SensorSubscriber;
import jp.go.nict.langrid.client.jsonrpc.JsonRpcClientFactory;

public class AbstractSensorService implements Sensor{
	@Override
	public void subscribe(String uri) throws URISyntaxException {
		subscribers.add(new URI(uri));
	}
	@Override
	public void unsubscribe(String uri) {
		try {
			subscribers.remove(new URI(uri));
		} catch(URISyntaxException e) {
			e.printStackTrace();
		}
	}
	protected <T> void notifyToSubscribers(T value) {
		for(URI uri : subscribers) {
			try{
				URL url = uri.toURL();
				new JsonRpcClientFactory().create(SensorSubscriber.class, url).accept(value);
			} catch(MalformedURLException e) {
			}
		}
	}

	private Set<URI> subscribers = new LinkedHashSet<>();
}
