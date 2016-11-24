package concurrency;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import entity.Employee;

import json2java.Json2java;
public class ConcurrencyParseJson2Java {
	final static int nThreads = 10;
	public static List<?> convert(String[] json, Class<?> clazz) throws InterruptedException, ExecutionException{
		ExecutorService exec = Executors.newFixedThreadPool(nThreads);
		List list = new LinkedList();
		for(int i = 0; i < nThreads; i++){
			Future<?> future = exec.submit(new Handle(json,(json.length / nThreads ) * i, (i + 1) * json.length / nThreads, clazz ));
			list.addAll((Collection) future.get());
		}
		return list;
	}
	
	
	public static List<?> convert2(String[] json, Class<?> clazz) throws InterruptedException, ExecutionException{
		ExecutorService exec = Executors.newFixedThreadPool(nThreads);
		List list = new LinkedList();
		for(int i = 0; i < json.length; i++){
			try {
				list.add(Json2java.convert(json[i], clazz));
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@SuppressWarnings("unused")
	private static class Handle implements Callable<List<?>>{
		private String[] json;
		private int start;
		private int end;
		private Class clazz;
		
		public Handle(String[] json, int start, int end, Class clazz) {
			// TODO Auto-generated method stub
			this.json = json;
			this.start = start;
			this.end = end;
			this.clazz = clazz;
		}
		@Override
		public List<?> call() throws Exception {
			// TODO Auto-generated method stub
			//System.out.println(Thread.currentThread().getName() + " is parse json!!!");
			List list = new LinkedList();
			for(String str : json){
				list.add(Json2java.convert(str, clazz));
			}
			return list;
		}
		
	}
	
	public static void main(String args[]) throws InterruptedException, ExecutionException{
		String[] json = new String[]{
				"{salary:100000.5,cards:[1,22,333],disks:[1.1,2.0,22.0,3.0,44.0],iis:[1,11111,222222],name:bond,sex:male,id:1111,age:20}",
				"{salary:100000.5,cards:[1,22,333],disks:[1.1,2.0,22.0,3.0,44.0],iis:[1,11111,222222],name:bond,sex:male,id:1111,age:20}", 
				"{salary:100000.5,cards:[1,22,333],disks:[1.1,2.0,22.0,3.0,44.0],iis:[1,11111,222222],name:bond,sex:male,id:1111,age:20}",
				"{salary:100000.5,cards:[1,22,333],disks:[1.1,2.0,22.0,3.0,44.0],iis:[1,11111,222222],name:bond,sex:male,id:1111,age:20}",
				"{salary:100000.5,cards:[1,22,333],disks:[1.1,2.0,22.0,3.0,44.0],iis:[1,11111,222222],name:bond,sex:male,id:1111,age:20}", 
				"{salary:100000.5,cards:[1,22,333],disks:[1.1,2.0,22.0,3.0,44.0],iis:[1,11111,222222],name:bond,sex:male,id:1111,age:20}",
				"{salary:100000.5,cards:[1,22,333],disks:[1.1,2.0,22.0,3.0,44.0],iis:[1,11111,222222],name:bond,sex:male,id:1111,age:20}",
				"{salary:100000.5,cards:[1,22,333],disks:[1.1,2.0,22.0,3.0,44.0],iis:[1,11111,222222],name:bond,sex:male,id:1111,age:20}", 
				"{salary:100000.5,cards:[1,22,333],disks:[1.1,2.0,22.0,3.0,44.0],iis:[1,11111,222222],name:bond,sex:male,id:1111,age:20}",
				"{salary:100000.5,cards:[1,22,333],disks:[1.1,2.0,22.0,3.0,44.0],iis:[1,11111,222222],name:bond,sex:male,id:1111,age:20}",
				"{salary:100000.5,cards:[1,22,333],disks:[1.1,2.0,22.0,3.0,44.0],iis:[1,11111,222222],name:bond,sex:male,id:1111,age:20}", 
				"{salary:100000.5,cards:[1,22,333],disks:[1.1,2.0,22.0,3.0,44.0],iis:[1,11111,222222],name:bond,sex:male,id:1111,age:20}",
				"{salary:100000.5,cards:[1,22,333],disks:[1.1,2.0,22.0,3.0,44.0],iis:[1,11111,222222],name:bond,sex:male,id:1111,age:20}",
				"{salary:100000.5,cards:[1,22,333],disks:[1.1,2.0,22.0,3.0,44.0],iis:[1,11111,222222],name:bond,sex:male,id:1111,age:20}", 
				"{salary:100000.5,cards:[1,22,333],disks:[1.1,2.0,22.0,3.0,44.0],iis:[1,11111,222222],name:bond,sex:male,id:1111,age:20}",
				"{salary:100000.5,cards:[1,22,333],disks:[1.1,2.0,22.0,3.0,44.0],iis:[1,11111,222222],name:bond,sex:male,id:1111,age:20}",
				"{salary:100000.5,cards:[1,22,333],disks:[1.1,2.0,22.0,3.0,44.0],iis:[1,11111,222222],name:bond,sex:male,id:1111,age:20}", 
				"{salary:100000.5,cards:[1,22,333],disks:[1.1,2.0,22.0,3.0,44.0],iis:[1,11111,222222],name:bond,sex:male,id:1111,age:20}",
				"{salary:100000.5,cards:[1,22,333],disks:[1.1,2.0,22.0,3.0,44.0],iis:[1,11111,222222],name:bond,sex:male,id:1111,age:20}",
				"{salary:100000.5,cards:[1,22,333],disks:[1.1,2.0,22.0,3.0,44.0],iis:[1,11111,222222],name:bond,sex:male,id:1111,age:20}", 
				"{salary:100000.5,cards:[1,22,333],disks:[1.1,2.0,22.0,3.0,44.0],iis:[1,11111,222222],name:bond,sex:male,id:1111,age:20}",
				"{salary:100000.5,cards:[1,22,333],disks:[1.1,2.0,22.0,3.0,44.0],iis:[1,11111,222222],name:bond,sex:male,id:1111,age:20}",
				"{salary:100000.5,cards:[1,22,333],disks:[1.1,2.0,22.0,3.0,44.0],iis:[1,11111,222222],name:bond,sex:male,id:1111,age:20}", 
				"{salary:100000.5,cards:[1,22,333],disks:[1.1,2.0,22.0,3.0,44.0],iis:[1,11111,222222],name:bond,sex:male,id:1111,age:20}",

		};
		Class clazz = Employee.class;
		long nowTime = System.currentTimeMillis();
		convert(json, clazz);
		long endTime = System.currentTimeMillis();
		long cost = endTime - nowTime;
		System.out.printf("this cost %d millis\n", cost);
	}
}
