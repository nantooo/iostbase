package jp.ac.kyoto_u.i.soc.ai.iostbase;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jp.ac.kyoto_u.i.soc.ai.iostbase.dao.EventRepository;
import jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf.Event;
import jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf.EventManagementService;
import jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf.LatLng;
import jp.ac.kyoto_u.i.soc.ai.iostbase.util.Geo;

@SpringBootTest
class IostbaseApplicationTests {
	@SuppressWarnings("unused")
	@Autowired
	private Sessions sessions;

	@Autowired
	private EventManagementService service;
	
	@Autowired
	private EventRepository repository;

	@Test
	void contextLoads() {
	}

	@BeforeEach
	public void setUp() throws Throwable{
		repository.deleteAll();
	}

	@Test
	public void test_getEvents() throws Throwable{
		service.notifyEvent(new Event("device:1", "temperature", "place:1", null, null, 20.0));
		service.notifyEvent(new Event("device:1", "temperature", "place:1", null, null, 20.2));
		assertEquals(2, service.getEvents(new Date(0), 0).length);
	}
	
	@Test
	public void test_getLatestEventsByPlaceTagAndDataType() throws Throwable{
		service.notifyEvent(new Event("device:1", "temperature", "user1:building1:room1:", null, null, 20.0));
		service.notifyEvent(new Event("device:1", "temperature", "user1:house1:room1", null, null, 18.2));
		service.notifyEvent(new Event("device:2", "temperature", "user1:building1:room1", null, null, 20.2));
		service.notifyEvent(new Event("device:2", "humidity", "user1:building1:room1", null, null, 40.5));
		Event[] events = service.getLatestEventsByPlaceTagAndDataType("user1:", "temperature", new Date(0), 0);
		assertEquals(2, events.length);
		assertEquals(18.2, (Double)events[0].getValue(), 0.001);
		assertEquals("user1:house1:room1", events[0].getPlaceTag());
		assertEquals("device:1", events[0].getDeviceId());
		assertEquals(20.2, (Double)events[1].getValue(), 0.001);
		assertEquals("user1:building1:room1", events[1].getPlaceTag());
		assertEquals("device:2", events[1].getDeviceId());
	}

	private LatLng 博物館 = new LatLng(35.027333803286005, 135.77926611687215);
	private LatLng 時計台 = new LatLng(35.026446451753294, 135.7808217980929);
	private LatLng カンフォーラ = new LatLng(35.0256293769725, 135.7806179502088);
	
	@Test
	public void geotest() throws Throwable{
		System.out.println(Geo.distMeter(カンフォーラ, 時計台));
		System.out.println(Geo.distMeter(カンフォーラ, 博物館));
	}

	@Test
	public void test_getLatestEventsByLatLngAndDataType() throws Throwable{
		service.notifyEvent(new Event("device:1", "temperature", null, 時計台.getLatitude(), 時計台.getLongitude(), 20.0));
		service.notifyEvent(new Event("device:1", "temperature", null, 時計台.getLatitude(), 時計台.getLongitude(), 22.0));
		service.notifyEvent(new Event("device:2", "temperature", null, 博物館.getLatitude(), 博物館.getLongitude(), 18.2));
		Event[] events = service.getLatestEventsByLatLngAndDataType(
				カンフォーラ, 100, "temperature", new Date(0), 0);
		assertEquals(1, events.length);
		assertEquals(22.0, (Double)events[0].getValue(), 0.001);
		assertEquals("device:1", events[0].getDeviceId());
	}
}
