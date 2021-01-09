package jp.ac.kyoto_u.i.soc.ai.iostbase.service.iot;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

import jp.ac.kyoto_u.i.soc.ai.iostbase.sensor.Sensor;
import jp.ac.kyoto_u.i.soc.ai.iostbase.sensor.SensorSubscriber;
import jp.go.nict.langrid.client.jsonrpc.JsonRpcClientFactory;

public abstract class AbstractSensorService<T> implements Sensor<T>{
	public AbstractSensorService() {
	}

	public AbstractSensorService(String deviceId, String dataType, String placeTag) {
		this.deviceId = deviceId;
		this.dataType = dataType;
		this.placeTag = placeTag;
	}

	@Override
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Override
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@Override
	public String getPlaceTag() {
		return placeTag;
	}

	public void setPlaceTag(String placeTag) {
		this.placeTag = placeTag;
	}

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
	protected void notifyToSubscribers(T value) {
		for(URI uri : subscribers) {
			try{
				URL url = uri.toURL();
				new JsonRpcClientFactory().create(SensorSubscriber.class, url).accept(value);
			} catch(MalformedURLException e) {
			}
		}
	}

	private String deviceId;
	private String dataType;
	private String placeTag;
	private Set<URI> subscribers = new LinkedHashSet<>();
}
