package jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
	public Event(String deviceId, String dataType,
			String placeTag, Double latitide, Double longitude,
			Object value) {
		this.deviceId = deviceId;
		this.dataType = dataType;
		this.placeTag = placeTag;
		this.latitude = latitide;
		this.longitude = longitude;
		this.value = value;
		this.created = new Date();
	}

	private String deviceId;
	private String dataType;
	private String placeTag;
	private Double latitude;
	private Double longitude;
	private Object value;
	private Date created;
}
