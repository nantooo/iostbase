package jp.ac.kyoto_u.i.soc.ai.iostbase;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.kyoto_u.i.soc.ai.iostbase.sensor.Sensor;
import jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf.Event;
import jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf.EventManagementService;
import jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf.LatLng;
import jp.ac.kyoto_u.i.soc.ai.iostbase.util.SOM;
import jp.go.nict.langrid.client.jsonrpc.JsonRpcClientFactory;
import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.commons.lang.ClassUtil;

public class SensorRunner {
	private EventManagementService service;
	private List<Sensor<?>> sensors;
	private int intervalMillis = 10000;

	public SensorRunner(String url) throws MalformedURLException {
		if(url != null) {
			var cf = new JsonRpcClientFactory();
			service = cf.create(
					EventManagementService.class,
					new URL(url));
		} else {
			service = new EventManagementService() {
				@Override
				public void updateRule(String ruleId, String body) {
				}
				
				@Override
				public void notifyEvent(Event event) {
					try {
						System.out.println(new ObjectMapper().writeValueAsString(event));
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public Event[] listEvents(int page, int size) {
					return null;
				}
				
				@Override
				public Event[] getEventsOfDevice(String deviceId, Date lastEventMillis, long timeoutMillis) {
					return null;
				}
				
				@Override
				public Event[] getLatestEventsByLatLngAndDataType(LatLng latLng, double r, String dataType,
						Date lastEventMillis, long timeoutMillis) {
					return null;
				}
				
				@Override
				public Event[] getLatestEventsByPlaceTagAndDataType(String placeTagPrefix, String dataType,
						Date lastEventMillis, long timeoutMillis) {
					return null;
				}
				
				@Override
				public Event[] getEvents(Date lastEventMillis, long timeoutMillis) {
					return null;
				}
				
				@Override
				public void deactivateRule(String ruleId) {
				}
				
				@Override
				public void activateRule(String ruleId) {
				}
			};
		}
	}

	public List<Sensor<?>> getSensors() {
		return sensors;
	}

	public void setSensors(List<Sensor<?>> sensors) {
		this.sensors = sensors;
	}

	public int getIntervalMillis() {
		return intervalMillis;
	}

	public void setIntervalMillis(int intervalMillis) {
		this.intervalMillis = intervalMillis;
	}

	public void run() throws InterruptedException {
		while(!Thread.interrupted()) {
			long start = System.nanoTime();
			for(Sensor<?> s : sensors) {
				Object value = s.getValue();
				Event e = new Event(s.getDeviceId(), s.getDataType(), s.getPlaceTag(),
						s.getLatLng() != null ? s.getLatLng().getLatitude() : null,
						s.getLatLng() != null ? s.getLatLng().getLongitude() : null,
						value);
				service.notifyEvent(e);
			}
			long d = System.nanoTime() - start;
			long st = intervalMillis - (d / 1000000);
			if(st > 0) {
				Thread.sleep(st);
			}
		}
	}

	public static void main(String[] args) throws Throwable{
		int pi = 0;
		String configFile = args.length > pi ? args[pi++] : "./sensorrunner/config.json";
		String iostUrl = args.length > pi ? args[pi++] : null;
		SensorRunner r = new SensorRunner(iostUrl);
		ObjectMapper m = new ObjectMapper();
		SOM config = SOM.of(m.reader().readValue(new File(configFile), Map.class));
		r.setIntervalMillis(config.getInt("intervalMillis", 10000));
		List<Sensor<?>> sensors = new ArrayList<>();
		Converter c = new Converter();
		for(SOM som : config.getSOMList("sensors")) {
			Sensor<?> s = (Sensor<?>)Class.forName(som.removeString("type")).getConstructor().newInstance();
			for(String name : som.propertyNames()) {
				Method setter = ClassUtil.findSetter(s.getClass(), name);
				setter.invoke(s, c.convert(som.getObject(name), setter.getParameterTypes()[0]));
			}
			sensors.add(s);
		}
		r.setSensors(sensors);
		r.run();
	}
}
