package jp.ac.kyoto_u.i.soc.ai.iostbase.actuator;

public interface ActuatorSubscriber {
	void accept(String actuatorType, String methodName, Object... parameters);
}
