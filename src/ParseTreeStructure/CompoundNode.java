package ParseTreeStructure;

public class CompoundNode extends BinaryTree {
	
	private BinaryTree leftSubTree;
	public BinaryTree getLeftSubTree() {
		return leftSubTree;
	}


	public void setLeftSubTree(BinaryTree leftSubTree) {
		this.leftSubTree = leftSubTree;
	}


	public BinaryTree getRightSubTree() {
		return rightSubTree;
	}


	public void setRightSubTree(BinaryTree rightSubTree) {
		this.rightSubTree = rightSubTree;
	}


	private BinaryTree rightSubTree;
	

	public CompoundNode(BinaryTree left, BinaryTree right, boolean isList, boolean isDotPresent){
		this.leftSubTree = left;
		this.rightSubTree = right;
		this.isList = isList;
		this.isDotNotation = isDotPresent;
	}
	
}
