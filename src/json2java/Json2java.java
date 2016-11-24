package json2java;

import java.lang.reflect.*;
import java.util.*;

import entity.Employee;
import entity.PC;

import java2json.Java2Json;

import util.FieldUtil;

public class Json2java {
	/**
	 * convert json data to java object
	 * 
	 * @param json
	 * @param o
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchFieldException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws InterruptedException 
	 */
	public static Object convert(String json, Class<?> clazz)
			throws InstantiationException, IllegalAccessException,
			SecurityException, NoSuchMethodException, NoSuchFieldException,
			IllegalArgumentException, InvocationTargetException,
			ClassNotFoundException, InterruptedException {
		Thread.sleep(1000);
		Object object = clazz.newInstance();
		List<String> key_value = parseJson(json);
		for (int index = 0; index < key_value.size(); index += 2) {
			String key = key_value.get(index);
			String value = key_value.get(index + 1);
			//
			Field field = FieldUtil.getFIeld(key, clazz);
			Class<?> fieldType = field.getType();

			/*
			 * 拼接setXXX函数 取得setXXX函数对象
			 */
			String methodName = "set" + key.substring(0, 1).toUpperCase()
					+ key.substring(1);
			Method method = clazz.getMethod(methodName, fieldType);

			System.out.println(methodName);
			System.out.println(value);

			/*
			 * 根据fieldType不同而采取不同的策略
			 * 如果不是字符串类型
			 */
			if (fieldType.getSimpleName().matches("^[i|I|d|D|f|F].*$")) {
				
				/*
				 * 处理数组
				 */
				if (fieldType.getSimpleName().endsWith("[]")) {
					String[] array = value.substring(1, value.length() - 1)
							.split(",");
					Class<?> lx = fieldType.getComponentType();
					Class<?> bl = Class.forName(Wrap(fieldType
							.getComponentType().getSimpleName()));
					Method method2 = bl.getMethod("valueOf", String.class);
					Constructor<?> con = bl.getConstructor(String.class);
					Object instance = con.newInstance(new Object[] { "0" });
					int i = 0;

					if (lx.getSimpleName().equals("int")) {
						int[] a = new int[array.length];
						for (String str : array) {
							a[i++] = (Integer) method2.invoke(instance,
									str.trim());
						}
						method.invoke(object, a);
					} else if (lx.getSimpleName().equals("Integer")) {
						Integer[] a = new Integer[array.length];
						for (String str : array) {
							a[i++] = (Integer) method2.invoke(instance,
									str.trim());
						}
						method.invoke(object, new Object[] { a });
					} else if (lx.getSimpleName().equals("double")) {
						double[] a = new double[array.length];
						for (String str : array) {
							a[i++] = (Double) method2.invoke(instance,
									str.trim());
						}
						method.invoke(object, a);
					} else if (lx.getSimpleName().equals("Double")) {
						Double[] a = new Double[array.length];
						for (String str : array) {
							a[i++] = (Double) method2.invoke(instance,
									str.trim());
						}
						method.invoke(object, new Object[] { a });
					} else if (lx.getSimpleName().equals("float")) {
						float[] a = new float[array.length];
						for (String str : array) {
							a[i++] = (Float) method2.invoke(instance,
									str.trim());
						}
						method.invoke(object, a);
					} else if (lx.getSimpleName().equals("Float")) {
						Float[] a = new Float[array.length];
						for (String str : array) {
							a[i++] = (Float) method2.invoke(instance,
									str.trim());
						}
						method.invoke(object, new Object[] { a });
					}
					/*
					 * 不是数组，是基本类型，将字符串转出需要的类型
					 */
				} else {
					Class<?> bl = Class
							.forName(Wrap(fieldType.getSimpleName()));
					Method method2 = bl.getMethod("valueOf", String.class);
					Constructor<?> con = bl.getConstructor(String.class);
					method.invoke(object, method2.invoke(
							con.newInstance(new Object[] { "0" }), value));
				}
			/*
			 * 如果是字符串类型
			 */
			}else if(fieldType.getSimpleName().equals("String")){
				method.invoke(object, value);
			}
			else {
				Object temp = convert(value, fieldType);
				method.invoke(object, temp);
			}
		}
		return object;
	}

	public static String Wrap(String name) {
		if (name.equals("int")) {
			return "java.lang.Integer";
		} else {
			return "java.lang." + name.substring(0, 1).toUpperCase()
					+ name.substring(1);
		}
	}

	public static void main(String args[]) throws InstantiationException,
			IllegalAccessException, SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			NoSuchFieldException, InvocationTargetException,
			ClassNotFoundException, InterruptedException {
		Employee e = new Employee();
		e.setAge(20);
		e.setId("1111");
		e.setName("bond");
		e.setSalary(100000.5);
		e.setSex("male");
		e.setCards(new int[] { 1, 22, 333 });
		e.setDisks(new double[] { 1.1, 2, 22, 3, 44 });
		e.setIis(new Integer[]{1,11111,222222});
		
		PC pc = new PC();
		pc.setBrand("dell");
		pc.setPrice(5000);
		e.setPc(pc);
		System.out.println("java convert json");
		String test = Java2Json.convert(e);
		System.out.println(test);
		System.out.println("json convert java");
		Employee person = (Employee) convert(test, Employee.class);
		System.out.println(person.toString());
		System.out.println("------------------PC:"+person.getPc().getBrand());
		// parseJson("{salary:100000.5,cards:[1, 22, 333],name:bond,sex:male,id:1111,age:20}");
	}

	/**
	 * 
	 * @param json
	 * @return
	 */
	public static boolean validJson(String json) {
		boolean valid = true;
		String[] vars = json.split(":");
		String var = null;
		for (int index = 0; index < vars.length; ++index) {
			var = vars[index];
			if (index == 0 && !var.startsWith("{")) {
				valid = false;
			} else if (index == vars.length - 1 && !var.endsWith("}")) {
				valid = false;
			} else {
				if (index != 0 && index != vars.length - 1) {
					if (var.length() < 1 || !var.contains(",")) {
						valid = false;
					} else {
						int i = var.lastIndexOf(',');
						String s1 = var.substring(0, i);
						String s2 = var.substring(i + 1);
						int a = s1.indexOf(",");
						int b = s1.indexOf("[");
						int c = s1.lastIndexOf(",");
						int d = s1.lastIndexOf("]");
						if (a < b || c > d) {
							valid = false;
						}
					}
				}
			}
		}
		return valid;
	}

	/**
	 * 
	 * @param json
	 * @return
	 */
	// {salary:100000.5,cards:[1, 22, 333],name:bond,sex:male,id:1111,age:20}
	public static List<String> parseJson(String json) {

		if (!validJson(json)) {
			System.out.println("json format error!");
			return null;
		}
		String[] vars = json.split(":");

		List<String> list = new LinkedList<String>();
		for (String var : vars) {
			if (var.startsWith("{")) {
				list.add(var.substring(1).trim());
			} else if (var.endsWith("}")) {
				list.add(var.substring(0, var.length() - 1).trim());
			} else {
				int index = var.lastIndexOf(',');
				list.add(var.substring(0, index).replaceAll("\\s", ""));
				list.add(var.substring(index + 1).trim());
			}
		}
		return list;
	}
}
