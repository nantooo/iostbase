package jp.ac.kyoto_u.i.soc.ai.iostbase.service;

import jp.ac.kyoto_u.i.soc.ai.iostbase.processor.HumidityProcessor;

public class DehumidifierHumidityProcessorService
implements HumidityProcessor {
	public DehumidifierHumidityProcessorService(DehumidifierOnOffSwitchActuatorService act,
			double upperThreashold) {
		this.act = act;
		this.upperThreashold = upperThreashold;
	}
	@Override
	public <T> T processHumidity(double value) {
		if(upperThreashold < value) {
			act.on();
		} else {
			act.off();
		}
		return null;
	}
	private DehumidifierOnOffSwitchActuatorService act;
	private double upperThreashold;
}
