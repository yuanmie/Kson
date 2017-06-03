package test;
import com.yuanmie.json.Parser;
import com.yuanmie.json.Token;

import java.util.Map;


public class ParserTest {
    public static void main(String args[]){
        String text = "{\"one\":true,\"two\":false,\"three\":10.23E+1,\"four\":[1,2,3],\"five\":"
        		+ "{\"one\":true,\"two\":false,\"three\":10.23E+1,\"four\":[1,2,3]}" +
                ",\"six\":[{\"one\":true,\"two\":false,\"three\":10.23E+1,\"four\":[1,2,3]}" +
                ",{\"one\":true,\"two\":false,\"three\":10.23E+1,\"four\":[1,2,3]}]}";
        Token token = new Token(text);
        //System.out.println(token.nextToken());
        Parser parser = new Parser(token);
        //System.out.println(token.nextToken());
        Map<String, Object> map = parser.parse();
        for(Object key : map.keySet()){
            System.out.printf("%s:%s\n", key, map.get(key).toString());
        }
        
    }
}
