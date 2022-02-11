package compiler.project1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.stream.Collectors;

import java.util.ArrayList;

import compiler.project1.Token.TokenType;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        //Scan the next token if the current is not EOF
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

            while(next != -1 && !done){//-1 means EOF
                switch((char)next){
                    case '(':
                        if(currentType == null)
                        {
                            currentType = TokenType.BEGPAR;
                        }
                        else
                        {
                            inFile.reset();
                        }

                        done = true;
                        break;

                    case ')':
                        if(currentType == null)
                        {
                            currentType = TokenType.ENDPAR;
                        }
                        else
                        {
                            inFile.reset();
                        }

                        done = true;
                        break;

                    case '[':
                        if(currentType == null)
                        {
                            currentType = TokenType.BEGSBRA;
                        }
                        else
                        {
                            inFile.reset();
                        }

                        done = true;
                        break;

                    case ']':
                        if(currentType == null)
                        {
                            currentType = TokenType.ENDSBRA;
                        }
                        else
                        {
                            inFile.reset();
                        }

                        done = true;
                        break;

                    case '{':
                        if(currentType == null)
                        {
                            currentType = TokenType.BEGBRA;
                        }
                        else
                        {
                            inFile.reset();
                        }

                        done = true;
                        break;

                    case '}':
                        if(currentType == null)
                        {
                            currentType = TokenType.ENDBRA;
                        }
                        else
                        {
                            inFile.reset();
                        }

                        done = true;
                        break;

                    case ',':
                        if(currentType == null)
                        {
                            currentType = TokenType.COMA;
                        }
                        else
                        {
                            inFile.reset();
                        }

                        done = true;
                        break;

                    case '+': 
                        if(currentType == null)
                        {
                            currentType = TokenType.ADD;
                        }
                        else
                        {
                            inFile.reset();
                        }

                        done = true;
                        break;

                    case '-': 
                        if(currentType == null)
                        {
                            currentType = TokenType.SUB;
                        }
                        else
                        {
                            inFile.reset();
                        }
                        done = true;
                        break;
                    case ';': 
                        if(currentType == null)
                        {
                            currentType = TokenType.SEMICOLON;
                        }
                        else
                        {
                            inFile.reset();
                        }
                        done = true;
                        break;

                    case '*':
                        if(currentType == null)
                        {
                            currentType = TokenType.MULT;
                            done = true;
                        }
                        else if(currentType == TokenType.DIV) //Throw away comment
                        {
                            next = inFile.read();
                            inFile.mark(0);
                            while(next != -1 && next != '*' && inFile.read() != '/')
                            {
                                inFile.reset();
                                next = inFile.read();
                                inFile.mark(0);
                            }
                            //reset state
                            currentType = null;
                            inFile.read(); //Skip past last '/' of comment
                            next = inFile.read();
                        }
                        else
                        {
                            inFile.reset();
                            done = true;
                        }
                        break;

                    case '<':
                        if(currentType == null)
                        {
                            currentType = TokenType.LESS;
                            next = inFile.read();
                        }
                        else
                        {
                            inFile.reset();
                            done = true;
                        }
                        break;

                    case '>':
                        if(currentType == null)
                        {
                            currentType = TokenType.GRE;
                            next = inFile.read();
                        }
                        else
                        {
                            inFile.reset();
                            done = true;
                        }

                    break;

                    case '!':
                        if(currentType == null)
                        {
                            inFile.mark(0);
                            if(inFile.read() == '=')
                            {
                                currentType = TokenType.NOTEQUAL;
                            }
                            else
                            {
                                inFile.reset();
                                return new Token(Token.TokenType.ERROR,"Unknown symbol: " + (char)next);
                            }
                        }
                        else
                        {
                            inFile.reset();
                            done = true;
                        }

                    case '=':
                        if(currentType == null)
                        {
                            currentType = TokenType.ASSIGN;
                            next = inFile.read();
                        }
                        else if(currentType == TokenType.LESS)
                        {
                            currentType = TokenType.LESSEQU;
                            done = true;
                        }
                        else if(currentType == TokenType.GRE)
                        {
                            currentType = TokenType.GREEQU;
                            done = true;
                        }
                        else if(currentType == TokenType.ASSIGN)
                        {
                            currentType = TokenType.EQUAL;
                            done = true;
                        }
                        else
                        {
                            inFile.reset();
                            done = true;
                        }
                        break;

                    case '/':
                        if(currentType == null)
                        {
                            currentType = TokenType.DIV;
                            inFile.mark(0);
                            next = inFile.read();
                        }
                        else
                        {
                            inFile.reset();
                            done = true;
                        }
                        break;

                    //Needs more cases
                    default:
                        //Handle ints
                        if(Character.isDigit((char)next) && (currentType == null || currentType == TokenType.INT))
                        {
                            data.add(next - '0');
                            inFile.mark(0);
                            next = inFile.read();
                            currentType = TokenType.INT;
                        }
                        else if(Character.isAlphabetic((char)next) && currentType == TokenType.INT)
                        {
                            String temp = data.stream().map((n) -> { return Integer.toString(n); } ).collect(Collectors.joining());
                            return new Token(TokenType.ERROR, "Unkown Symbol: " + temp);
                        }
                        //Handle IDs and reserved key words
                        else if(Character.isAlphabetic((char)next) && (currentType == null || currentType == TokenType.ID))
                        {
                            data.add(next);
                            inFile.mark(0);
                            next = inFile.read();
                            currentType = TokenType.ID;
                        }
                        else if(Character.isDigit((char)next) && currentType == TokenType.ID)
                        {
                            String temp = data.stream().map((n) -> { return Character.toString(n); }).collect(Collectors.joining());
                            return new Token(TokenType.ERROR, "Unkown Symbol: " + temp);
                        }
                        else if(currentType != null)
                        {
                            done = true;
                        }
                        else if(next == ' ' || next == '\n' || next == '\r')
                        {
                            next = inFile.read();
                        }
                        else
                        {
                            return new Token(Token.TokenType.ERROR,"Unknown symbol: " + (char)next);
                        }
                }
            }

            if(currentType == TokenType.INT)
            {
                int num = 0;
                for(int n : data)
                {
                    num = num * 10 + n;
                }

                return new Token(currentType, num);
            }

            if(currentType == TokenType.ID)
            {
                String temp = data.stream().map((n) -> { return Character.toString(n); }).collect(Collectors.joining());
                currentType = (reservedWords.containsKey(temp) ? reservedWords.get(temp) : currentType);

                return new Token(currentType, temp);
            }

            //Loop never ran, so we must be EOF
            if(currentType == null)
            {
                currentType = TokenType.EOF;
            }

            return new Token(currentType);

        }catch(IOException error){//Can happen for comments
             //Do nothing - we ignore comments
        }
        return new Token(Token.TokenType.EOF); 
    }
    public static void main (String[] args) {
        try{
            FileReader inFile = new FileReader("Project1/input.txt");
            FileWriter outFile = new FileWriter("Project1/output.txt");
            
            BufferedReader brFile = new BufferedReader(inFile);
            CMinusScanner cMinScan = new CMinusScanner(brFile);
            
            
            //Scan through all the tokens until it reaches EOF
            while (cMinScan.viewNextToken().getType() != Token.TokenType.EOF){

                Token t = cMinScan.getNextToken();
                String output = t.getType().name();
                if(t.getData() != null)
                {
                    output += ": " + t.getData();
                }

                outFile.write(output + "\n");
            }
            
            inFile.close();
            outFile.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CMinusScanner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CMinusScanner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}