package Interpreter;


import Exception.ParserException;
import ParseTreeStructure.AtomNode;
import ParseTreeStructure.BinaryTree;
import ParseTreeStructure.CompoundNode;
import ParseTreeStructure.NilNode;
import Tokens.Token;
import Tokens.TokenCategory;

public class Parser {
	
	private BinaryTree parseTree;
	
	
	public Parser(Scanner scanner){
		this.scanner = scanner;
	}
	
	private Scanner scanner;
	
	
	public BinaryTree parseS() throws ParserException{
		parseTree = parseE(null);
		return parseTree;
		
	}
	
	private BinaryTree parseE(Token token) throws ParserException{
		//System.out.print("parseE");
		if(null == token){
			token = scanner.getNextToken();
		}
		
		if(TokenCategory.NUMERIC_ATOM.equals(token.getCategory()) ||
				TokenCategory.LITERAL_ATOM.equals(token.getCategory()) ){
			return parseAtom(token);
			
			
		}else if(TokenCategory.OPEN_PARENTHESIS.equals(token.getCategory())){
			parseOpenParenthesis(token);
			return parseX(null);
			
		}else{
			isScannerError(token);
			throw new ParserException("Parsing failed - Expecting an Atom or Open Parenthesis, found "+token.getLexValue());
		}
	}

	

	private BinaryTree parseX(Token token) throws ParserException {
		//System.out.print("parseX");
		if(null == token){
			token = scanner.getNextToken();
		}
		if(TokenCategory.CLOSE_PARENTHESIS.equals(token.getCategory()) ){
			parseCloseParenthesis(token);
			return null;
			
		}else{
			BinaryTree leftSubTree = parseE(token);
			BinaryTree rightSubTree = parseY(null);
			
			return new CompoundNode(leftSubTree,rightSubTree);
			
		}
		
	}

	
	private BinaryTree parseY(Token token) throws ParserException {
		//System.out.print("parseY");
		if(null == token){
			token = scanner.getNextToken();
		}
		
		if(TokenCategory.DOT.equals(token.getCategory()) ){
			parseDot(token);
			BinaryTree tree = parseE(null);
			parseCloseParenthesis(null);
			return tree;
			
		}else{
			BinaryTree tree = parseR(token);
			parseCloseParenthesis(null);
			return tree;
		}
		
	}


	
	private BinaryTree parseR(Token token) throws ParserException {
		//System.out.print("parseR");
		if(null == token){
			token = scanner.getNextToken();
		}
		
		if(TokenCategory.NUMERIC_ATOM.equals(token.getCategory()) ||
				TokenCategory.LITERAL_ATOM.equals(token.getCategory()) || TokenCategory.OPEN_PARENTHESIS.equals(token.getCategory())){
			BinaryTree leftSubTree = parseE(token);
			BinaryTree rightSubTree = parseR(null);
			//boolean isList =  rightSubTree instanceof NilNode;
			return new CompoundNode(leftSubTree, rightSubTree);
		}else{
			scanner.putBackToken(token);
			return new NilNode();
		}
		

	}
	
	private void parseOpenParenthesis(Token token) throws ParserException {
		//System.out.print("parseOpenParenthesis");
		if(null == token){
			token = scanner.getNextToken();
			if(!TokenCategory.OPEN_PARENTHESIS.equals(token.getCategory())){
				isScannerError(token);
				throw new ParserException("Parsing failed - Expecting an Open Parenthesis, found "+ token.getLexValue());
			}
		}
		System.out.print(token.getLexValue());
		
	}

	private BinaryTree parseAtom(Token token) throws ParserException {
		//System.out.print("parseAtom");
		if(null == token){
			token = scanner.getNextToken();
			if(!TokenCategory.NUMERIC_ATOM.equals(token.getCategory()) && !TokenCategory.LITERAL_ATOM.equals(token.getCategory()) ){
				isScannerError(token);
				throw new ParserException("Parsing failed - Expecting an Atom, found "+ token.getLexValue());
			}
		}
		System.out.print(token.getLexValue());
		if("NIL".equalsIgnoreCase(token.getLexValue())){
			return new NilNode();
		}else{
			return new AtomNode(token);
		}
		
	}
	
	private void parseCloseParenthesis(Token token) throws ParserException {
		//System.out.print("parseCloseParenthesis");
		if(null == token){
			token = scanner.getNextToken();
			if(!TokenCategory.CLOSE_PARENTHESIS.equals(token.getCategory())){
				isScannerError(token);
				throw new ParserException("Parsing failed - Expecting a Close Parenthesis, found "+ token.getLexValue());
			}
		}
		System.out.print(token.getLexValue());
		
	}
	

	private void parseDot(Token token) throws ParserException {
		//System.out.print("parseDot");
		if(null == token){
			token = scanner.getNextToken();
			if(!TokenCategory.DOT.equals(token.getCategory())){
				isScannerError(token);
				throw new ParserException("Parsing failed - Expecting a Dot, found "+ token.getLexValue());
			}
		}
		System.out.print(token.getLexValue());
		
	}
	
	
	private void isScannerError(Token token) throws ParserException {
		if(TokenCategory.ERROR.equals(token.getCategory())){
			throw new ParserException(""+ token.getLexValue());
		}
	}

	
	
	
	
}
