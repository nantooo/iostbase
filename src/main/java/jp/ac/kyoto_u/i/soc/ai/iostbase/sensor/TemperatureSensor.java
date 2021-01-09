package jp.ac.kyoto_u.i.soc.ai.iostbase.sensor;

public interface TemperatureSensor extends Sensor<Double>{
	enum Type{ CELCIUS, FAHRENHEIT}

	default Type getType() { return Type.CELCIUS;}

	@Override
	default Double getValue() {
		return getTemperature();
	}

	double getTemperature();
}
