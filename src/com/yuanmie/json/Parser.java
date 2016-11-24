package com.yuanmie.json;

import java.util.*;

/**
 * Created by Administrator on 2016/11/19 0019.
 */
public class Parser {
    private Token token;

    public Parser(Token token){
        this.token = token;
    }

    public Map<String, Object> parse(){
        expect("{");
        return parse_object();
    }

    public Map<String, Object> parse_object(){
        Map<String, Object> result = new HashMap<String, Object>();

        String t = token.nextToken();
        while(token.getType().equals("string")){
            expect(":");
            Object o = parse_value();
            result.put(removeQuote(t), o);
            t = token.nextToken();
            if(!t.equals(",") && !t.equals("}")){
                throw new RuntimeException("fffff");
            }
            if(t.equals("}")) break;
            t = token.nextToken();
        }

        return result;
    }

    public Object parse_value(){
        String t = token.nextToken();
        String type = token.getType();
        if("string".equals(type)){
            return removeQuote(t);
        }else if("number".equals(type)){
            if("int".equals(token.getNumberType())){
                return Integer.parseInt(t);
            }else if("float".equals(token.getNumberType())){
                return Double.parseDouble(t);
            }
        }else if("object".equals(type)){
            return parse_object();
        }else if("array".equals(type)){
            return parse_array();
        }else if("true".equals(type)){
            return Boolean.TRUE;
        }else if("false".equals(type)){
            return Boolean.FALSE;
        }else if("null".equals(type)){
            return null;
        }else{
            throw new RuntimeException("Syntax error!!!");
        }
        return null;
    }

    private List<? extends Object> parse_array() {
        List<Object> result = new ArrayList<Object>();
        String t = token.peekToken();
        while(!t.equals("]")){
        	 Object o = parse_value();
        	 t = token.nextToken();
        	 if(t.equals("]")){
        		 break;
        	 }
             if(!t.equals(",")){
                 throw new RuntimeException("Syntax error!!!");
             }
            result.add(o);
            t = token.peekToken();
        }
        return result;
    }

    public void expect(String t){
        String tmp = token.nextToken();
        if(t.equals(tmp)){

        }else{
            throw new RuntimeException("Syntax error!");
        }
    }

    public void print(String str){
        System.out.println(str);
    }
    public void reset(String text){
        token.setText(text);
    }
    
    public String removeQuote(String str){
    	return str.substring(1, str.length()-1);
    }
}
