package jp.ac.kyoto_u.i.soc.ai.iostbase.service;

import jp.ac.kyoto_u.i.soc.ai.iostbase.processor.TemperatureProcessor;

public class HeaterTemperatureProcessorService
implements TemperatureProcessor {
	public HeaterTemperatureProcessorService(HeaterOnOffSwitchActuatorService act, double threashold) {
		this.act = act;
		this.threashold = threashold;
	}
	@Override
	public <T> T processTemperature(double value) {
		if(value < threashold) {
			act.on();
		} else {
			act.off();
		}
		return null;
	}
	private HeaterOnOffSwitchActuatorService act;
	private double threashold;
}
