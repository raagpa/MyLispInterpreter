package Interpreter;


import Exception.ParserException;
import Exception.ScannerException;
import Interpreter.Scanner;
import ParseTreeStructure.AtomNode;
import ParseTreeStructure.BinaryTree;
import ParseTreeStructure.CompoundNode;
import ParseTreeStructure.NilNode;
import Tokens.Token;
import Tokens.TokenCategory;


public class MyInt {
	 public static void main(String[] args) {
	    	
	    	String userDir =System.getProperty("user.dir");
	    	System.out.println(userDir);
	    	Scanner scanner = new Scanner();
	    	try {
				scanner.scan(userDir+"/bin/test1");
				Parser parser = new Parser(scanner);
		    	try {
		    		
		    		while(scanner.hasToken()  && !TokenCategory.EOF.equals((scanner.peek()).getCategory())){
		    			System.out.println("\n");
		    			printResult(parser.parseS());
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
	 
	
	 
	 /*private static void write(BinaryTree parseTree){
		 if(parseTree instanceof AtomNode){
			 System.out.print(((AtomNode)parseTree).getToken() + " ");
		 }else if(parseTree instanceof NilNode){
			 System.out.print(") ");
		 }else{
			 if(parseTree.isList()){
				 writeList(parseTree);
			 }else if(parseTree.isDotNotation()){
				 writeSExpression(parseTree);
			 }else{
				 writeNode(parseTree);
			 }
		 }

	 }*/
	 
	/* private static void writeNode(BinaryTree parseTree){
		 write(((CompoundNode) parseTree).getLeftSubTree());
		 write(((CompoundNode) parseTree).getRightSubTree());
	 }*/
	 
	 private static void writeList(BinaryTree parseTree){
		 System.out.print("( ");
		 BinaryTree tempTree = parseTree;
		 while(tempTree instanceof CompoundNode){
			BinaryTree leftSubTree = ((CompoundNode)tempTree).getLeftSubTree();
			if(leftSubTree instanceof AtomNode){
				 System.out.print(((AtomNode)leftSubTree).getToken() + " ");
			}else{
				printResult(leftSubTree);
			}
			
			tempTree = ((CompoundNode)tempTree).getRightSubTree();
		 }
		 System.out.print(") ");
		 

	 }
	 
	 private static void writeSExpression(BinaryTree parseTree){
		
		 System.out.print("( ");
		 printResult(((CompoundNode) parseTree).getLeftSubTree());
		 System.out.print(" . ");
		 printResult(((CompoundNode) parseTree).getRightSubTree());
		 System.out.print(") ");
	 }
	 
	 
	 private static void printResult(BinaryTree parseTree){
		 	 if(parseTree instanceof AtomNode){
		 		System.out.print(((AtomNode)parseTree).getToken() + " ");
		 	 }else if( parseTree instanceof NilNode){
		 		 //ignore
		 	 }else {
				 if(parseTree.isList()){
					 writeList(parseTree);
				 }else {
					 writeSExpression(parseTree);
				 }
		 	 }
		 }
	
}
