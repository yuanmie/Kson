package json2xml;

import java.util.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import json2java.Json2java;
import org.w3c.dom.*;
public class Json2Xml {
	public static void convert(String json){
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.newDocument();
			Element root = document.createElement("json2xml");
			Element jsonElement = document.createElement("json");
			List<String> jsonParse = Json2java.parseJson(json);
			for(int i = 0; i < jsonParse.size(); i += 2){
				String key = jsonParse.get(i);
				String value = jsonParse.get(i + 1);
				System.out.println("----------:"+key);
				Element keyElement = document.createElement(key);
				keyElement.appendChild(document.createTextNode(value));
				jsonElement.appendChild(keyElement);
			}
			root.appendChild(jsonElement);
			document.appendChild(root);
			TransformerFactory tFactory =
				    TransformerFactory.newInstance();
				    Transformer transformer = 
				    tFactory.newTransformer();

				    DOMSource source = new DOMSource(document);
				    StreamResult result = new StreamResult(new FileOutputStream("src/test2.xml"));
				    transformer.transform(source, result);
			//((XmlDocument)document).write(new FileOutputStream("src/test.xml"));
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String args[]){
		String json = "{salary:100000.5,cards:[1, 22, 333],disks:[1.1, 2.0, 22.0, 3.0, 44.0],name:bond,sex:male,id:1111,age:20}";
		convert(json);
	}
}
