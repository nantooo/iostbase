package jp.ac.kyoto_u.i.soc.ai.iostbase;

import jp.ac.kyoto_u.i.soc.ai.iostbase.service.intf.Event;

public interface EventStore {
	void insert(Object event);
}
