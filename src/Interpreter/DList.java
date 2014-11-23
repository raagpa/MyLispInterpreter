package Interpreter;

import java.util.HashMap;

import ParseTreeStructure.BinaryTree;

/**
 * Singleton class to represent the global data structure DList
 * to store the user defined method definition
 * @author chiragpa
 *
 */
public class DList {

	private static DList dList;
	private static HashMap<String, BinaryTree> map = new HashMap<String,BinaryTree>() ;
	
	private DList(){
		
	}
	
	public static DList getInstance(){
		if(dList == null){
			return new DList();
		}
		return dList;
	}
	
	public HashMap<String,BinaryTree> getDListMap(){
		return map;
	}
}
