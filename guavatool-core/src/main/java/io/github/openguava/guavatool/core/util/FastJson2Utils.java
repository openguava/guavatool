package io.github.openguava.guavatool.core.util;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

/**
 * fastjson2 工具类
 * @author openguava
 *
 */
public class FastJson2Utils {

	public static JSONObject parseObject(String text) {
		return JSONObject.parseObject(text);
	}
	
	public static JSONArray parseArray(String text) {
		return JSONArray.parseArray(text);
	}
	
	public static String toJSONString(Object object) {
		return JSONObject.toJSONString(object);
	}
	
	public static String totring(Object object) {
		return JSONObject.toJSONString(object);
	}
}
