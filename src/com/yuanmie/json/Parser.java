package com.yuanmie.json;

import java.util.*;

public class Parser {
    private Token token;

    public Parser(Token token){
        this.token = token;
    }

    public void reset(String text){
        token.setText(text);
    }

    /*
        start parse json string
     */
    public Map<String, Object> parse(){
        expect("{");
        return parse_object();
    }

    /*
        the json object correspond to java map
     */
    public Map<String, Object> parse_object(){
        Map<String, Object> result = new HashMap<String, Object>();

        String t = token.nextToken();
        /*
            loop handle key:value
         */
        while(token.getType().equals("string")){
            expect(":");
            Object o = parse_value();
            result.put(removeQuote(t), o);
            t = token.nextToken();
            if(!t.equals(",") && !t.equals("}")){
                throw new RuntimeException("object format not correct!");
            }
            if(t.equals("}")) break;
            t = token.nextToken();
        }

        if(!t.equals("}")){
            throw new RuntimeException("object not correct end!");
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
            }else if("double".equals(token.getNumberType())){
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
            throw new RuntimeException("value format error!!!");
        }
        return null;
    }

    private List<? extends Object> parse_array() {
        List<Object> result = new ArrayList<Object>();

        /*
            use peekToken rather than nextToken, in order to correct parse value.
            The value will lose a token information if here is nextToken.
         */
        String t = token.peekToken();
        while(!t.equals("]")){
             Object o = parse_value();
             result.add(o);
             t = token.nextToken();
             if(t.equals("]")){
                 break;
             }
             if(!t.equals(",")){
                 throw new RuntimeException("Syntax error!!!");
             }

            t = token.peekToken();
        }

        if(!t.equals("]")){
            throw new RuntimeException("the array not correct end!");
        }
        return result;
    }


    public void expect(String t){
        String tmp = token.nextToken();
        if(!t.equals(tmp)){
            throw new RuntimeException("expect '" + t +  "' !");
        }
    }

    public void print(String str){
        System.out.println(str);
    }

    
    public String removeQuote(String str){
    	return str.substring(1, str.length()-1);
    }
}
