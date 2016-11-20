package com.yuanmie.json;

public class KsonTest {
	public static void main(String args[]){
		Kson kson = new Kson();
		Entity entity = kson.fromJson("{\"one\":true,\"two\":false,\"three\":10.23E+1,\"four\":[1,2,3],\"five\":"
        		+ "{\"one\":true,\"two\":false,\"three\":10.23E+1,\"four\":[1,2,3]}}", Entity.class);
		System.out.println(entity);
	}
}
