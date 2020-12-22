package jp.ac.kyoto_u.i.soc.ai.iostbase.service.iot;

import jp.ac.kyoto_u.i.soc.ai.iostbase.actuator.OnOffSwitchActuator;

public class HeaterOnOffSwitchActuatorService
extends AbstractActuatorService
implements OnOffSwitchActuator {
	@Override
	public void on() {
		on = true;
		notifyToSubscribers("heater", "on");
	}
	@Override
	public void off() {
		on = false;
		notifyToSubscribers("heater", "off");
	}
	public boolean isOn() {
		return on;
	}
	private boolean on;
}
