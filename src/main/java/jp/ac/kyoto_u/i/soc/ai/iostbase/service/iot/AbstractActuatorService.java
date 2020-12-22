package jp.ac.kyoto_u.i.soc.ai.iostbase.service.iot;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

import jp.ac.kyoto_u.i.soc.ai.iostbase.actuator.Actuator;
import jp.ac.kyoto_u.i.soc.ai.iostbase.actuator.ActuatorSubscriber;
import jp.go.nict.langrid.client.jsonrpc.JsonRpcClientFactory;

public class AbstractActuatorService implements Actuator{
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
	protected <T> void notifyToSubscribers(String type, String notificationName, Object... parameters) {
		for(URI uri : subscribers) {
			try{
				URL url = uri.toURL();
				new JsonRpcClientFactory().create(ActuatorSubscriber.class, url).accept(type, notificationName, parameters);
			} catch(MalformedURLException e) {
			}
		}
	}

	private Set<URI> subscribers = new LinkedHashSet<>();
}
