package jp.ac.kyoto_u.i.soc.ai.iostbase.sensor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

import jp.ac.kyoto_u.i.soc.ai.iostbase.actuator.Actuator;

public interface Sensor {
	default Object getValue(String methodName) {
		try {
			Method m = getClass().getMethod(methodName);
			do {
				if(m.getParameterCount() != 1) break;
				if(m.getDeclaringClass().equals(Actuator.class)) break;
				return m.invoke(this);
			} while(false);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
		throw new RuntimeException("no suitable method to execute.");
	}

	void subscribe(String URI) throws URISyntaxException;

	void unsubscribe(String URI);
}
