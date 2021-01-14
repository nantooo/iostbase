package jp.ac.kyoto_u.i.soc.ai.iostbase.sensor;

import java.net.URISyntaxException;

import jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf.LatLng;

public interface Sensor<T> {
	String getDeviceId();
	String getDataType();
	String getPlaceTag();
	LatLng getLatLng();
	T getValue();

	void subscribe(String URI) throws URISyntaxException;

	void unsubscribe(String URI);
}
