package jp.ac.kyoto_u.i.soc.ai.iostbase;

import jp.ac.kyoto_u.i.soc.ai.iostbase.dao.entity.Event;

public interface EventStore {
	void insert(Event event);
}
