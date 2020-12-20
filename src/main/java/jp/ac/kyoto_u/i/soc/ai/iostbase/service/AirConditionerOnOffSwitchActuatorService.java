package jp.ac.kyoto_u.i.soc.ai.iostbase.service;

import jp.ac.kyoto_u.i.soc.ai.iostbase.actuator.OnOffSwitchActuator;

public class AirConditionerOnOffSwitchActuatorService
extends AbstractActuatorService
implements OnOffSwitchActuator {
	@Override
	public void on() {
		on = true;
		notifyToSubscribers("airconditioner", "on");
	}
	@Override
	public void off() {
		on = false;
		notifyToSubscribers("airconditioner", "off");
	}
	public boolean isOn() {
		return on;
	}
	private boolean on;
}
