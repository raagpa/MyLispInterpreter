package ParseTreeStructure;

public class CompoundNode extends BinaryTree {
	
	private BinaryTree leftSubTree;
	private BinaryTree rightSubTree;
	
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



	public CompoundNode(BinaryTree left, BinaryTree right){
		this.leftSubTree = left;
		this.rightSubTree = right;
	
	}
	
	public boolean isList(){
		BinaryTree tree = this;
		while(tree instanceof CompoundNode){
			tree = ((CompoundNode)tree).getRightSubTree();
		}
		if (tree instanceof NilNode){
			return true;
		}else{
			return false;
		}
	}
	
	
}
