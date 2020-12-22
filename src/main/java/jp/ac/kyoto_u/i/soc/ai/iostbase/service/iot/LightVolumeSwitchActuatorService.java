package jp.ac.kyoto_u.i.soc.ai.iostbase.service.iot;

import jp.ac.kyoto_u.i.soc.ai.iostbase.actuator.VolumeSwitchActuator;

public class LightVolumeSwitchActuatorService
extends AbstractActuatorService
implements VolumeSwitchActuator {
	@Override
	public void setVolume(double volume) {
		this.volume = volume;
		notifyToSubscribers("light", "setVolume", volume);
	}
	public double getVolume() {
		return volume;
	}
	private double volume;
}
