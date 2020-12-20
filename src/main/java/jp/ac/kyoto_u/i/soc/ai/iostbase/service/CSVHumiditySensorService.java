package jp.ac.kyoto_u.i.soc.ai.iostbase.service;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import jp.ac.kyoto_u.i.soc.ai.iostbase.sensor.HumiditySensor;

public class CSVHumiditySensorService
extends AbstractSensorService
implements HumiditySensor {
	/**
	 * assume csv has deviceId, timeInMillis, value columns.
	 * @param reader
	 * @throws IOException
	 * @throws CsvValidationException
	 */
	public CSVHumiditySensorService(Reader reader) throws IOException, CsvValidationException {
		records = new LinkedList<>();
		try(CSVReader cr = new CSVReader(reader)){
			records.add(cr.readNext());
		}
		if(records.size() == 0) {
			throw new IOException("no records.");
		}
	}
	@Override
	public double getHumidity() {
		String[] row = null;
		if(records.size() == 1) {
			row = records.get(0);
		} else {
			row = records.remove(0);
		}
		notifyToSubscribers(row[2]);
		return Double.parseDouble(row[2]);
	}

	private List<String[]> records;
}
