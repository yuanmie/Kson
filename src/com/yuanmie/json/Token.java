package com.yuanmie.json;


import java.io.UnsupportedEncodingException;

public class Token {
    private String text;
    private String type;
    private String numberType;
    private int currIndex = 0;
    private int text_length;
    private char[] charArray;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.currIndex = 0;
        this.text_length = text.length();
        this.charArray = text.toCharArray();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCurrIndex() {
        return currIndex;
    }

    public void setCurrIndex(int currIndex) {
        this.currIndex = currIndex;
    }

    public int getText_length() {
        return text_length;
    }

    public void setText_length(int text_length) {
        this.text_length = text_length;
    }

    public Token() {
        this.currIndex = 0;
        this.text_length = 0;
    }

    public Token(String text) {
        this.text = text;
        this.text_length = text.length();
        this.currIndex = 0;
        this.charArray = text.toCharArray();
    }

    public String nextToken() {
        if (eof()) return "";

        char ch = charArray[currIndex];
        String sign = "";
        int oldIndex = 0;
        switch (ch) {
            case 't':
                if (charArray[currIndex + 1] == 'r' && charArray[currIndex + 2] == 'u' && charArray[currIndex + 3] == 'e') {
                    currIndex += 4;
                    this.type = "true";
                    return "true";
                }

                break;
            case 'f':
                if (charArray[currIndex + 1] == 'a' && charArray[currIndex + 2] == 'l' && charArray[currIndex + 3] == 's' && charArray[currIndex + 4] == 'e') {
                    currIndex += 5;
                    this.type = "false";
                    return "false";
                }
                break;
            case 'n':
                if (charArray[currIndex + 1] == 'u' && charArray[currIndex + 2] == 'l' && charArray[currIndex + 3] == 'l') {
                    currIndex += 4;
                    this.type = "null";
                    return "null";
                }
                break;

            case '{':
                this.type = "object";
                ++currIndex;
                return ch + "";
            case '}':
            case ',':
            case ':':
            case '[':
                this.type = "array";
            case ']':
                ++currIndex;
                return ch + "";
            //string token
            case '"':
                oldIndex = currIndex;
                while (!eof() && charArray[++currIndex] != '"') {
                    ;
                }
                ++currIndex;
                this.type = "string";
                return dumpString(text.substring(oldIndex+1, currIndex-1));

            case '-':
                sign = "-";
                ++currIndex;
            default:
                oldIndex = currIndex;
                ch = charArray[currIndex];
                if (ch >= '0' && ch <= '9') {
                    this.type = "number";
                    this.numberType = "int";
                    currIndex++;
                    if (ch == '0') {

                    } else {
                        while (!eof() && isDigit(charArray[currIndex])) {
                            currIndex++;
                        }
                    }
                    if (!eof() && charArray[currIndex] == '.') {
                        this.numberType = "double";
                        ++currIndex;
                        while (!eof() && isDigit(charArray[currIndex])) {
                            currIndex++;
                        }

                        if (!eof() && (charArray[currIndex] == 'e' || charArray[currIndex] == 'E')) {
                            ++currIndex;
                            if (!eof() && (charArray[currIndex] == '+' || charArray[currIndex] == '-')) {
                                ++currIndex;
                            }
                            while (!eof() && isDigit(charArray[currIndex])) {
                                currIndex++;
                            }
                        }
                    }
                    return sign + text.substring(oldIndex, currIndex);
                }else {
                    throw new RuntimeException("lex error!");
                }

        }
        return "";
    }

     public String dumpString(String string) {
        byte[] src = string.getBytes();
        StringBuffer buf = new StringBuffer();
        buf.append("\"");
        for (int n = 0; n < src.length; n++) {
            int c = toUnsigned(src[n]);
            if (c == '"') buf.append("\\\"");
            else if (isPrintable(c)) buf.append((char)c);
            else if (c == '\b') buf.append("\\b");
            else if (c == '\t') buf.append("\\t");
            else if (c == '\n') buf.append("\\n");
            else if (c == 013) buf.append("\\v");
            else if (c == '\f') buf.append("\\f");
            else if (c == '\r') buf.append("\\r");
            else {
                buf.append("\\" + Integer.toOctalString(c));
            }
        }
        buf.append("\"");
        return buf.toString();
    }

     private int toUnsigned(byte b) {
        return b >= 0 ? b : 256 + b;
    }

     public boolean isPrintable(int c) {
        return (' ' <= c) && (c <= '~');
    }
    public Boolean eof() {
        return this.currIndex >= this.text_length;
    }

    public boolean isDigit(int c) {
        return c >= '0' && c <= '9';
    }

    public String getNumberType() {
        return numberType;
    }

    public void setNumberType(String numberType) {
        this.numberType = numberType;
    }
    
    public String peekToken(){
    	int oldIndex = currIndex;
    	String result = nextToken();
    	currIndex = oldIndex;
    	return result;
    }
}
