
package compiler.project1;

%%

%public
%class CMinusScanner2
%implements Scanner

%unicode

%line
%column

%scanerror CMinusScannerException

%init{
	nextToken = getNextToken();
%init}


%{
	private Token nextToken;

	public Token getNextToken()
	{
		Token returnT = nextToken;
		if(returnT.getType() != Token.TokenType.EOF) nextToken = yylex();
		return returnT;
	}
	
	public Token viewNextToken()
	{
		return nextToken;
	}
%}

/* main character classes */
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]

WhiteSpace = {LineTerminator} | [ \t\f]

/* comments */
Comment = {TraditionalComment} | {DocumentationComment}

TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
DocumentationComment = "/*" "*"+ [^/*] ~"*/"

/* identifiers */
Identifier = ([a-zA-Z])+

/* integer literals */
DecIntegerLiteral = [0-9]+

%%

<YYINITIAL> {

  /* keywords */
  "else"		{ return Token.TokenType.ELSE;   }
  "if"			{ return Token.TokenType.IF;	   } 
  "int"			{ return Token.TokenType.INT;	   }
  "return"		{ return Token.TokenType.RETURN; }
  "void"		{ return Token.TokenType.VOID;   }
  "while"		{ return Token.TokenType.WHILE;  }
    
  /* separators */
  "("                            { return Token.TokenType.BEGPAR; 	}
  ")"                            { return Token.TokenType.ENDPAR; 	}
  "{"                            { return Token.TokenType.BEGBRA; 	}
  "}"                            { return Token.TokenType.ENDBRA; 	}
  "["                            { return Token.TokenType.BEGSBRA; 	}
  "]"                            { return Token.TokenType.ENDSBRA; 	}
  ";"                            { return Token.TokenType.SEMICOLON;  }
  ","                            { return Token.TokenType.COMA;		}
  
  /* operators */
  "="                            { return Token.TokenType.EQUAL;		}
  ">"                            { return Token.TokenType.LESS;		}
  "<"                            { return Token.TokenType.GRE;		}
  "=="                           { return Token.TokenType.EQUAL;		}
  "<="                           { return Token.TokenType.LESSEQU;	}
  ">="                           { return Token.TokenType.GREEQU;		}
  "!="                           { return Token.TokenType.NOTEQUAL;	}
  "+"                            { return Token.TokenType.ADD;		}
  "-"                            { return Token.TokenType.SUB;		}
  "*"                            { return Token.TokenType.MULT;		}
  "/"                            { return Token.TokenType.DIV;		}

  /* numeric literals */

  /* This is matched together with the minus, because the number is too big to 
     be represented by a positive integer. 
  "-2147483648"                  { return symbol(INTEGER_LITERAL, Integer.valueOf(Integer.MIN_VALUE)); }*/
  
  {DecIntegerLiteral}            { return symbol(INTEGER_LITERAL, Integer.valueOf(yytext())); }
  
  /* comments */
  {Comment}                      { /* ignore */ }

  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }

  /* identifiers */ 
  {Identifier}                   { return symbol(IDENTIFIER, yytext()); }  
}

/* error fallback */
[^]                              { throw new RuntimeException("Illegal character \""+yytext()+
                                                              "\" at line "+yyline+", column "+yycolumn); }
<<EOF>>                          { return symbol(EOF); }