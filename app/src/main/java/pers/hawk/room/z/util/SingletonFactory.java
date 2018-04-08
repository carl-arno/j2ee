package pers.hawk.room.z.util;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 工厂模式
 */
public class SingletonFactory {

	/**
	 *
	 */
	private static class SingletonJson {
		private static final ObjectMapper objectMapper = new ObjectMapper();
		private static final XMLMapper XML = new XMLMapper();
	}

	/**
	 * @return
	 */
	public static ObjectMapper getSingletonJson() {
		SingletonJson.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return SingletonJson.objectMapper;
	}

	/**
	 * @return
	 */
	public static XMLMapper getSingletonXML() {
		SingletonJson.XML.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return SingletonJson.XML;
	}

}
