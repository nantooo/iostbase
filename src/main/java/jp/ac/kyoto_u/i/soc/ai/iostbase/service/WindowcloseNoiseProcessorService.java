package jp.ac.kyoto_u.i.soc.ai.iostbase.service;

import jp.ac.kyoto_u.i.soc.ai.iostbase.processor.NoiseProcessor;

public class WindowcloseNoiseProcessorService
implements NoiseProcessor {
	public WindowcloseNoiseProcessorService(WindowOpenCloseSwitchActuatorService act,
			int upperThreashold) {
		this.act = act;
		this.upperThreashold = upperThreashold;
	}
	@Override
	public <T> T processNoise(int db) {
		if(upperThreashold < db) {
			act.close();
		}
		return null;
	}
	private WindowOpenCloseSwitchActuatorService act;
	private int upperThreashold;
}
