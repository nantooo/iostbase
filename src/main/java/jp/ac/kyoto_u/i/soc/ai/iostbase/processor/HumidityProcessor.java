package jp.ac.kyoto_u.i.soc.ai.iostbase.processor;

public interface HumidityProcessor extends Processor{
	<T> T processHumidity(double value);
}
