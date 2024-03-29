package jp.ac.kyoto_u.i.soc.ai.iostbase.dao.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String deviceId;
	private String dataType;
	private String placeTag;
	private Double latitude;
	private Double longitude;
	private String value;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	public Event(String deviceId, String dataType,
			String placeTag, Double latitude, Double longitude, String value) {
		this.deviceId = deviceId;
		this.dataType = dataType;
		this.placeTag = placeTag;
		this.latitude = latitude;
		this.longitude = longitude;
		this.value = value;
		this.created = new Date();
	}
}
