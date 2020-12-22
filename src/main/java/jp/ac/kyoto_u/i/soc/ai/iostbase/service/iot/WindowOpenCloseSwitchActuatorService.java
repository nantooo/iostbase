package jp.ac.kyoto_u.i.soc.ai.iostbase.service.iot;

import jp.ac.kyoto_u.i.soc.ai.iostbase.actuator.OpenCloseSwitchActuator;

public class WindowOpenCloseSwitchActuatorService
extends AbstractActuatorService
implements OpenCloseSwitchActuator {
	@Override
	public void open() {
		open = true;
		notifyToSubscribers("window", "open");
	}
	@Override
	public void close() {
		open = false;
		notifyToSubscribers("window", "close");
	}
	public boolean isOpen() {
		return open;
	}
	private boolean open;
}
