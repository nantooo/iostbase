package jp.ac.kyoto_u.i.soc.ai.iostbase.util;

import org.geotools.referencing.GeodeticCalculator;

import jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf.LatLng;

public class Geo {
	public static double distMeter(LatLng loc1, LatLng loc2) {
		GeodeticCalculator c = new GeodeticCalculator();
		c.setStartingGeographicPoint(loc1.getLongitude(), loc1.getLatitude());
		c.setDestinationGeographicPoint(loc2.getLongitude(), loc2.getLatitude());
		return c.getOrthodromicDistance();
	}
	public static double distMeter(double lat1, double lng1, double lat2, double lng2) {
		GeodeticCalculator c = new GeodeticCalculator();
		c.setStartingGeographicPoint(lng1, lat1);
		c.setDestinationGeographicPoint(lng2, lat2);
		return c.getOrthodromicDistance();
	}
}
