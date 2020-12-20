package jp.ac.kyoto_u.i.soc.ai.iostbase.service;

import jp.ac.kyoto_u.i.soc.ai.iostbase.processor.HumidityProcessor;

public class HumidifierHumidityProcessorService
implements HumidityProcessor {
	public HumidifierHumidityProcessorService(HumidifierOnOffSwitchActuatorService act,
			double lowerThreashold) {
		this.act = act;
		this.lowerThreashold = lowerThreashold;
	}
	@Override
	public <T> T processHumidity(double value) {
		if(value < lowerThreashold) {
			act.on();
		} else {
			act.off();
		}
		return null;
	}
	private HumidifierOnOffSwitchActuatorService act;
	private double lowerThreashold;
}
