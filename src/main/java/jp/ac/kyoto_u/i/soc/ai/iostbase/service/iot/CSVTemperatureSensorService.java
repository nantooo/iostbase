package jp.ac.kyoto_u.i.soc.ai.iostbase.service.iot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import jp.ac.kyoto_u.i.soc.ai.iostbase.sensor.TemperatureSensor;

public class CSVTemperatureSensorService
extends AbstractSensorService<Double>
implements TemperatureSensor {
	/**
	 * assume csv has deviceId, timeInMillis, value columns.
	 * @param reader
	 * @throws IOException
	 * @throws CsvValidationException
	 */
	public CSVTemperatureSensorService() {
		
	}
	
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public int getHeaderRows() {
		return headerRows;
	}

	public void setHeaderRows(int headerRows) {
		this.headerRows = headerRows;
	}

	public int getValueColumn() {
		return valueColumn;
	}

	public void setValueColumn(int valueColumn) {
		this.valueColumn = valueColumn;
	}

	private void readRows() {
		try {
			records = new LinkedList<>();
			try(CSVReader cr = new CSVReader(Files.newBufferedReader(Paths.get(file)))){
				for(int i = 0; i < headerRows; i++) {
					cr.readNext();
				}
				records.addAll(cr.readAll());
			}
			if(records.size() == 0) {
				throw new IOException("no records.");
			}
		} catch(Exception e) {
			records = new ArrayList<>();
		}
	}

	@Override
	public double getTemperature(){
		if(records == null) readRows();
		String[] row = null;
		if(records.size() == 1) {
			row = records.get(0);
		} else if(records.size() == 0){
			throw new RuntimeException();
		} else {
			row = records.remove(0);
		}
		Double ret = Double.parseDouble(row[valueColumn]);
		notifyToSubscribers(ret);
		return ret;
	}

	private String file;
	private int headerRows;
	private int valueColumn;
	private List<String[]> records;
}
