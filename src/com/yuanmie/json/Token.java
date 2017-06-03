package com.yuanmie.json;

public class Token {
    private String text;
    private String type;
    private String numberType;
    private int currIndex = 0;
    private int text_length;
    private char[] charArray;

    public void setText(String text) {
        this.text = text;
        this.currIndex = 0;
        this.text_length = text.length();
        this.charArray = text.toCharArray();
    }

    public String getType() {
        return type;
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
            /*
                handle true
             */
            case 't':
                if (charArray[currIndex + 1] == 'r' && charArray[currIndex + 2] == 'u' && charArray[currIndex + 3] == 'e') {
                    currIndex += 4;
                    this.type = "true";
                    return "true";
                }
                errorToken();
                break;
            /*
                handle false
             */
            case 'f':
                if (charArray[currIndex + 1] == 'a' && charArray[currIndex + 2] == 'l' && charArray[currIndex + 3] == 's' && charArray[currIndex + 4] == 'e') {
                    currIndex += 5;
                    this.type = "false";
                    return "false";
                }
                errorToken();
                break;
            /*
                handle null
             */
            case 'n':
                if (charArray[currIndex + 1] == 'u' && charArray[currIndex + 2] == 'l' && charArray[currIndex + 3] == 'l') {
                    currIndex += 4;
                    this.type = "null";
                    return "null";
                }
                errorToken();
                break;

            /*
                handle object
             */
            case '{':
                this.type = "object";
                ++currIndex;
                return ch + "";
            case '[':
                this.type = "array";
                ++currIndex;
                return ch + "";
            /*
            It's no need to assign type for these token.
             */
            case '}':
            case ',':
            case ':':
            case ']':
                ++currIndex;
                return ch + "";
            /*
                handle string
             */
            case '"':
                oldIndex = currIndex;
                while (!eof() && charArray[++currIndex] != '"') { }

                if(eof()){
                    throw new RuntimeException("string quote not correct end!");
                }
                String result = dumpString(text.substring(oldIndex+1, currIndex));
                this.type = "string";
                ++currIndex;
                return result;

            case '-':
                sign = "-";
                ++currIndex;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            {
                oldIndex = currIndex;
                ch = charArray[currIndex];
                /*
                    handle integer part
                 */
                if (isDigit(ch)) {
                    this.type = "number";
                    this.numberType = "int";
                    currIndex++;
                    if (ch != '0') {
                        while (!eof() && isDigit(charArray[currIndex])) {
                            currIndex++;
                        }
                    }
                    /*
                        handle double part
                     */
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
                }
                /*
                    case: there isn't digit but other thing after sign symbol.
                    For example： -aaa
                 */
                else{
                    errorToken();
                }
            }
            default:
                    errorToken();
        }
        return "";
    }

    private void errorToken() {
        throw new RuntimeException("invalid token at " + currIndex);
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
    
    public String peekToken(){
    	int oldIndex = currIndex;
    	String result = nextToken();
    	currIndex = oldIndex;
    	return result;
    }
}
