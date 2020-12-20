package jp.ac.kyoto_u.i.soc.ai.iostbase.actuator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

import jp.go.nict.langrid.commons.beanutils.ConversionException;
import jp.go.nict.langrid.commons.beanutils.Converter;

public interface Actuator {
	default Object actuate(String methodName, Object... parameters) {
		Converter c = new Converter();
		Object[] params = new Object[parameters.length];
		for(Method m : getClass().getMethods()) {
			if(m.getParameterCount() != parameters.length) continue;
			if(!m.getName().equals(methodName)) continue;
			if(m.getDeclaringClass().equals(Actuator.class)) continue;
			boolean callable = true;
			for(int i = 0; i < parameters.length; i++) {
				if(parameters[i] == null && m.getParameterTypes()[i].isPrimitive()) {
					callable = false;
					break;
				}
				params[i] = parameters[i];
				if(m.getParameterTypes()[i].isAssignableFrom(parameters[i].getClass())) continue;
				if(parameters[i] == null) continue;
				try {
					params[i] = c.convert(parameters[i], m.getParameterTypes()[i]);
					continue;
				} catch(ConversionException e) {
				}
				callable = false;
				break;
			}
			if(!callable) continue;
			try {
				return m.invoke(this, params);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
		throw new RuntimeException("no suitable method to execute.");
	}

	void subscribe(String uri) throws URISyntaxException;

	void unsubscribe(String uri);
}
