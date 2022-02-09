package compiler.project1;

public class Token {
    public enum TokenType{
        ELSE, IF, INT, RETURN, VOID, WHILE, //Page 491 #1
        ADD, SUB, MULT, DIV, LESS, LESSEQUAL, GREATER, GREATEREQUAL, //Page 491 #2
        EQUAL, NOTEQUAL, ASSIGN, SEMICOLON, COMA, BEGPAR, ENDPAR, //Page 491, #2
        BEGSBRA, ENDSBRA, BEGBRA, ENDBRA, BEGCOM, ENDCOM, //Page 491, #2
        ID, NUM, //Page 492 #3
        EOF
    }
    private TokenType tokenType;
    private Object tokenData;
    public Token (TokenType type) {
        this (type, null);
    }
    public Token (TokenType type, Object data) {
        tokenType = type;
        tokenData = data;
    }
    
    public TokenType getType(){return tokenType;}
    public Object getData(){return tokenData;}
    public void setType(TokenType type){tokenType=type;}
    public void setData(Object data){tokenData=data;}
}