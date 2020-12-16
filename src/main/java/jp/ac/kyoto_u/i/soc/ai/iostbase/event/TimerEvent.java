package jp.ac.kyoto_u.i.soc.ai.iostbase.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimerEvent {
	private long time;
}
