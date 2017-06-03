package com.yuanmie.json;

import java.lang.reflect.Field;
import java.util.Map;


public class Kson {
	private Parser parser;
	public Kson(){
		Token token = new Token("");
		parser = new Parser(token);
	}

	public Map<String, Object> fromJsonToMap(String json){
		parser.reset(json);
		return parser.parse();
	}

	public <T> T fromJson(String json, Class<T> clazz){
		Map<String, Object> map = fromJsonToMap(json);
		return map2class(map, clazz);
	}

	public String toJson(Object o){
		return Java2Json.convert(o);
	}
	
	private <T> T map2class(Map<String, Object> map, Class<T> clazz){
		T object = null;
		try {
			object = clazz.newInstance();
			Field fields[] = clazz.getDeclaredFields();
			String fieldName;
			for(Field field : fields){
				field.setAccessible(true);
				fieldName = field.getName();
				Object value = map.get(fieldName);
				/*
				if there use setter , maybe count on java autobox problem.
				 */
				field.set(object, value);
			}
		} catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} 
		return object;
	}
}
