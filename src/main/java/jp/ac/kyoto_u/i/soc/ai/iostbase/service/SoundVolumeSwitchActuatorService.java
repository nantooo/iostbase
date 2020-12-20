package jp.ac.kyoto_u.i.soc.ai.iostbase.service;

import jp.ac.kyoto_u.i.soc.ai.iostbase.actuator.VolumeSwitchActuator;

public class SoundVolumeSwitchActuatorService
extends AbstractActuatorService
implements VolumeSwitchActuator {
	@Override
	public void setVolume(double volume) {
		this.volume = volume;
		notifyToSubscribers("sound", "setVolume", volume);
	}
	public double getVolume() {
		return volume;
	}
	private double volume;
}
