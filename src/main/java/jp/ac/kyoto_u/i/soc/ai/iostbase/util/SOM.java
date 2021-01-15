package jp.ac.kyoto_u.i.soc.ai.iostbase.util;

import java.util.ArrayList;
import java.util.List;
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

	public Iterable<String> propertyNames(){
		return som.keySet();
	}

	public SOM getSOM(String name) {
		return SOM.of(som.get(name));
	}
	
	public Map<String, Object> getMap(){
		return som;
	}

	@SuppressWarnings("unchecked")
	public List<SOM> getSOMList(String name) {
		List<SOM> ret = new ArrayList<>();
		for(Object o : (List<Map<String, Object>>)som.get(name)) {
			ret.add(SOM.of(o));
		}
		return ret;
	}

	public Object getObject(String name) {
		return som.get(name);
	}

	public Object putObject(String name, Object value) {
		return som.put(name, value);
	}

	public Object removeObject(String name) {
		return som.remove(name);
	}

	public String getString(String name) {
		return (String)som.get(name);
	}

	public String removeString(String name) {
		return (String)som.remove(name);
	}

	public int getInt(String name, int defaultValue) {
		Number n = (Number)som.get(name);
		if(n == null) return defaultValue;
		else return n.intValue();
	}

	private Map<String, Object> som;
}
