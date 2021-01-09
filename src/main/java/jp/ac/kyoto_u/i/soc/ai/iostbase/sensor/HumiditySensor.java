package jp.ac.kyoto_u.i.soc.ai.iostbase.sensor;

public interface HumiditySensor extends Sensor<Double>{
	@Override
	default Double getValue() {
		return getHumidity();
	}
	double getHumidity();
}
