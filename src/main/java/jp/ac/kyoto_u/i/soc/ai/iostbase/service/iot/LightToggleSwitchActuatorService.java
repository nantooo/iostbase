package jp.ac.kyoto_u.i.soc.ai.iostbase.service.iot;

import jp.ac.kyoto_u.i.soc.ai.iostbase.actuator.ToggleSwitchActuator;

public class LightToggleSwitchActuatorService
extends AbstractActuatorService
implements ToggleSwitchActuator {
	@Override
	public void toggle() {
		on = !on;
		notifyToSubscribers("light", "toggle");
	}
	public boolean isOn() {
		return on;
	}
	private boolean on;
}
