package xml2json;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.*;

public class Xml2Json {
	public static String convert(String xmlPath){
		//Map<String, String > map = new HashMap<String, String>();
		StringBuffer json = new StringBuffer();
		try{
			json.append("{");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(new File(xmlPath));
			NodeList nl = document.getElementsByTagName("json");
			for(int index = 0; index < nl.getLength(); index++){
				Element e = (Element) nl.item(index);
				NodeList child = e.getChildNodes();
				for(int i = 0; i < child.getLength(); i++){
					Node item = child.item(i);
					if(item.getNodeName() == "#text"){
						continue;
					}
					String key = item.getNodeName();
					String value = item.getTextContent();
					json.append(String.format("%s:%s,",key,value));
					//map.put(key, value);
				}
			}
			
			json.append("}");
			json.deleteCharAt(json.lastIndexOf(","));
		}catch(Exception e){
			e.printStackTrace();
		}
		return json.toString();
		//return ConstructJson(map);
	}
	
	public static void main(String args[]){
		System.out.println(convert("src/test2.xml"));
	}
}
