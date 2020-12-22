package jp.ac.kyoto_u.i.soc.ai.iostbase.service.iot;

import jp.ac.kyoto_u.i.soc.ai.iostbase.actuator.OnOffSwitchActuator;

public class DehumidifierOnOffSwitchActuatorService
extends AbstractActuatorService
implements OnOffSwitchActuator {
	@Override
	public void on() {
		on = true;
		notifyToSubscribers("dehumidifier", "on");
	}
	@Override
	public void off() {
		on = false;
		notifyToSubscribers("dehumidifier", "off");
	}
	public boolean isOn() {
		return on;
	}
	private boolean on;
}
