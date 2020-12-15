package jp.ac.kyoto_u.i.soc.ai.iostbase;

import java.net.URL;
import java.util.Date;

import org.junit.jupiter.api.Test;

import jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf.Event;
import jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf.EventManagementService;
import jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf.LatLng;
import jp.go.nict.langrid.client.jsonrpc.JsonRpcClientFactory;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;

public class GPSSensorTest {
	@Test
	public void test() throws Throwable{
		EventManagementService s = new JsonRpcClientFactory().create(
				EventManagementService.class,
				new URL("http://localhost:8080/jsServices/EventManagement"),
				"user", "password"
				);
		/*
		{"deviceId": "user:gps:1",
		"eventType": "LatLng",
		"value": {
		  "latitude": 35.0261389,
		  "longitude": 135.7806666
		}
		}
		 */
		Event e = new Event("user:gps:1", "LatLng", new LatLng(35.0261389, 135.7806666), new Date());
		s.notifyEvent(e);
		for(Event ev : s.getEvents(new Date(0), 1000)) {
			System.out.println(JSON.encode(ev));
		}
	}
}
