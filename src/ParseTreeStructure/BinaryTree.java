package ParseTreeStructure;

public abstract class BinaryTree {

	protected boolean isList;
	protected boolean isDotNotation;


	public boolean isDotNotation() {
		return isDotNotation;
	}

	public void setDotNotation(boolean isDotNotation) {
		this.isDotNotation = isDotNotation;
	}

	public boolean isList() {
		return isList;
	}

	public void setList(boolean isList) {
		this.isList = isList;
	}


}
