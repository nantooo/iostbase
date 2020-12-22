package jp.ac.kyoto_u.i.soc.ai.iostbase;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf.Event;
import jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf.EventManagementService;
import jp.ac.kyoto_u.i.soc.ai.iostbase.util.SOM;
import jp.go.nict.langrid.client.jsonrpc.JsonRpcClientFactory;
import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;

public class DumpRequests {
	public static void main(String[] args) throws Throwable{
		var df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		var cf = new JsonRpcClientFactory();
		var s = cf.create(
				EventManagementService.class,
				new URL("http://localhost:8080/jsServices/EventManagement"));
		
		// update rule
		try(var os = new FileOutputStream("requests/01_updateRule.json");
				var is = new FileInputStream("./rules/sample.drl")){
			cf.setRequestDumpStream(os);
			s.updateRule("rule1", StreamUtil.readAsString(is, "UTF-8"));
		}
		try(var os = new FileOutputStream("requests/02_activateRule.json")){
			cf.setRequestDumpStream(os);
			s.activateRule("rule1");
		}
		try(var os = new FileOutputStream("requests/03_addEvent.json");
				var is = new FileInputStream("./rules/event1.json")){
			cf.setRequestDumpStream(os);
			var now = Calendar.getInstance();
			var ev = JSON.decode(is, Event.class);
			SOM.set(ev.getValue(), "targetTime", df.format(CalendarUtil.cloneAndAdd(now, Calendar.MINUTE, 30).getTime()));
			s.notifyEvent(ev);
		}
		try(var os = new FileOutputStream("requests/04_addEvent.json");
				var is = new FileInputStream("./rules/event2.json")){
			cf.setRequestDumpStream(os);
			s.notifyEvent(JSON.decode(is, Event.class));
		}
		try(var os = new FileOutputStream("requests/05_addEvent.json");
				var is = new FileInputStream("./rules/event3.json")){
			cf.setRequestDumpStream(os);
			s.notifyEvent(JSON.decode(is, Event.class));
		}
		try(var os = new FileOutputStream("requests/06_getEventsOfDevice.json")){
			cf.setRequestDumpStream(os);
			cf.setResponseDumpStream(System.out);
			s.getEventsOfDevice("rules:eventdetector", new Date(0), 1000);
		}
	}
}
