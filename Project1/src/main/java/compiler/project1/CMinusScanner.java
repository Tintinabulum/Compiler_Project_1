package compiler.project1;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.ArrayList;

import compiler.project1.Token.TokenType;

public class CMinusScanner implements Scanner {
    private BufferedReader inFile;
    private Token nextToken;
    private HashMap<String, TokenType> reservedWords;

    public CMinusScanner (BufferedReader file) {
        inFile = file;
        reservedWords = new HashMap<String, TokenType>();
        reservedWords.put("while", TokenType.WHILE);
        reservedWords.put("void", TokenType.VOID);
        reservedWords.put("int", TokenType.INT);
        reservedWords.put("return", TokenType.RETURN);
        reservedWords.put("if", TokenType.IF);
        reservedWords.put("else", TokenType.ELSE);

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
            ArrayList<Integer> data = new ArrayList<Integer>();
            TokenType currentType = null;
            boolean done = false;

            while(next!=-1 && !done){//-1 means EOF
                switch((char)next){
                    case '+': 
                        currentType = (currentType == null ? TokenType.ADD : currentType);
                        done = true;
                        break;
                    case '-': 
                        currentType = (currentType == null ? TokenType.SUB : currentType);
                        done = true;
                        break;
                    case '*':
                        currentType = (currentType == null ? TokenType.MULT : currentType);
                        done = true;
                        break;
                    case '/':
                        inFile.mark(0);
                        next = inFile.read();

                        //Division
                        if((char)next!='*'){
                            inFile.reset();
                            currentType = (currentType == null ? TokenType.DIV : currentType);
                            done = true;
                            break;
                        }

                        //Comment
                        boolean wasStar = false;
                        next = inFile.read();
                        while(!(wasStar && (char)next == '/')){
                            wasStar = next == '*';
                            next = inFile.read();
                        }
                        //Ignore comments
                        break;

                    //Needs more cases
                    default:
                        //Handle ints
                        if(Character.isDigit((char)next) && (currentType == null || currentType == TokenType.INT))
                        {
                            data.add(next);
                            currentType = TokenType.INT;
                        }
                        else if(Character.isAlphabetic((char)next) && currentType == TokenType.INT)
                        {
                            String temp = data.stream().map(Object::toString).collect(Collectors.joining());
                            return new Token(TokenType.ERROR, "Unkown Symbol: " + temp);
                        }
                        //Handle IDs and reserved key words
                        else if(Character.isAlphabetic((char)next) && (currentType == null || currentType == TokenType.ID))
                        {
                            data.add(next);
                            currentType = TokenType.ID;
                        }
                        else if(Character.isDigit((char)next) && currentType == TokenType.ID)
                        {
                            String temp = data.stream().map(Object::toString).collect(Collectors.joining());
                            return new Token(TokenType.ERROR, "Unkown Symbol: " + temp);
                        }
                        else if((char)next == ' ')
                        {
                            if(currentType == TokenType.ID || currentType == TokenType.INT)
                            {
                                done = true;
                            }

                            break;
                        }
                        else
                            return new Token(Token.TokenType.ERROR,"Unknown symbol: " + (char)next);
                }
            }

            if(currentType == TokenType.INT)
            {
                int num = 0;
                for(int n : data)
                {
                    num *= 10 + n;
                }

                return new Token(currentType, num);
            }

            if(currentType == TokenType.ID)
            {
                String temp = data.stream().map((n) -> { return Character.toString(n); }).collect(Collectors.joining());
                currentType = (reservedWords.containsKey(temp) ? reservedWords.get(temp) : currentType);

                return new Token(currentType, temp);
            }

        }catch(IOException error){//Can happen for comments
             //Do nothing - we ignore comments
        }
        return new Token(Token.TokenType.EOF); 
    }
}