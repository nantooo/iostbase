package jp.ac.kyoto_u.i.soc.ai.iostbase.service.iot;

import jp.ac.kyoto_u.i.soc.ai.iostbase.processor.TemperatureProcessor;

public class AirConditionerTemperatureProcessorService
implements TemperatureProcessor {
	public AirConditionerTemperatureProcessorService(AirConditionerOnOffSwitchActuatorService act,
			double lowerThreashold, double upperThreashold) {
		this.act = act;
		this.lowerThreashold = lowerThreashold;
		this.upperThreashold = upperThreashold;
	}
	@Override
	public <T> T processTemperature(double value) {
		if(value < lowerThreashold) {
			act.on();
		} else if(upperThreashold < value) {
			act.on();
		} else {
			act.off();
		}
		return null;
	}
	private AirConditionerOnOffSwitchActuatorService act;
	private double lowerThreashold;
	private double upperThreashold;
}
