package ParseTreeStructure;
/**
 * Class for Nil Node in S-Expression
 * @author chiragpa
 *
 */
public class NilNode extends BinaryTree{

	
	public boolean isList(){
		return false;
	}
	
	public int length(){
		return 1;
	}
	public boolean isNILNode(){
		return true;
	}
}
