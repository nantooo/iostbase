package jp.ac.kyoto_u.i.soc.ai.iostbase.service.iot;

import jp.ac.kyoto_u.i.soc.ai.iostbase.actuator.OnOffSwitchActuator;

public class LightOnOffSwitchActuatorService
extends AbstractActuatorService
implements OnOffSwitchActuator {
	@Override
	public void on() {
		on = true;
		notifyToSubscribers("light", "on");
	}
	@Override
	public void off() {
		on = false;
		notifyToSubscribers("light", "off");
	}
	public boolean isOn() {
		return on;
	}
	private boolean on;
}
