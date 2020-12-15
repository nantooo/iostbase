package jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf;

import java.util.Date;

public interface EventManagementService {
	void notifyEvent(Event event);
	Event[] getEvents(Date lastEventMillis, long timeoutMillis);
	void updateRule(String ruleId, String body);
	void activateRule(String ruleId);
	void deactivateRule(String ruleId);
}
