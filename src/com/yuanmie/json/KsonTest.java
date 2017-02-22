package com.yuanmie.json;

public class KsonTest {
	public static void main(String args[]){
		Kson kson = new Kson();
		Entity entity = kson.fromJson("{\"str\":\"string_\ntest\",\"one\":true,\"two\":false,\"three\":10.0,\"four\":[1,2,3],\"five\":"
        		+ "{\"one\":true,\"two\":false,\"three\":10.0,\"four\":[1,2,3]}}", Entity.class);
		System.out.println(entity);
		System.out.print(kson.toJson(entity));
	}
}
