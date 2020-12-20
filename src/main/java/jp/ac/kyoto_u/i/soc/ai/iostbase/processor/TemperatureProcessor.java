package jp.ac.kyoto_u.i.soc.ai.iostbase.processor;

public interface TemperatureProcessor extends Processor{
	<T> T processTemperature(double value);
}
