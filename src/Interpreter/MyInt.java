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


public class MyInt {
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
	 
	 private static void writeSExpression(BinaryTree parseTree){
		
		 System.out.print("( ");
		 writeResult(((CompoundNode) parseTree).getLeftSubTree());
		 System.out.print(" . ");
		 writeResult(((CompoundNode) parseTree).getRightSubTree());
		 System.out.print(") ");
	 }
	 
	 
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
