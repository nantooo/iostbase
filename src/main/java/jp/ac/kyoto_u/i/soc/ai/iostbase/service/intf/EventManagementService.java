package jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf;

import java.util.Date;

import jp.go.nict.langrid.commons.rpc.intf.Parameter;

public interface EventManagementService {
	void notifyEvent(@Parameter(name="event", sample="{\"deviceId\": \"user:gps:1\",\"dataType\": \"LatLng\",\"value\": {\"latitude\":35.0261389,  \"longitude\": 135.7806666}}")
		Event event);
	Event[] getEvents(@Parameter(name="lastEventMillis") Date lastEventMillis, long timeoutMillis);
	Event[] getEventsOfDevice(
			@Parameter(name="deviceId") String deviceId,
			@Parameter(name="lastEventMillis") Date lastEventMillis, long timeoutMillis);
	Event[] listEvents(@Parameter(name="page", sample="0") int page,
			@Parameter(name="size", sample="10") int size);
	void updateRule(@Parameter(name="ruleId") String ruleId, @Parameter(name="body") String body);
	void activateRule(@Parameter(name="ruleId") String ruleId);
	void deactivateRule(@Parameter(name="ruleId") String ruleId);
}
