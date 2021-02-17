package jp.ac.kyoto_u.i.soc.ai.iostbase.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.kyoto_u.i.soc.ai.iostbase.Sessions;
import jp.ac.kyoto_u.i.soc.ai.iostbase.dao.EventRepository;
import jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf.Event;
import jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf.EventManagementService;
import jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf.LatLng;
import jp.ac.kyoto_u.i.soc.ai.iostbase.util.Geo;
import jp.go.nict.langrid.commons.beanutils.Converter;

@Component
public class EventManagement implements EventManagementService{
	@Autowired
	private EventRepository er;

	public EventManagement() {
	}

	@Override
	public void notifyEvent(Event event) {
		Sessions.instance().insertEvent(event);
	}

	@Override
	public Event[] getEvents(Date lastEventMillis, long timeoutMillis) {
		long start = System.currentTimeMillis();
		while(true) {
			var ret = er.findAllByCreatedGreaterThanOrderByCreated(lastEventMillis);
			long d = System.currentTimeMillis() - start;
			if(ret.size() == 0 && d < timeoutMillis) {
				try {
					Thread.sleep(100);
					continue;
				} catch (InterruptedException e) {
				}
			}
			return convert(ret);
		}
	}

	@Override
	public Event[] getEventsOfDevice(String deviceId, Date lastEventMillis, long timeoutMillis) {
		long start = System.currentTimeMillis();
		while(true) {
			var ret = er.findAllByDeviceIdEqualsAndCreatedGreaterThanOrderByCreated(deviceId, lastEventMillis);
			long d = System.currentTimeMillis() - start;
			if(ret.size() == 0 && d < timeoutMillis) {
				try {
					Thread.sleep(100);
					continue;
				} catch (InterruptedException e) {
				}
			}
			return convert(ret);
		}
	}
	
	@Override
	public Event[] getLatestEventsByPlaceTagAndDataType(String placeTagPrefix, String dataType, Date lastEventMillis, long timeoutMillis) {
		long start = System.currentTimeMillis();
		while(true) {
			var ret = new LinkedHashMap<String, jp.ac.kyoto_u.i.soc.ai.iostbase.dao.entity.Event>();
			for(var ev : er.findAllByDataTypeEqualsAndCreatedGreaterThanOrderByCreated(dataType, lastEventMillis)) {
				if(ev.getPlaceTag()!="outside"){
					if(!ev.getPlaceTag().contains(placeTagPrefix)) continue;
				}
				if(ret.containsKey(ev.getDeviceId())){
					ret.remove(ev.getDeviceId());
				}
				ret.put(ev.getDeviceId(), ev);
			}
			long d = System.currentTimeMillis() - start;
			if(ret.size() == 0 && d < timeoutMillis) {
				try {
					Thread.sleep(100);
					continue;
				} catch (InterruptedException e) {
				}
			}
			return convert(ret.values());
		}
	}

	@Override
	public Event[] getLatestEventsByLatLngAndDataType(LatLng latLng, double r, String dataType, Date lastEventMillis,
			long timeoutMillis) {
		var start = System.currentTimeMillis();
		while(true) {
			var ret = new LinkedHashMap<String, jp.ac.kyoto_u.i.soc.ai.iostbase.dao.entity.Event>();
			for(var ev : er.findAllByDataTypeEqualsAndCreatedGreaterThanOrderByCreated(dataType, lastEventMillis)) {
				if(ev.getLatitude() != null && ev.getLongitude() != null) {
					if(Geo.distMeter(latLng.getLatitude(), latLng.getLongitude(), ev.getLatitude(), ev.getLongitude()) > r) continue;
				}
				if(ret.containsKey(ev.getDeviceId())){
					ret.remove(ev.getDeviceId());
				}
				ret.put(ev.getDeviceId(), ev);
			}
			var d = System.currentTimeMillis() - start;
			if(ret.size() == 0 && d < timeoutMillis) {
				try {
					Thread.sleep(100);
					continue;
				} catch (InterruptedException e) {
				}
			}
			return convert(ret.values());
		}
	}

	@Override
	public Event[] listEvents(int page, int size) {
		return convert(er.findAll(PageRequest.of(page, size)).getContent());
	}

	@Override
	public void updateRule(String ruleId, String body) {
		Sessions.instance().updateRule(ruleId, body);
	}

	@Override
	public void activateRule(String ruleId) {
		Sessions.instance().activateRule(ruleId);
	}

	@Override
	public void deactivateRule(String ruleId) {
		Sessions.instance().deactivateRule(ruleId);
	}

	private Event[] convert(Iterable<jp.ac.kyoto_u.i.soc.ai.iostbase.dao.entity.Event> events) {
		ObjectMapper m = new ObjectMapper();
		Converter c = new Converter();
		var ret = new ArrayList<>();
		for(var ev : events) {
			var e = c.convert(ev, Event.class);
			try {
				e.setValue(m.readValue(ev.getValue(), Object.class));
			} catch (JsonProcessingException e1) {
				e1.printStackTrace();
			}
			ret.add(e);
		}
		return ret.toArray(new Event[] {});
	}
}
