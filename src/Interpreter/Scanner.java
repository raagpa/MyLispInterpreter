package Interpreter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


import Exception.ScannerException;
import Tokens.Token;
import Tokens.TokenCategory;


public class Scanner {

	private List<Token> tokenList;


	public void scan(String file) throws ScannerException{
		StringBuilder input = new StringBuilder();
		tokenList = new ArrayList<Token>();
		try {
			
			input = readInput(file);
			

			int i =0;

			while (i<input.length()) {

				char c  = input.charAt(i);
				if(isWhiteSpace(c)){
					//ignore
					i++;
				}else if(isOpenParenthesis(c)){
					tokenList.add(new Token("(",TokenCategory.OPEN_PARENTHESIS));
					i++;

				}else if(isCloseParenthesis(c)){
					tokenList.add(new Token(")",TokenCategory.CLOSE_PARENTHESIS));
					i++;
				}else if(isDot(c)){
					if(isValidDotToken(i,input)){
						tokenList.add(new Token(".",TokenCategory.DOT));
					}else{
						throw new ScannerException("The only characters that are allowed "
								+ " to be immediately before or immediately after a Dot token are a "
								+ "whitespace, ( or )");
					} 
					i++;
				}else{
					StringBuilder atom = new StringBuilder();
					//char temp = input.charAt(i);
					while(i< input.length() && (!isCloseParenthesis(input.charAt(i)) && !isOpenParenthesis(input.charAt(i)) 
							&& !isDot(input.charAt(i)) && !isWhiteSpace(input.charAt(i))) ){
						atom.append(input.charAt(i));
						i++;
					}
					if(isEOF(atom)){
						tokenList.add(new Token(atom.toString(),TokenCategory.EOF));
					}else if(isNumericAtom(atom)){
						tokenList.add(new Token(atom.toString(),TokenCategory.NUMERIC_ATOM));
					}else if(isValidLiteralAtom(atom)){
						tokenList.add(new Token(atom.toString(),TokenCategory.LITERAL_ATOM));
					}else{
						throw new ScannerException("Invalid characters in atom : "+ atom);
					}

				}

			}

			tokenList.add(new Token("EOF",TokenCategory.EOF));

		}catch(ScannerException se){
			//throw se;
			tokenList.add(new Token(se.getMessage(),TokenCategory.ERROR));
		}

	}

	private boolean isEOF(StringBuilder atom) {
		if("EOF".equalsIgnoreCase(atom.toString())){
			return true;
		}
		return false;
	}

	/*
	private StringBuilder readFromStdin(StringBuilder input){
		InputStream stream = System.in;
		byte bytes[] = null;
		try{
			while (stream.available() > 0){
				stream.read(bytes, 0, 1024);
				input = input.append(new String(bytes));

			}
		} catch(IOException i){
			i.printStackTrace();
		}
		return input;
	}*/

	private StringBuilder readInput(String file)
	{
		BufferedReader reader = null;
		StringBuilder input = null;
		try{
			if(null == file){
				reader = new BufferedReader(new InputStreamReader(System.in));
			}else{
				reader = new BufferedReader(new FileReader(file));
			}

			String line = "";
			input = new StringBuilder(); 
			while((line = reader.readLine()) != null){
				input.append(" "+line);
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return input;
	}

	private boolean isValidLiteralAtom(StringBuilder atom) {
		if(isAlphaNumeric(atom.toString())){
			return true;
		}
		return false;
	}

	private boolean isAlphaNumeric(String atom) {
		int i=0;
		int length = atom.length();

		while(i< length){
			char c = atom.charAt(i);
			if(!((c >= 48 && c<= 57) || (c >=65 && c <=90)
					|| (c >= 97  && c<= 122))){
				return false;
			}
			i++;
		}
		return true;
	}

	private boolean isNumericAtom(StringBuilder atom) {
		try{
			Integer.parseInt(atom.toString());
			return true;
		}catch(NumberFormatException ne){
			return false;
		}

	}

	private boolean isWhiteSpace(char c) {
		if(c == ' ' || c == '\n' || c == '\t'){
			return true;
		}
		return false;
	}

	private boolean isValidDotToken(int i, StringBuilder input) {
		if(i-1 >=0 && i+1 < input.length()){
			char previousToken = input.charAt(i-1);
			char nextToken = input.charAt(i+1);
			if((isOpenParenthesis(previousToken) || isCloseParenthesis(previousToken) || isWhiteSpace(previousToken))
					&& (isOpenParenthesis(nextToken) || isCloseParenthesis(nextToken) || isWhiteSpace(nextToken))){
				return true;
			}
		}
		return false;
	}

	private boolean isCloseParenthesis(char c) {
		if(c == ')'){
			return true;
		}
		return false;
	}

	private boolean isDot(char c) {
		if(c == '.'){
			return true;
		}
		return false;
	}

	private boolean isOpenParenthesis(char c) {
		if(c == '('){
			return true;
		}
		return false;
	}

	public boolean hasToken() {

		if(tokenList.isEmpty()){
			return false;
		}
		return true;
	}

	public Token getNextToken() {
		if(hasToken()){
			Token token = tokenList.get(0);
			tokenList.remove(0);
			return token;
		}
		return null;

	}

	public void putBackToken(Token token) {
		tokenList.add(0, token);

	}

	public Token peek(){
		if(hasToken()){
			Token token = tokenList.get(0);
			return token;
		}
		return null;
	}

}
