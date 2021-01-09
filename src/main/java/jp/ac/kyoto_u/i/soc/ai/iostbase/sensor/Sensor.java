package jp.ac.kyoto_u.i.soc.ai.iostbase.sensor;

import java.net.URISyntaxException;

public interface Sensor<T> {
	String getDeviceId();
	String getDataType();
	String getPlaceTag();
	T getValue();

	void subscribe(String URI) throws URISyntaxException;

	void unsubscribe(String URI);
}
