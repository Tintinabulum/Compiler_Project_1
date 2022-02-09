package compiler.project1;
import java.io.BufferedReader;
public class CMinusScanner implements Scanner {
    private BufferedReader inFile;
    private Token nextToken;
    public CMinusScanner (BufferedReader file) {
        inFile = file;
        nextToken = scanToken();
    }
    public Token scanToken(){
        return null; //NEEDS TO BE IMPLEMENTED
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
}