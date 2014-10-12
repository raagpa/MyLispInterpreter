package ParseTreeStructure;

import Tokens.Token;

public class AtomNode extends BinaryTree{
	
	private Token token;
	
	public AtomNode(Token token){
		this.token = token;
		this.isList = false;
		this.isDotNotation = false;
	}
	
	public Token getToken(){
		return token;
	}
	
	public String toString(){
		return token.getLexValue();
	}

}
