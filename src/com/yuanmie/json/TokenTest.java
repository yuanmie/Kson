package com.yuanmie.json;
import java.util.ArrayList;
import java.util.List;

public class TokenTest {

    private Token token;
    private String text = "";
    private int testcaseCounter;
    private int testcaseSuccess;

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public int getTestcaseCounter() {
        return testcaseCounter;
    }

    public void setTestcaseCounter(int testcaseCounter) {
        this.testcaseCounter = testcaseCounter;
    }

    public int getTestcaseSuccess() {
        return testcaseSuccess;
    }

    public void setTestcaseSuccess(int testcaseSuccess) {
        this.testcaseSuccess = testcaseSuccess;
    }

    public TokenTest(Token token) {
        this.token = token;
    }

    public void test(){
//        testPrimitive();
//        testBoolean();
//        testString();
        testNumber();

        print("success percent is " + (int)(((double)testcaseSuccess/testcaseCounter) * 100) + "%");
        print("sum test is " + testcaseCounter);
        print("success test is " + testcaseSuccess);
    }

    public void baseTest(String expect,String source){
        token.setText(expect);
        String actual = token.nextToken();
       ++testcaseCounter;
        if(actual.equals(expect)){
            ++testcaseSuccess;
            print("success at " + source + " expect is " +expect + " and actual is " + actual);
        }else{
            print("fail at " + source + " expect is " +expect + " and actual is " + actual);
        }
    }

    public void testNumber(){
        List<String> numberList = new ArrayList<String>();
//        numberList.add("0");
//        numberList.add("-0");
        numberList.add("0.123");
        numberList.add("0.123E");
        numberList.add("0.e");
        numberList.add("0.123E+132");
        numberList.add("0.123E-123");
        numberList.add("0.123E-");
        numberList.add("1");
        numberList.add("10");
        numberList.add("10.123");
        numberList.add("10.123E");
        numberList.add("10.e");
        numberList.add("103.123E-123");
        numberList.add("102.123E-");
        int caseCounter = 0;
       for(String number : numberList){
           baseTest(number, "testNumber" + ++caseCounter);
       }
    }

    public void testBoolean(){
        List<String> numberList = new ArrayList<String>();
        numberList.add("true");
        numberList.add("false");

        numberList.add("false2");
        numberList.add("falfffse");
        numberList.add("false");
        numberList.add("fasxlse");
        numberList.add("false");
        numberList.add("false9");
        
        int caseCounter = 0;
        for(String number : numberList){
            baseTest(number, "testNumber" + ++caseCounter);
        }
    }

    public void testPrimitive(){
        List<String> numberList = new ArrayList<String>();
        numberList.add("{");
        numberList.add("}");
        numberList.add(":");
        numberList.add(",");
        numberList.add("]");
        numberList.add("[");

        int caseCounter = 0;
        for(String number : numberList){
            baseTest(number, "testNumber" + ++caseCounter);
        }
    }

    public void testString(){
        List<String> strList = new ArrayList<String>();
        strList.add("\"0\"");
        strList.add("\"-0\"");
        strList.add("\"0.123\"");
        strList.add("\"0.123E\"");
        strList.add("\"0.e\"");
        strList.add("\"0.123E+132\"");
        strList.add("\"0.123E-123\"");
        strList.add("\"0.123E-\"");
        strList.add("\"1\"");
        strList.add("\"10\"");
        strList.add("\"true\"");
        strList.add("\"false\"");
        strList.add("\"null\"");
        strList.add("\"103.123E-123\"");
        strList.add("\"102.123E-\"");
        int caseCounter = 0;
        for(String str : strList){
            baseTest(str, "teststr" + ++caseCounter);
        }
    }



    public void print(String str){
        System.out.println(str);
    }
    public static void  main(String args[]){
        String text = "{true,false,null,0}";
        Token token = new Token(text);
        TokenTest tokenTest = new TokenTest(token);
        tokenTest.test();

    }
}
