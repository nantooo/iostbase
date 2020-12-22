package jp.ac.kyoto_u.i.soc.ai.iostbase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.kyoto_u.i.soc.ai.iostbase.dao.EventRepository;
import jp.ac.kyoto_u.i.soc.ai.iostbase.event.TimerEvent;
import jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf.Event;
import jp.ac.kyoto_u.i.soc.ai.iostbase.util.DroolsUtil;
import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
public class IostbaseApplication {
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

	public void updateRule(String ruleId, String body) {
		sessions.computeIfAbsent(ruleId, rid -> new RuleSession()).recreate(ruleId, body);;
	}
	
	public void activateRule(String ruleId) {
		sessions.getOrDefault(ruleId, new RuleSession()).activate();
	}

	public void deactivateRule(String ruleId) {
		sessions.getOrDefault(ruleId, new RuleSession()).deactivate();
	}

	@Autowired
	private EventRepository er;

	@Scheduled(initialDelay = 10000, fixedRate = 10000)
	public void insertTimerEvent() {
		es.insert(new TimerEvent(new Date().getTime()));
	}

	private EventStore es = e -> {
		insertEvent(e);
	};

	public void insertEvent(Object event) {
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

	public static IostbaseApplication instance() {
		return instance;
	}

	private Map<String, RuleSession> sessions = new HashMap<>();

	private static IostbaseApplication instance;

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(IostbaseApplication.class, args);
		IostbaseApplication app = ctx.getBean(IostbaseApplication.class);
		instance = app;
	}

}
