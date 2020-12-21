package jp.ac.kyoto_u.i.soc.ai.iostbase.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import jp.ac.kyoto_u.i.soc.ai.iostbase.dao.EventRepository;
import jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf.Event;
import jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf.EventManagementService;
import jp.ac.kyoto_u.i.soc.ai.iostbase.util.DroolsUtil;
import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
public class EventManagement implements EventManagementService{
	@Autowired
	private EventRepository er;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class RuleSession{
		public void activate() {
			if(session != null) active = true;
		}
		public void deactivate() {
			if(session != null) active = false;
		}
		public void recreate(String ruleId, String body) {
			if(session != null) {
				session.destroy();
			}
			try {
				session = DroolsUtil.createSessionFromResource(new ByteArrayInputStream(body.getBytes("UTF-8")), ruleId);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public void insert(jp.ac.kyoto_u.i.soc.ai.iostbase.dao.entity.Event e) {
			if(session != null && active) session.insert(e);
		}
		private boolean active;
		private KieSession session;
	}

	@Override
	public void notifyEvent(Event event) {
		Converter c = new Converter();
		var ev = new jp.ac.kyoto_u.i.soc.ai.iostbase.dao.entity.Event();
		c.copyProperties(ev, event);
		if(ev.getCreated() == null) {
			ev.setCreated(new Date());
		}
		ev.setValue(JSON.encode(event.getValue()));
		er.save(ev);
		sessions.values().forEach(s -> s.insert(ev));
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
		sessions.computeIfAbsent(ruleId, rid -> new RuleSession()).recreate(ruleId, body);;
	}

	@Override
	public void activateRule(String ruleId) {
		sessions.getOrDefault(ruleId, new RuleSession()).activate();
	}

	@Override
	public void deactivateRule(String ruleId) {
		sessions.getOrDefault(ruleId, new RuleSession()).deactivate();
	}

	private Event[] convert(List<jp.ac.kyoto_u.i.soc.ai.iostbase.dao.entity.Event> events) {
		Converter c = new Converter();
		var ret = new ArrayList<>();
		for(var ev : events) {
			var e = c.convert(ev, Event.class);
			e.setValue(JSON.decode(ev.getValue()));
			ret.add(e);
		}
		return ret.toArray(new Event[] {});
	}

	private Map<String, RuleSession> sessions = new HashMap<>();
}
