package jp.ac.kyoto_u.i.soc.ai.iostbase.service.iot;

import jp.ac.kyoto_u.i.soc.ai.iostbase.actuator.OnOffSwitchActuator;

public class HumidifierOnOffSwitchActuatorService
extends AbstractActuatorService
implements OnOffSwitchActuator {
	@Override
	public void on() {
		on = true;
		notifyToSubscribers("humidifier", "on");
	}
	@Override
	public void off() {
		on = false;
		notifyToSubscribers("humidifier", "off");
	}
	public boolean isOn() {
		return on;
	}
	private boolean on;
}
