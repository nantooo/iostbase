package jp.ac.kyoto_u.i.soc.ai.iostbase.util;

import java.util.Map;

/**
 * Helper class for String to Object map.
 * @author nakaguchi
 *
 */
public class SOM {
	@SuppressWarnings("unchecked")
	public static void set(Object som, String name, Object value) {
		((Map<String, Object>)som).put(name, value);
	}

	public static SOM of(Object som) {
		return new SOM(som);
	}

	@SuppressWarnings("unchecked")
	public SOM(Object som) {
		this.som = (Map<String, Object>)som;
	}

	public SOM get(String name) {
		return SOM.of(som.get(name));
	}

	public Object getObject(String name) {
		return som.get(name);
	}

	public void set(String name, Object value) {
		som.put(name, value);
	}

	private Map<String, Object> som;
}
