package Exception;

public class ParserException extends Exception {
	public ParserException(String error){
		super("ERROR: "+error);
	}
}
