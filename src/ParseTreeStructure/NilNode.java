package ParseTreeStructure;

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
