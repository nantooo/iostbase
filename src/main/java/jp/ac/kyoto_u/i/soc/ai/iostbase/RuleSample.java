package jp.ac.kyoto_u.i.soc.ai.iostbase;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import org.kie.api.runtime.KieSession;

import jp.ac.kyoto_u.i.soc.ai.iostbase.dao.entity.Event;
import jp.ac.kyoto_u.i.soc.ai.iostbase.event.TimerEvent;
import jp.ac.kyoto_u.i.soc.ai.iostbase.util.DroolsUtil;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;

public class RuleSample {
	public static void main(String[] args) throws Throwable{
		try(InputStream ruleIs = new FileInputStream("./rules/sample.drl");
				InputStream event1Is = new FileInputStream("./rules/event1.json");
				InputStream event2Is = new FileInputStream("./rules/event2.json");
				InputStream event3Is = new FileInputStream("./rules/event3.json")
				){
			KieSession session = DroolsUtil.createSessionFromResource(ruleIs, "sample.drl");
			session.insert(JSON.decode(event1Is, Event.class));
			session.insert(JSON.decode(event2Is, Event.class));
			session.insert(JSON.decode(event3Is, Event.class));
			session.insert(new TimerEvent(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse("2020-12-16 09:30:00.000").getTime()));
			session.fireAllRules();
		}
	}
}
