package Interpreter;


import java.util.HashMap;

import Exception.EvaluationException;
import Exception.ParserException;
import Exception.ScannerException;
import Interpreter.Scanner;
import ParseTreeStructure.AtomNode;
import ParseTreeStructure.BinaryTree;
import ParseTreeStructure.CompoundNode;
import Tokens.TokenCategory;


/**
 * Main Class for the Interpreter.
 * 
 * @author chiragpa
 *
 */
public class MyInt {
	
	
	
	
	/**
	 * main() method for the Interpreter. Scans the input from the Std in, 
	 * parses it and then evaluates. The result of evaluation is printed to Std out by writeResult() method.
	 * 
	 * @param String args[]
	 * @return void
	 */
	public static void main(String[] args) {

		//String userDir =System.getProperty("user.dir");
		//System.out.println(userDir);
		Scanner scanner = new Scanner();
		try {
			//scanner.scan(userDir+"/bin/test1");
			scanner.scan(null);
			Parser parser = new Parser(scanner);

			try {

				while(scanner.hasToken()  && !TokenCategory.EOF.equals((scanner.peek()).getCategory())){	
					try{
						Evaluator evaluator  = new Evaluator(parser.parseS(),new HashMap());	
						writeResult(evaluator.evaluate());
						//writeResult(parser.parseS());
						System.out.println("\n");
					}catch(EvaluationException e){
						System.out.println(e.getMessage());
					}
				}
			} catch (ParserException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println(e.getMessage());
			}
		} catch (ScannerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}




	}


	/**
	 * If the parseTree is a List (ends with a NIL), print the output in the List format.
	 *
	 * @param BinaryTree paramTree
	 * @return void
	 *
	 */
	private static void writeList(BinaryTree parseTree){
		System.out.print("( ");
		BinaryTree tempTree = parseTree;
		while(tempTree instanceof CompoundNode){
			BinaryTree leftSubTree = ((CompoundNode)tempTree).getLeftSubTree();
			if(leftSubTree instanceof AtomNode){
				System.out.print(((AtomNode)leftSubTree).getToken()+ " " );
			}else{
				writeResult(leftSubTree);
			}

			tempTree = ((CompoundNode)tempTree).getRightSubTree();
		}
		System.out.print(") ");


	}

	/**
	 * If the parseTree is not a List, print the output in the dot format.
	 * 
	 * @param BinaryTree paramTree
	 * @return void
	 */
	private static void writeSExpression(BinaryTree parseTree){

		System.out.print("( ");
		writeResult(((CompoundNode) parseTree).getLeftSubTree());
		System.out.print(" . ");
		writeResult(((CompoundNode) parseTree).getRightSubTree());
		System.out.print(") ");
	}


	/**
	 * If the parseTree is an AtomNode, prints it. Else checks if the parseTree is List or not 
	 * and calls the appropriate write() method.
	 * 
	 * @param BinaryTree parseTree
	 * @return void
	 */
	private static void writeResult(BinaryTree parseTree){
		if(parseTree instanceof AtomNode){
			System.out.print(((AtomNode)parseTree).getToken() + " ");
		}/*else if( parseTree instanceof NilNode){
		 		 //ignore
		 		System.out.print("NIL ");
		 	 }*/else if(null == parseTree){
		 		 System.out.print("NIL ");

		 	 }else {
		 		 if(parseTree.isList()){
		 			 writeList(parseTree);
		 		 }else {
		 			 writeSExpression(parseTree);
		 		 }
		 	 }
	}

}
