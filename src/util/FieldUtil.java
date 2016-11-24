package util;

import java.util.Arrays;
import java.util.List;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.lang.reflect.*;
public class FieldUtil {
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static Field[] getAllField(Class<?> clazz){
		
		List<Field> fieldList = new LinkedList<Field>();
		Field[] fields = clazz.getDeclaredFields();
		if(fields != null && fields.length > 0){
			fieldList.addAll(Arrays.asList(fields));
		}
		
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != Object.class){
			Field[] superField = getAllField(superClass);
			if(superField != null && superField.length > 0){
				for(Field field : superField){
				if(!contain(fieldList, field)){
					fieldList.add(field);
				}
				}
			}
		}
		Field[] resultFields = new Field[fieldList.size()];
		fieldList.toArray(resultFields);
		return resultFields;
	}
	
	public static boolean contain(List<Field> fieldList, Field field){
		for(Field f:fieldList){
			if (f.getName().equals(field.getName())){
				return true;
			}
		}
		return false;
	}
	
	public static Field getFIeld(String fieldName, Class<?> clazz){
		Field[] fields = getAllField(clazz);
		for(Field field : fields){
			if(field.getName().contains(fieldName)){
				return field;
			}
		}
		return null;
	}
}
