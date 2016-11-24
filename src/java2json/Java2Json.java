package java2json;

import entity.Employee;
import entity.PC;
import util.FieldUtil;

import java.lang.reflect.*;
import java.util.*;

public class Java2Json {
    private static List<String> buildInClassList;
    private static String template = "\"%s\":%s";
    private static String template_string = "\"%s\":\"%s\"";

    static {
        String[] buildInClass = new String[]{
                "Integer", "int",
                "Float", "float",
                "Double", "double",
                "String",
                "Boolean", "boolean",
                "Char", "char",
                "Short", "short",
                "Long", "long"
        };
        buildInClassList = Arrays.asList(buildInClass);
    }

    /**
     * @param o 为了能让所有的对象都调用这个函数
     * @return json String
     */
    public static String convert(Object o) {
        if(o == null) return null;
        Method method = null;
        String methodName = null;
        // 类属性的类型的Class对象和类型名
        Class<?> typeClass = null;
        String typeName = null;
        // 获得Class对象
        Class<?> clazz = o.getClass();
        String className = clazz.getSimpleName();
        System.out.println(className);
        String item;
        if(buildInClassList.contains(className)){
            if(className.equals("String")) return "\"" + o.toString() + "\"";
           return o.toString();
        }else if(className.endsWith("[]")){
            return toJson_array(o);
        }
        else if(className.endsWith("List")){
            List<? extends Object> list = (List<? extends Object>)o;
            Class<?> paramType = list.get(0).getClass();
            return toJson_list(list, paramType.getSimpleName());
        }else if(className.endsWith("Map")){
            StringBuffer sb = new StringBuffer();
            sb.append("{");
            Map<Object, Object> map = (Map<Object, Object>) o;
            for (Object key : map.keySet()) {
                Object value = map.get(key);
                item = String.format(template, key.toString(),
                        convert(value));
                sb.append(item);
                sb.append(",");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            sb.append("}");
            return sb.toString();
        }else{
            // 用stringbuffer来容纳json字符串
            StringBuffer resultString = new StringBuffer("{");

            Field[] fields = FieldUtil.getAllField(clazz);
            for (Field field : fields) {
                String fieldName = field.getName();

                // 获得Field的类型Class和名字
                typeClass = field.getType();
                typeName = typeClass.getSimpleName();

                try {
                    // 获取函数
                    // 拼接getXXX方法
                    methodName = "get" + fieldName.substring(0, 1).toUpperCase()
                            + fieldName.substring(1);
                    method = clazz.getMethod(methodName);

                    // 处理基本类型
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

            // 消除最后一个多余的逗号
            resultString.deleteCharAt(resultString.lastIndexOf(","));
            resultString.append("}");
            return resultString.toString();
        }


    }


    private static String toJson_list(List<? extends Object> list, String className) {
        StringBuffer arrayString = new StringBuffer();
        arrayString.append("[");
        for (Object o : list) {
            if (buildInClassList.contains(className)) {
                if (className.equals("String")) {
                    arrayString.append("\"" + o.toString() + "\",");
                } else {
                    arrayString.append(o.toString() + ",");
                }

            } else {
                arrayString.append(convert(o) + ",");
            }
        }
        arrayString.append("]");
        arrayString.deleteCharAt(arrayString.lastIndexOf(","));
        return arrayString.toString();
    }


    private static String toJson_array(Object array) {
        StringBuffer arrayString = new StringBuffer();
        arrayString.append("[");
        for (int index = 0; index < Array.getLength(array); index++) {
            Object o = Array.get(array, index);
            String className = o.getClass().getSimpleName();
            if (buildInClassList.contains(className)) {
                if (className.equals("String")) {
                    arrayString.append("\"" + o.toString() + "\",");
                } else {
                    arrayString.append(o.toString() + ",");
                }

            } else {
                arrayString.append(convert(o) + ",");
            }
        }
        arrayString.append("]");
        arrayString.deleteCharAt(arrayString.lastIndexOf(","));
        return arrayString.toString();
    }

    /**
     * 测试用例，一个继承object的person类，和继承person类的employee类
     *
     * @param args
     * @throws SecurityException
     * @throws NoSuchFieldException
     */
    public static void main(String args[]) throws SecurityException,
            NoSuchFieldException {
        Employee e = new Employee();
        e.setAge(20);
        e.setId("1111");
        e.setName("bond");
        e.setSalary(100000.59999);
        e.setSex("male");
        e.setCards(new int[]{1, 22, 333});
        e.setDisks(new double[]{1.1, 2, 22, 3, 44});
        e.setIis(new Integer[]{1, 11111, 222222});
        e.setTt(new String[]{"hhh", "kkk"});


        PC pc = new PC();
        pc.setBrand("dell");
        pc.setPrice(5000);
        e.setPc(null);
        e.setPcs(new PC[]{pc, pc});
        Map<String, PC> pcmaps = new HashMap<String, PC>();
        pcmaps.put("one", pc);
        pcmaps.put("two", pc);
        e.setPclist(Arrays.asList(new PC[]{pc, pc}));
        e.setPcMap(pcmaps);
        Map<String, List<PC>> difficults = new HashMap<String, List<PC>>();
        difficults.put("hello", Arrays.asList(new PC[]{pc, pc}));
        difficults.put("world", Arrays.asList(new PC[]{pc, pc}));
        e.setDifficult(difficults);
        System.out.println(convert(e));
    }
}
