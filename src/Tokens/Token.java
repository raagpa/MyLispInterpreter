package Tokens;

public class Token {
	private String lexValue;
	public String getLexValue() {
		return lexValue;
	}

	public void setLexValue(String lexValue) {
		this.lexValue = lexValue;
	}

	public TokenCategory getCategory() {
		return category;
	}

	public void setCategory(TokenCategory category) {
		this.category = category;
	}

	private TokenCategory category;
	public Token(String value , TokenCategory tokenCategory){
		lexValue = value.toUpperCase();
		category = tokenCategory;
	}
	
	public String toString(){
		return lexValue;
	}
	
	
}
