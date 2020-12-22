package jp.ac.kyoto_u.i.soc.ai.iostbase.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.kyoto_u.i.soc.ai.iostbase.IostbaseApplication;
import jp.ac.kyoto_u.i.soc.ai.iostbase.dao.EventRepository;
import jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf.Event;
import jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf.EventManagementService;
import jp.go.nict.langrid.commons.beanutils.Converter;

@Component
public class EventManagement implements EventManagementService{
	@Autowired
	private EventRepository er;

	public EventManagement() {
	}

	@Override
	public void notifyEvent(Event event) {
		IostbaseApplication.instance().insertEvent(event);
	}

	@Override
	public Event[] getEvents(Date lastEventMillis, long timeoutMillis) {
		long start = System.currentTimeMillis();
		while(true) {
			var ret = er.findAllByCreatedGreaterThan(lastEventMillis);
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
			var ret = er.findAllByDeviceIdEqualsAndCreatedGreaterThan(deviceId, lastEventMillis);
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
	public Event[] listEvents(int page, int size) {
		return convert(er.findAll(PageRequest.of(page, size)).getContent());
	}

	@Override
	public void updateRule(String ruleId, String body) {
		IostbaseApplication.instance().updateRule(ruleId, body);
	}

	@Override
	public void activateRule(String ruleId) {
		IostbaseApplication.instance().activateRule(ruleId);
	}

	@Override
	public void deactivateRule(String ruleId) {
		IostbaseApplication.instance().deactivateRule(ruleId);
	}

	private Event[] convert(List<jp.ac.kyoto_u.i.soc.ai.iostbase.dao.entity.Event> events) {
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
