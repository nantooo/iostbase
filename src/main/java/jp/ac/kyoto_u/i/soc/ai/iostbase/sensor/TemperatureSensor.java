package jp.ac.kyoto_u.i.soc.ai.iostbase.sensor;

public interface TemperatureSensor extends Sensor{
	enum Type{ CELCIUS, FAHRENHEIT}

	default Type getType() { return Type.CELCIUS;}

	double getTemperature();
}
