package jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
	private String deviceId;
	private String eventType;
	private Object value;
}
