package jp.ac.kyoto_u.i.soc.ai.iostbase.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.ac.kyoto_u.i.soc.ai.iostbase.dao.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
	List<Event> findAllByCreatedGreaterThanOrderByCreated(Date time);
	List<Event> findAllByDeviceIdEqualsAndCreatedGreaterThanOrderByCreated(String deviceId, Date time);
	List<Event> findAllByPlaceTagStartingWithAndDataTypeEqualsAndCreatedGreaterThanOrderByCreated(String placeTagPrefix, String dataType, Date time);
	List<Event> findAllByDataTypeEqualsAndCreatedGreaterThanOrderByCreated(String dataType, Date time);
}
