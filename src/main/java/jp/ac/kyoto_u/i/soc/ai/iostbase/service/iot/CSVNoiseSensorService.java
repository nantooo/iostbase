package jp.ac.kyoto_u.i.soc.ai.iostbase.service.iot;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import jp.ac.kyoto_u.i.soc.ai.iostbase.sensor.NoiseSensor;

public class CSVNoiseSensorService
extends AbstractSensorService
implements NoiseSensor {
	/**
	 * assume csv has deviceId, timeInMillis, value columns.
	 * @param reader
	 * @throws IOException
	 * @throws CsvValidationException
	 */
	public CSVNoiseSensorService(Reader reader) throws IOException, CsvValidationException {
		records = new LinkedList<>();
		try(CSVReader cr = new CSVReader(reader)){
			records.add(cr.readNext());
		}
		if(records.size() == 0) {
			throw new IOException("no records.");
		}
	}
	@Override
	public int getNoise() {
		String[] row = null;
		if(records.size() == 1) {
			row = records.get(0);
		} else {
			row = records.remove(0);
		}
		notifyToSubscribers(row[2]);
		return Integer.parseInt(row[2]);
	}

	private List<String[]> records;
}
