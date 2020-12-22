package jp.ac.kyoto_u.i.soc.ai.iostbase.service.iot;

import jp.ac.kyoto_u.i.soc.ai.iostbase.actuator.OnOffSwitchActuator;

public class MicOnOffSwitchActuatorService
extends AbstractActuatorService
implements OnOffSwitchActuator {
	@Override
	public void on() {
		on = true;
		notifyToSubscribers("mic", "on");
	}
	@Override
	public void off() {
		on = false;
		notifyToSubscribers("mic", "off");
	}
	public boolean isOn() {
		return on;
	}
	private boolean on;
}
