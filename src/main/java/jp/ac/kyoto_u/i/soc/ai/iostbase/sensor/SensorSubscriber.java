package jp.ac.kyoto_u.i.soc.ai.iostbase.sensor;

public interface SensorSubscriber {
	<T> void accept(T value);
}
