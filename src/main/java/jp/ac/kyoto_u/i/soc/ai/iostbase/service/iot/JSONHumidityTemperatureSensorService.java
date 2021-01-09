package jp.ac.kyoto_u.i.soc.ai.iostbase.service.iot;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.opencsv.exceptions.CsvValidationException;

import jp.ac.kyoto_u.i.soc.ai.iostbase.sensor.HumiditySensor;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;

public class JSONHumidityTemperatureSensorService
extends AbstractSensorService<Double>
implements HumiditySensor {
	/**
	 * assume json has an array of {"deviceId": string, "timeInMillis": number, value: number}.
	 * @param reader
	 * @throws IOException
	 * @throws CsvValidationException
	 */
	@SuppressWarnings("unchecked")
	public JSONHumidityTemperatureSensorService(Reader reader) throws IOException, CsvValidationException {
		records = new LinkedList<>();
		for(var e : (Map<String, Object>[])JSON.decode(reader)) {
			records.add(e);
		}
		if(records.size() == 0) {
			throw new IOException("no elements.");
		}
	}
	@Override
	public double getHumidity() {
		Map<String, Object> val = null;
		if(records.size() == 1) {
			val = records.get(0);
		} else {
			val = records.remove(0);
		}
		Double ret = ((Number)val.get("value")).doubleValue();
		notifyToSubscribers(ret);
		return ret;
	}

	private List<Map<String, Object>> records;
}
