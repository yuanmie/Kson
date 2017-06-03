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
                StringBuffer buf = new StringBuffer();
                buf.append("\"");
                int c;
                while (!eof() && ((c = charArray[++currIndex]) != '\"')) {
                    switch(c){
                        case '\\':
                            c = charArray[++currIndex];
                            switch(c){
                                case '\"': buf.append('\"'); break;
                                case '\\': buf.append('\\'); break;
                                case '/':  buf.append('/' ); break;
                                case 'b':  buf.append('\b'); break;
                                case 'f':  buf.append('\f'); break;
                                case 'n':  buf.append('\n'); break;
                                case 'r':  buf.append('\r'); break;
                                case 't':  buf.append('\t'); break;
                                case 'u':
                                    /*
                                        handle unicode character
                                     */
                                    ++currIndex;
                                    String code = text.substring(currIndex, currIndex + 4);
                                    if(invalidCode(code)){
                                        errorMessage("invalid unicode!!!");
                                    }

                                    int codePoint = computeCodePoint(code);
                                    if(Character.isHighSurrogate((char)codePoint)){
                                        currIndex += 4;
                                        int highCodePoint = codePoint;
                                        if(charArray[currIndex] == '\\' && charArray[currIndex + 1] == 'u'){
                                            currIndex += 2;
                                            String lowSurrogate = text.substring(currIndex, currIndex + 4);
                                            if(invalidCode(lowSurrogate)){
                                                errorMessage("invalid unicode!!!");
                                            }
                                            currIndex += 3;
                                            int lowCodePoint = computeCodePoint(lowSurrogate);
                                            if(Character.isLowSurrogate((char)lowCodePoint)){
                                                if(Character.isSurrogatePair((char)highCodePoint, (char)lowCodePoint)){
                                                    codePoint = Character.toCodePoint((char)highCodePoint, (char)lowCodePoint);
                                                    buf.append(Character.toChars(codePoint));
                                                }else{
                                                    errorMessage("invalid unicode surrogatePair");
                                                }
                                            }else{
                                                errorMessage("invalid unicode surrogatePair");
                                            }
                                        }else{
                                            errorMessage("expect unicode");
                                        }
                                    }
                                    else if(Character.isBmpCodePoint(codePoint)){
                                        currIndex += 3;
                                        buf.append(Character.toChars(codePoint));
                                    }else{
                                        errorMessage("invalid highSurrogate");
                                    }
                                    break;
                            }
                            break;
                        default:
                            if(isPrintable(c)){
                                buf.append((char)c);
                            }else{
                                errorToken();
                            }
                    }
                }

                if(eof()){
                    throw new RuntimeException("string quote not correct end!");
                }
                buf.append("\"");
                this.type = "string";
                ++currIndex;
                return buf.toString();

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

    private boolean invalidCode(String code) {
        boolean r = code.matches("[0-9a-fA-F]{4}");
        return !r;
    }

    private void errorToken() {
        throw new RuntimeException("invalid token at " + currIndex);
    }
    private void errorMessage(String message) {
        throw new RuntimeException(message + " at " + currIndex);
    }

    private boolean isPrintable(int c) {
        return (' ' <= c) && (c <= '~');
    }

    private boolean isValidHex(final int c) {
        int lc = Character.toLowerCase(c);
        return (('0' <= lc) && (lc <= '9')) || 'a' <= lc && lc <= 'f';
    }

    private static int computeCodePoint(String s) {

        char[] chars = s.toCharArray();
        char cc = 0;

        for (int j = 0; j < 4; j++) {
            cc <<= 4;
            char ch = Character.toLowerCase(chars[j]);
            if ('0' <= ch && ch <= '9' || 'a' <= ch && ch <= 'f') {
                cc |= Character.digit(ch, 16);
            }
        }
        return cc;
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
