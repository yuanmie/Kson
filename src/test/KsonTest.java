package test;

import entity.Entity;
import com.yuanmie.json.Kson;

import java.nio.charset.Charset;

public class KsonTest {
	public static void main(String args[]){
		Kson kson = new Kson();
		Entity entity = kson.fromJson("{\"str\":\"string \\n \\\\ \\b \\f \\r \\t \\\" \\u0400 \\ud800\\udc00 \\u7f18\\u706d test\"" +
				",\"one\":true,\"two\":false,\"three\":10.0,\"four\":[1,2,3],\"five\":"
        		+ "{\"one\":true,\"two\":false,\"three\":10.0,\"four\":[1,2,null]}}", Entity.class);
		System.out.println(entity);
		System.out.print(kson.toJson(entity));

	}
}
