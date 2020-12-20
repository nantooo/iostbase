package jp.ac.kyoto_u.i.soc.ai.iostbase.service;

import jp.ac.kyoto_u.i.soc.ai.iostbase.actuator.ToggleSwitchActuator;

public class HeaterToggleSwitchActuatorService
extends AbstractActuatorService
implements ToggleSwitchActuator {
	@Override
	public void toggle() {
		on = !on;
		notifyToSubscribers("heater", "toggle");
	}
	public boolean isOn() {
		return on;
	}
	private boolean on;
}
