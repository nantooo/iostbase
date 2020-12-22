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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.kyoto_u.i.soc.ai.iostbase.EventStore;
import jp.ac.kyoto_u.i.soc.ai.iostbase.dao.EventRepository;
import jp.ac.kyoto_u.i.soc.ai.iostbase.event.TimerEvent;
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

	@Scheduled(initialDelay = 10000, fixedRate = 10000)
	public void insertTimerEvent() {
		es.insert(new TimerEvent(new Date().getTime()));
	}

	private EventStore es = e -> {
		insertEvent(e);
		System.out.println("insert: " + JSON.encode(e));
	};

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	class RuleSession{
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
				if(!ruleId.endsWith(".drl")) ruleId += ".drl";
				session = DroolsUtil.createSessionFromResource(new ByteArrayInputStream(body.getBytes("UTF-8")), ruleId);
				session.setGlobal("eventStore", es);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public void insert(Event e) {
			if(session != null && active) {
				session.insert(e);
				session.fireAllRules();
			}
		}
		public void insert(TimerEvent e) {
			if(session != null && active) {
				session.insert(e);
				session.fireAllRules();
			}
		}
		private boolean active;
		private KieSession session;
	}

	@Override
	public void notifyEvent(Event event) {
		insertEvent(event);
	}

	private void insertEvent(Object event) {
		Converter c = new Converter();
		if(event instanceof Event) {
			var ev = new jp.ac.kyoto_u.i.soc.ai.iostbase.dao.entity.Event();
			c.copyProperties(ev, event);
			if(ev.getCreated() == null) {
				ev.setCreated(new Date());
			}
			Object v = ((Event)event).getValue();
			ObjectMapper m = new ObjectMapper();
			try {
				ev.setValue(m.writeValueAsString(v));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			er.save(ev);
			sessions.values().forEach(s -> s.insert((Event)event));
		} else if(event instanceof TimerEvent) {
			sessions.values().forEach(s -> s.insert((TimerEvent)event));
		} else {
			throw new RuntimeException("unknown type of event: " + event.getClass());
		}
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

	private Map<String, RuleSession> sessions = new HashMap<>();
}
