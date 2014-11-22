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
		if (tree == null || /*tree instanceof NilNode || */
				((tree instanceof AtomNode) &&  "NIL".equals(((AtomNode) tree).getToken().getLexValue()))){
			return true;
		}else{
			return false;
		}
	}
	
	public BinaryTree getCAR() {
		// TODO Auto-generated method stub
		return this.leftSubTree;
	}
	
	public BinaryTree getCDR() {
		// TODO Auto-generated method stub
//		if(this.rightSubTree instanceof CompoundNode){
//			return ((CompoundNode) this.rightSubTree).leftSubTree;
//		}
		
		return this.rightSubTree;
	}


	public int length() {
		// TODO Auto-generated method stub
		BinaryTree tree = this;
		int length = 0;
		while(tree instanceof CompoundNode){
			length ++;
			tree = ((CompoundNode)tree).getRightSubTree();
		}
		
		return length;
	}
	
	public boolean isNILNode(){
		
		return false;
	}
	
	
	
}
