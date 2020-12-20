package jp.ac.kyoto_u.i.soc.ai.iostbase.sensor;

public interface NoiseSensor extends Sensor{
	/**
	 * return noise in dB.
	 */
	int getNoise();
}
