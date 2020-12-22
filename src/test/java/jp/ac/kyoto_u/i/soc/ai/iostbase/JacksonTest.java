package jp.ac.kyoto_u.i.soc.ai.iostbase;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonTest {
	@Test
	public void test() throws Throwable{
		var om = new ObjectMapper();
		System.out.println(om.writeValueAsString("hello"));
		Object v = om.readValue("\"hello\"", Object.class);
		System.out.println(v.getClass());
		System.out.println(v);
	}
}
