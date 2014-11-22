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
		if("NIL".equals(getToken().getLexValue())){
			return true;
		}
		return false;
	}

	public int length(){
		return 1;
	}
	
	public boolean isNILNode(){
		if(((this instanceof AtomNode) &&  "NIL".equals(((AtomNode) this).getToken().getLexValue()))){
			return true;
		}
		return false;
	}
}
