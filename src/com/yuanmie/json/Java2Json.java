package com.yuanmie.json;

import util.FieldUtil;
import java.lang.reflect.*;
import java.util.*;

/*
    TODO: 17-6-3 fix up nullpoint exception
 */
public class Java2Json {
    private static List<String> buildInTypeList;
    private static String template = "\"%s\":%s";

    /*
        construct build-in type list
     */
    static {
        String[] buildInType = new String[]{
                "Integer", "int",
                "Float", "float",
                "Double", "double",
                "String",
                "Boolean", "boolean",
                "Char", "char",
                "Short", "short",
                "Long", "long"
        };
        buildInTypeList = Arrays.asList(buildInType);
    }

    /**
     * @param o
     * @return json String
     */
    public static String convert(Object o) {
        if(o == null) return null;

        Class<?> clazz = o.getClass();
        String className = clazz.getSimpleName();
        String item = "";
        /*
            handle build-in type, just return their string represent
         */
        if(buildInTypeList.contains(className)){
            if(className.equals("String")){
                return "\"" + o.toString() + "\"";
            }
            return o.toString();
        }
        /*
            handle array type
         */
        else if(className.endsWith("[]")){
            return toJson_array(o);
        }
        /*
            handle list type
         */
        else if(className.endsWith("List")){
            List<? extends Object> list = (List<? extends Object>)o;
            return toJson_list(list);
        }
        /*
            handle map type
         */
        else if(className.endsWith("Map")){
            StringBuffer sb = new StringBuffer();
            sb.append("{");
            Map<Object, Object> map = (Map<Object, Object>) o;
            for (Object key : map.keySet()) {
                Object value = map.get(key);
                /*
                    hypothesis:key is built-in type
                    if you don't like this hypothesis, you can do it by this way:
                    item = String.format(template, convert(key),
                        convert(value));
                 */
                item = String.format(template, key.toString(),
                        convert(value));
                sb.append(item);
                sb.append(",");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            sb.append("}");
            return sb.toString();
        }
        /*
            handle custom type
         */
        else{
            Method method = null;
            String methodName = null;
            StringBuffer resultString = new StringBuffer("{");

            Field[] fields = FieldUtil.getAllField(clazz);
            for (Field field : fields) {
                String fieldName = field.getName();
                try {
                    /*
                        construct field getter method
                     */
                    methodName = "get" + fieldName.substring(0, 1).toUpperCase()
                            + fieldName.substring(1);
                    method = clazz.getMethod(methodName);
                    item = String.format(template, field.getName(),
                            convert(method.invoke(o)));
                    resultString.append(item);
                    resultString.append(",");
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            resultString.deleteCharAt(resultString.lastIndexOf(","));
            resultString.append("}");
            
            return resultString.toString();
        }
    }


    private static String toJson_list(List<? extends Object> list) {
        /*
            check list whether is empty
        */
        if(list.size() < 1){
            return "[]";
        }

        Class<?> elementType = list.get(0).getClass();
        String className = elementType.getSimpleName();

        StringBuffer listString = new StringBuffer();
        listString.append("[");
        for (Object o : list) {
            if (buildInTypeList.contains(className)) {
                if (className.equals("String")) {
                    listString.append("\"" + o.toString() + "\",");
                } else {
                    listString.append(toString(o) + ",");
                }
            }else {
                listString.append(convert(o) + ",");
            }
        }
        listString.append("]");
        listString.deleteCharAt(listString.lastIndexOf(","));
        return listString.toString();
    }

    private static String toJson_array(Object array) {
        StringBuffer arrayString = new StringBuffer();
        arrayString.append("[");
        for (int index = 0; index < Array.getLength(array); index++) {
            Object o = Array.get(array, index);
            String className = o.getClass().getSimpleName();
            if (buildInTypeList.contains(className)) {
                if (className.equals("String")) {
                    arrayString.append("\"" + o.toString() + "\",");
                } else {
                    arrayString.append(toString(o) + ",");
                }
            } else {
                arrayString.append(convert(o) + ",");
            }
        }
        arrayString.append("]");
        arrayString.deleteCharAt(arrayString.lastIndexOf(","));
        return arrayString.toString();
    }

    private static String toString(Object o){
        return o == null ? "null" : o.toString();
    }
}
