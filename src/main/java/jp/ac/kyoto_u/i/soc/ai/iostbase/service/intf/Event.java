package jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
	private String deviceId;
	private String dataType;
	private Object value;
	private Date created;
}
