package jp.ac.kyoto_u.i.soc.ai.iostbase.dao.entity;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class LatLng {
	private double latitude;
	private double longitude;
	public double distance(LatLng target) {
		return Math.sqrt(
				Math.pow(latitude - target.latitude, 2) +
				Math.pow(longitude - target.longitude, 2));
	}
}
