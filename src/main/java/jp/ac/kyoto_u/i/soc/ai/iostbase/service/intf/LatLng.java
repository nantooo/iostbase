package jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LatLng {
	private double latitude;
	private double longitude;
	public double distance(LatLng target) {
		return Math.sqrt(
				Math.pow(latitude - target.latitude, 2) +
				Math.pow(longitude - target.longitude, 2));
	}

	public double distance(double lat, double lon) {
		return Math.sqrt(
				Math.pow(latitude - lat, 2) +
				Math.pow(longitude - lon, 2));
	}
}
