import java.util.*;
import java.io.*;

public class Parse {

	 // Global declarations of variables
	static final int MAX_LEXEME_LEN = 100;
	static Token charClass;                            // Compare to enum to identify the character's class
	static int lexLen;                                 // Current lexeme's length
	static char lexeme[] = new char[MAX_LEXEME_LEN];   // Current lexeme's character array
	static char nextChar;
	static Token nextToken;
	static int charIndex;
	static char ch = '0';
	static String newString;
	static FileWriter myOutput;
	static Scanner scnr; // Scanner to print all the input before parsing
	static Scanner scan; // Scanner that will be used for parsing
	
	
	 //Tokens and categories for single characters
	static enum Token {
		INT_LIT,
		IDENT,
		ASSIGN_OP,
		ADD_OP,
		SUB_OP,
		MULT_OP,
		DIV_OP,
		LEFT_PAREN,
		RIGHT_PAREN,
		PRINT_KEYWORD,
		PROGRAM_KEYWORD,
		END_KEYWORD,
		LETTER,
		DIGIT,
		SEMICOLON,
		UNKNOWN,
		END_OF_FILE,
		UNDERSCORE
	}

 
	 /*********************** Main driver ***********************/
	public static void main(String[] args) throws IOException {

		 // Variables 
		String line;
		
	    myOutput = new FileWriter("parseOut.txt");

		System.out.println("Roy Turner, CSCI4200, Spring 2022, Parser"); 
		myOutput.write("Roy Turner Student, CSCI4200, Spring 2022, Parser\n");
		System.out.println("********************************************************************************");
		myOutput.write("********************************************************************************\n");

		// Uses two scanners, one to print the original input, and one to use for parsing
		try {
			scan = new Scanner(new File("sourceProgram.txt"));
			scnr = new Scanner(new File("sourceProgram.txt"));
			
			
			while (scnr.hasNextLine()) {
				System.out.println(scnr.nextLine());
				myOutput.write(scnr.nextLine());
			}
			scnr.close();
			System.out.println("********************");
			
			

			// For each line, grab each character 
			while (scan.hasNextLine()) {
				line = scan.nextLine();
				charIndex = 0;
				
				if (getChar(line)) {
					// Make sure first Token is program before we start parsing
					if (lex(line) == Token.PROGRAM_KEYWORD) {
						System.out.println("Next token is PROGRAM_KEYWORD");
						program(newString);
					}

				}
				
		}
			scan.close();
		}
		catch (FileNotFoundException e) {
			System.out.println(e.toString());
		
		}
		myOutput.close();	
	}

	
	// Method to look up single character tokens
	private static Token lookup(char ch) {

		switch (ch) {
		
		case '(':
			addChar();
			nextToken = Token.LEFT_PAREN;
			break;

		case ')':
			addChar();
			nextToken = Token.RIGHT_PAREN;
			break;

		case '+':
			addChar();
			nextToken = Token.ADD_OP;
			break;

		case '-':
			addChar();
			nextToken = Token.SUB_OP;
			break;

		case '*':
			addChar();
			nextToken = Token.MULT_OP;
			break;

		case '/':
			addChar();
			nextToken = Token.DIV_OP;
			break;

		case '=':
			addChar();
			nextToken = Token.ASSIGN_OP;
			break;
		
		case ';':
			addChar();
			nextToken = Token.SEMICOLON;
			break;
		}

		return nextToken;
	}

	
	/************ addChar - inserts a char to lexeme *************/
	private static boolean addChar() {
		
		if (lexLen <= 98) {
			lexeme[lexLen++] = nextChar;
			lexeme[lexLen] = 0;
			return true;
		}
		
		else {
			System.out.println("Error - lexeme is too long \n");
			return false;
		}
	}

	
	/************* getChar - a function to get the next character in the line *************/
	private static boolean getChar(String ln) {

		if (charIndex >= ln.length()) {
			return false;
		}

		nextChar = ln.charAt(charIndex++);

		if (Character.isDigit(nextChar)) {
			charClass = Token.DIGIT;
		} 
		
		else if (Character.isAlphabetic(nextChar)) {
			charClass = Token.LETTER;
		} 
		
		else if (nextChar == '_') {
			charClass = Token.UNDERSCORE;
		}
		
		else {
			charClass = Token.UNKNOWN;
		}

		return true;
	}

	/************* getNonBlank - a method to skip whitespace *************/
	public static boolean getNonBlank(String ln) {
		while (Character.isSpaceChar(nextChar) || nextChar == '	') {
			if (!getChar(ln)){
				return false;
			}
		}
		return true;
	}
	

	 /* @throws IOException ***************************************************/
	/************* lex - a simple lexical analyzer for arithmetic expressions *************/
	public static Token lex(String ln) throws IOException {
		
		lexLen = 0;
		getNonBlank(ln);
		
		switch (charClass) {
		// Parse identifiers
		case LETTER:
			nextToken = Token.IDENT;
			addChar();
			
			if (getChar(ln)) {
				while (charClass == Token.LETTER || charClass == Token.UNDERSCORE) {
					addChar();
					
					if (!getChar(ln)) {
						break;
					}
			}
				
			// Scans the identifiers for valid keywords
			switch(String.valueOf(lexeme, 0, lexLen).toUpperCase()) {
				  			case "PROGRAM" :
					  		nextToken = Token.PROGRAM_KEYWORD;
				                break;
				            case "END":
				            	nextToken = Token.END_KEYWORD;
				                break;
				            case "PRINT":
				                nextToken = Token.PRINT_KEYWORD;
				                break;
				        }
				
				if (charClass == Token.UNKNOWN && charIndex == ln.length()) {
					charIndex--;
				}
			}
			break;
			
		// Parse integer literals 
		case DIGIT:
			nextToken = Token.INT_LIT;
			addChar();
			
			if (getChar(ln)) {
				while (charClass == Token.DIGIT) {
					addChar();
					
					if(!getChar(ln)) {
						break;
					}
				}
				
				if (charClass == Token.UNKNOWN && charIndex == ln.length()) {
					charIndex--;
				}
			}
			break;
			
		 // Parentheses and operators 
		case UNKNOWN:
			lookup(nextChar);
			getChar(ln);
			break;
			
		default:
			nextToken = Token.UNKNOWN;
			break;
		} // End of switch 
		
		 // Print each token 
		//System.out.printf("Next token is: %-12s", nextToken.toString());
		

		return nextToken;
		
	
	}
	
	/* Each nonterminal has a method*/
	public static void program(String p) throws IOException {
		System.out.println("Enter <program>\n");
		myOutput.write("Enter <program>\n");
		
		 while(scan.hasNextLine()){
			  String newString = scan.nextLine().trim();
			  charIndex = 0;
		
			  if(getChar(newString) && nextToken != Token.END_KEYWORD) {
				    while(charIndex < newString.length()) {
				      lex(newString);
				      // A statement can either be assign or output. Since assign starts with ident, and output starts with the print keyword, we check for those. 
				      if(nextToken == Token.IDENT || nextToken == Token.PRINT_KEYWORD) {
				         statement(newString);
				      }
				      else {
				         error();
				      }
				   }
				}
		}
	
		while (nextToken == Token.SEMICOLON) {
			lex(newString);
			statement(newString);
		}
		
		System.out.println("Exit <program>\n");
		myOutput.write("Exit <program>\n");
		
		System.out.println("Next Token is END");
		myOutput.write("Next Token is END\n");
		
		System.out.println("Parsing of the program is complete!");
		myOutput.write("Parsing of the program is complete!\n");
		
	}
	
	//<statement> → <output> | <assign>
	public static void statement(String s) throws IOException { 
		System.out.println("Enter <statement>\n");
		myOutput.write("Enter <statement>\n");
		if (nextToken == Token.IDENT) {
			assign(s);
		}
		else if (nextToken == Token.PRINT_KEYWORD) {
			System.out.println("Next Token is PRINT_KEYWORD");
			myOutput.write("Next Token is PRINT_KEYWORD\n");
			output(s);
		}
		else {
			error();

		
		}
		System.out.println("Exit <statement>\n");
		myOutput.write("Exit <statement>\n");
			}
	
	//<output> → print (<expr>)
	public static void output(String o) throws IOException {
		System.out.println("Enter <output>\n");
		myOutput.write("Enter <output>\n");
			lex(o);
		if (nextToken == Token.LEFT_PAREN) {
			lex(o);
			expr(o);
		}
		else {
			error();
		}
		System.out.println("Exit <output>\n");
		myOutput.write("Exit <output>\n");
	}
	
	//<assign> → IDENT = <expr> 
	public static void assign(String a) throws IOException {
		System.out.println("Enter <assign>\n");
		myOutput.write("Enter <assign>\n");
		System.out.println("Next Token is IDENT");
		myOutput.write("Next Token is IDENT\n");
			lex(a);
			
		if (nextToken == Token.ASSIGN_OP) {
			lex(a);
		}
		else {
			error();
		}
		if (nextToken == Token.LEFT_PAREN) {
			expr(a);
		}
		else {
			error();
		}
			
		System.out.println("Exit <assign>\n");
		myOutput.write("Exit <assign>\n");
	}
	
	// <expr> → <term> {(+ | -) <term>}
	public static void expr(String e) throws IOException {
		System.out.println("Enter <expr>\n");
		myOutput.write("Enter <expr>\n");
	/* Parse the first term */
				term(e);
	/*  If next token is {+|-} we parse next term*/
			while (nextToken == Token.ADD_OP || nextToken == Token.SUB_OP) {
				if (nextToken == Token.ADD_OP) {
					System.out.println("Next Token is ADD_OP");
					myOutput.write("Next Token is ADD_OP\n");
					lex(e);
					term(e);
				}
				if (nextToken == Token.SUB_OP) {
					System.out.println("Next Token is SUB_OP");
					myOutput.write("Next Token is SUB_OP\n");
					lex(e);
					term(e);
				}
				
				}
			System.out.println("Exit <expr>\n");
			myOutput.write("Exit <expr>\n");
	} 
		/* End of function expr */
	
	public static void term(String t) throws IOException {
		System.out.println("Enter <term>\n");
		myOutput.write("Enter <term>\n");
	/* Parse the first factor */
		factor(t);
	/* As long as the next token is * or /, get the
	next token and parse the next factor */
		while (nextToken == Token.MULT_OP || nextToken == Token.DIV_OP) {
			if (nextToken == Token.MULT_OP) {
				System.out.println("Next Token is MULT_OP");
				myOutput.write("Next Token is MULT_OP\n");
				lex(t);
				factor(t);
			}
			if (nextToken == Token.DIV_OP) {
				System.out.println("Next Token is DIV_OP");
				myOutput.write("Next Token is DIV_OP\n");
				lex(t);
				factor(t);
			}
			
	}
		System.out.println("Exit <term>\n");
		myOutput.write("Exit <term>\n");
	} /* End of function term */
	
	
	/*<factor> -> id | int_constant | ( <expr )*/
	public static void factor(String f) throws IOException {
		System.out.println("Enter <factor>");
		myOutput.write("Enter <factor>\n");
	/* Determine which RHS */
		if (nextToken == Token.IDENT  || nextToken == Token.INT_LIT) {
			 if (nextToken == Token.IDENT) {
				 System.out.println("Next Token is IDENT");
				 myOutput.write("Next Token is Ident\n");/* Get the next token */
				 lex(f);
			 }
			 if (nextToken == Token.INT_LIT) {
				 System.out.println("Next Token is INT_LIT");
				 myOutput.write("Next Token is INT_LIT\n");
			 }
		}
		
		else if (nextToken == Token.LEFT_PAREN) {
			System.out.println("Next Token is LEFT_PARENT");
			myOutput.write("Next Token is LEFT_PARENT\n");
				lex(f);
				expr(f);
		if (nextToken == Token.RIGHT_PAREN) {
			System.out.println("Next Token is RIGHT_PAREN");
			 myOutput.write("Next Token is RIGHT_PAREN\n");
			lex(f);
		}
		else {
			error();
	} /* End of if (nextToken == ... */
	/* It was not an id, an integer literal, or a left
	parenthesis */
	//**********else error();
	} /* End of else */
		System.out.println("Exit <factor>\n");
		myOutput.write("Enter <factor>\n");
	
	} 
	
		public static void error() {
			System.out.println("Error not correct Syntax");
	}
	
	}


	

	