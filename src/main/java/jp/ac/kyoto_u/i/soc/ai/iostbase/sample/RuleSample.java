package jp.ac.kyoto_u.i.soc.ai.iostbase.sample;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import jp.ac.kyoto_u.i.soc.ai.iostbase.EventStore;
import jp.ac.kyoto_u.i.soc.ai.iostbase.event.TimerEvent;
import jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf.Event;
import jp.ac.kyoto_u.i.soc.ai.iostbase.util.DroolsUtil;
import jp.ac.kyoto_u.i.soc.ai.iostbase.util.SOM;
import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;

public class RuleSample {
	public static void main(String[] args) throws Throwable{
		var df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		try(var ruleIs = new FileInputStream("./rules/sample.drl");
				var event1Is = new FileInputStream("./rules/event1.json");
				var event2Is = new FileInputStream("./rules/event2.json");
				var event3Is = new FileInputStream("./rules/event3.json")
				){
			var session = DroolsUtil.createSessionFromResource(ruleIs, "sample.drl");
			session.setGlobal("eventStore", (EventStore)(e -> {
				System.out.println("event stored: " + JSON.encode(e));
				session.insert(e);
			}));
			var e1 = JSON.decode(event1Is, Event.class);
			var now = Calendar.getInstance();
			SOM.set(e1.getValue(), "targetTime", df.format(CalendarUtil.cloneAndAdd(now, Calendar.MINUTE, 30).getTime()));
			session.insert(e1);
			session.insert(JSON.decode(event2Is, Event.class));
			session.insert(JSON.decode(event3Is, Event.class));
			session.insert(new TimerEvent(now.getTimeInMillis()));
			session.fireAllRules();
		}
	}
}
