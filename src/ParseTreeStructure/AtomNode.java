package ParseTreeStructure;

import Tokens.Token;

public class AtomNode extends BinaryTree{
	
	private Token token;
	
	public AtomNode(Token token){
		this.token = token;
		
	}
	
	public Token getToken(){
		return token;
	}
	
	public String toString(){
		return token.getLexValue();
	}
	
	public boolean isList(){
		return false;
	}

}
