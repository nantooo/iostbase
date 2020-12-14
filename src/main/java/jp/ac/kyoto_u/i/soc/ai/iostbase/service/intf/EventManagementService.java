package jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf;

public interface EventManagementService {
	void notifyEvent(Event event);
	Event[] getEvents(long lastEventMillis, long timeoutMillis);
	void updateRule(String ruleId, String body);
	void activateRule(String ruleId);
	void deactivateRule(String ruleId);
}
