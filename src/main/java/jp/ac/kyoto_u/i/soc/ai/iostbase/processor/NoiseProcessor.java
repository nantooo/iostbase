package jp.ac.kyoto_u.i.soc.ai.iostbase.processor;

public interface NoiseProcessor extends Processor{
	<T> T processNoise(int db);
}
