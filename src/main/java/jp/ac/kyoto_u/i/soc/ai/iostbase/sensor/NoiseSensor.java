package jp.ac.kyoto_u.i.soc.ai.iostbase.sensor;

public interface NoiseSensor extends Sensor<Integer>{
	@Override
	default Integer getValue() {
		return getNoise();
	}
	/**
	 * return noise in dB.
	 */
	int getNoise();
}
