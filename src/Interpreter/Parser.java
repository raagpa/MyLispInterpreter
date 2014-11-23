package Interpreter;


import Exception.ParserException;
import ParseTreeStructure.AtomNode;
import ParseTreeStructure.BinaryTree;
import ParseTreeStructure.CompoundNode;
import ParseTreeStructure.NilNode;
import Tokens.Token;
import Tokens.TokenCategory;

/**
 * This class follows the grammar provided in the project
 * description.
 * <S> ::= <E> <E> ::= atom <E> ::= ( <X>
 * <X> ::= <E> <Y> <X> ::= ) <Y> ::= . <E> )
 * <Y> ::= <R> ) <R> ::= null <R> ::= <E> <R>
 * Methods have been developed by this grammar.
 * 
 * The parser processes the stream of tokens 
 * produced by the scanner, and builds the tree 
 * representation of the corresponding S-expression.
 * 
 * @author chiragpa
 *
 */
public class Parser {
	
	private BinaryTree parseTree;

	private Scanner scanner;
	
	
	public Parser(Scanner scanner){
		this.scanner = scanner;
	}
	
	
	
	/**
	 *
	 * @return BinaryTree
	 * @throws ParserException
	 */
	public BinaryTree parseS() throws ParserException{
		parseTree = parseE(null);
		return parseTree;
		
	}
	
	/**
	 *
	 * @param Token
	 * @return BinaryTree
	 * @throws ParserException
	 */
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

	

	/**
	 *
	 * @param Token
	 * @return BinaryTree
	 * @throws ParserException
	 */
	private BinaryTree parseX(Token token) throws ParserException {
		//System.out.print("parseX");
		if(null == token){
			token = scanner.getNextToken();
		}
		if(TokenCategory.CLOSE_PARENTHESIS.equals(token.getCategory()) ){
			parseCloseParenthesis(token);
			//return null;
			return new AtomNode(new Token("NIL",TokenCategory.LITERAL_ATOM));
			
		}else{
			BinaryTree leftSubTree = parseE(token);
			BinaryTree rightSubTree = parseY(null);
			
			return new CompoundNode(leftSubTree,rightSubTree);
			
		}
		
	}

	
	/**
	 *
	 * @param Token
	 * @return BinaryTree
	 * @throws ParserException
	 */
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


	
	/**
	 *
	 * @param Token
	 * @return BinaryTree
	 * @throws ParserException
	 */
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
			//return new NilNode();
			//return null;
			return new AtomNode(new Token("NIL",TokenCategory.LITERAL_ATOM));
		}
		

	}
	
	/**
	 * @return void
	 * @param Token
	 * @throws ParserException
	 */
	private void parseOpenParenthesis(Token token) throws ParserException {
		//System.out.print("parseOpenParenthesis");
		if(null == token){
			token = scanner.getNextToken();
			if(!TokenCategory.OPEN_PARENTHESIS.equals(token.getCategory())){
				isScannerError(token);
				throw new ParserException("Parsing failed - Expecting an Open Parenthesis, found "+ token.getLexValue());
			}
		}
		//System.out.print(token.getLexValue());
		
	}

	/**
	 *
	 * @param Token
	 * @return BinaryTree
	 * @throws ParserException
	 */
	private BinaryTree parseAtom(Token token) throws ParserException {
		//System.out.print("parseAtom");
		if(null == token){
			token = scanner.getNextToken();
			if(!TokenCategory.NUMERIC_ATOM.equals(token.getCategory()) && !TokenCategory.LITERAL_ATOM.equals(token.getCategory()) ){
				isScannerError(token);
				throw new ParserException("Parsing failed - Expecting an Atom, found "+ token.getLexValue());
			}
		}
		//System.out.print(token.getLexValue());
//		if("NIL".equalsIgnoreCase(token.getLexValue())){
//			return new NilNode();
//		}else{
			return new AtomNode(token);
//		}
		
	}
	
	/**
	 * @return BinaryTree
	 * @param Token
	 * @throws ParserException
	 */
	private void parseCloseParenthesis(Token token) throws ParserException {
		//System.out.print("parseCloseParenthesis");
		if(null == token){
			token = scanner.getNextToken();
			if(!TokenCategory.CLOSE_PARENTHESIS.equals(token.getCategory())){
				isScannerError(token);
				throw new ParserException("Parsing failed - Expecting a Close Parenthesis, found "+ token.getLexValue());
			}
		}
		//System.out.print(token.getLexValue());
		
	}
	

	/**
	 * @return void
	 * @param Token
	 * @throws ParserException
	 */
	private void parseDot(Token token) throws ParserException {
		//System.out.print("parseDot");
		if(null == token){
			token = scanner.getNextToken();
			if(!TokenCategory.DOT.equals(token.getCategory())){
				isScannerError(token);
				throw new ParserException("Parsing failed - Expecting a Dot, found "+ token.getLexValue());
			}
		}
		//System.out.print(token.getLexValue());
		
	}
	
	
	/**
	 * @return void
	 * @param Token
	 * @throws ParserException
	 */
	private void isScannerError(Token token) throws ParserException {
		if(TokenCategory.ERROR.equals(token.getCategory())){
			throw new ParserException(""+ token.getLexValue());
		}
	}

	
	
	
	
}
