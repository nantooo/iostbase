package jp.ac.kyoto_u.i.soc.ai.iostbase.service;

import jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf.Event;
import jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf.EventManagementService;

public class EventManagement implements EventManagementService{
	@Override
	public void notifyEvent(Event event) {
		System.out.println("notify");
		// TODO Auto-generated method stub
	}

	@Override
	public Event[] getEvents(long lastEventMillis, long timeoutMillis) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateRule(String ruleId, String body) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activateRule(String ruleId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivateRule(String ruleId) {
		// TODO Auto-generated method stub
		
	}
	
	
}
