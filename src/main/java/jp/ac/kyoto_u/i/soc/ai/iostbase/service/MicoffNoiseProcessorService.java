package jp.ac.kyoto_u.i.soc.ai.iostbase.service;

import jp.ac.kyoto_u.i.soc.ai.iostbase.processor.NoiseProcessor;

public class MicoffNoiseProcessorService
implements NoiseProcessor {
	public MicoffNoiseProcessorService(MicOnOffSwitchActuatorService act,
			int upperThreashold) {
		this.act = act;
		this.upperThreashold = upperThreashold;
	}
	@Override
	public <T> T processNoise(int db) {
		if(upperThreashold < db) {
			act.off();
		} else {
			act.on();
		}
		return null;
	}
	private MicOnOffSwitchActuatorService act;
	private int upperThreashold;
}
