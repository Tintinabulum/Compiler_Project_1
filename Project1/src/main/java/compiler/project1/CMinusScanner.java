package compiler.project1;
import java.io.BufferedReader;
import java.io.IOException;
public class CMinusScanner implements Scanner {
    private BufferedReader inFile;
    private Token nextToken;
    public CMinusScanner (BufferedReader file) {
        inFile = file;
        nextToken = scanToken();
    }
    
    public Token getNextToken () {
        Token returnToken = nextToken;
        if (nextToken.getType() != Token.TokenType.EOF)
            nextToken = scanToken();
        return returnToken;
    }
    public Token viewNextToken () {
        return nextToken;
    }
    
    public Token scanToken(){
        try{
            int next = inFile.read();
            while(next!=-1){//-1 means EOF
                switch((char)next){
                    case '+': return new Token(Token.TokenType.ADD);
                    case '-': return new Token(Token.TokenType.SUB);
                    case '*': return new Token(Token.TokenType.MULT);
                    case '/':
                        inFile.mark(0);
                        next = inFile.read();
                        if((char)next!='*'){
                            inFile.reset();
                            return new Token(Token.TokenType.DIV);
                        }
                        boolean wasStar = false;
                        next = inFile.read();
                        while(!(wasStar && (char)next=='/')){
                            wasStar=next=='*';
                            next = inFile.read();
                        }
                        return new Token(Token.TokenType.COMMENT);
                    //Needs more cases
                    default: return new Token(Token.TokenType.ERROR,"Unknown symbol: "+(char)next);
                }
            }
        }catch(IOException error){//Can happen for comments
             return new Token(Token.TokenType.BEGCOM);
        }
        return new Token(Token.TokenType.EOF); 
    }
}