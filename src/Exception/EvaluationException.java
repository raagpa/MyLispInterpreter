package Exception;

public class EvaluationException extends Exception {
	public EvaluationException(String error){
		super("ERROR: "+error);
	}
}
