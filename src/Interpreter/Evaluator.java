package Interpreter;

import java.util.HashMap;

import Exception.EvaluationException;
import ParseTreeStructure.AtomNode;
import ParseTreeStructure.BinaryTree;
import ParseTreeStructure.CompoundNode;
import Tokens.Token;
import Tokens.TokenCategory;

public class Evaluator {
	
	private BinaryTree expression;
	private HashMap<String, BinaryTree> aListMap;
	private static final String T = "T";
	private static final String NIL = "NIL";
	
	public Evaluator(BinaryTree parseS , HashMap<String , BinaryTree> aListMap){
		this.expression = parseS;
		this.aListMap = aListMap;
		//aListMap.put("A", "2");
	}
	

	public BinaryTree evaluate() throws EvaluationException {
		try{
			if ( expression instanceof AtomNode){
				if(T.equals(((AtomNode) expression).getToken().getLexValue())){
					return expression;
				}else if(NIL.equals(((AtomNode) expression).getToken().getLexValue())){
					//return new NilNode();
					return expression;
				}else if(TokenCategory.NUMERIC_ATOM.equals(((AtomNode) expression).getToken().getCategory())){
					return expression;
					
				}else /*if(TokenCategory.LITERAL_ATOM.equals(((AtomNode) expression).getToken().getCategory()))*/{
					if(aListMap.containsKey(((AtomNode) expression).getToken().getLexValue()) ){
						//return new AtomNode(new Token (aListMap.get(((AtomNode) expression).getToken().getLexValue()).toString() , TokenCategory.NUMERIC_ATOM));
						return aListMap.get(((AtomNode) expression).getToken().getLexValue());
					}else{
						throw new EvaluationException("UNBOUND LITERAL ATOM - "+ ((AtomNode) expression).getToken().getLexValue());
					}
					
				}
			}/*else if (expression instanceof NilNode){
				return expression;
			}*/else /*if (expression instanceof CompoundNode)*/{
				BinaryTree car = ((CompoundNode) expression).getCAR();
				
				if((car instanceof AtomNode)  && Operator.QUOTE.equals(((AtomNode) car).getToken().getLexValue())){
					return quote( ((CompoundNode) expression).getCDR());
				}else if ((car instanceof AtomNode) && Operator.COND.equals(((AtomNode) car).getToken().getLexValue())){
					BinaryTree cdr = ((CompoundNode) expression).getCDR();
					return evcon(cdr,aListMap,DList.getInstance().getDListMap());
					
				}else if ((car instanceof AtomNode)  && Operator.DEFUN.equals(((AtomNode) car).getToken().getLexValue())){
					return defun(((CompoundNode) expression).getCDR());
				}else{
					BinaryTree cdr = ((CompoundNode) expression).getCDR();
					return apply(car,evlist(cdr,aListMap,DList.getInstance().getDListMap()),aListMap,DList.getInstance().getDListMap());
				}
				
				
			}
		}catch(ClassCastException e){
			throw new EvaluationException("Error while evaluating");
		}
		
		
		
	}


	private BinaryTree defun(BinaryTree expression) throws EvaluationException{
		try{
			if(((CompoundNode) expression).length() == 3){
				 AtomNode functionName = (AtomNode) ((CompoundNode) expression).getCAR();
				 BinaryTree params =  ((CompoundNode) ((CompoundNode) expression).getCDR()).getCAR();
				 BinaryTree body =  ((CompoundNode) ((CompoundNode) ((CompoundNode) expression).getCDR()).getCDR()).getCAR();
				 
				 validateFunctionName(functionName);
				 
				 if(validateFormals(params)){
					 DList.getInstance().getDListMap().put(functionName.getToken().getLexValue(), ((CompoundNode) expression).getCDR());
					 return functionName;
				 }else{
						throw new EvaluationException("Invalid Function Params");
				 }
				
				
			}else{
				throw new EvaluationException("Invalid Number of Arguments to DEFUN");
			}
		}catch(ClassCastException e){
			throw new EvaluationException("Illegal Arguments to DEFUN");
		}
		
	}
	
	private void validateFunctionName(AtomNode functionName) throws EvaluationException{
		if(TokenCategory.LITERAL_ATOM.equals(functionName.getToken().getCategory())){
			String name = functionName.getToken().getLexValue();
			if(name.equalsIgnoreCase(Operator.PLUS)
					|| name.equalsIgnoreCase(Operator.MINUS) || name.equalsIgnoreCase(Operator.ATOM)
					|| name.equalsIgnoreCase(Operator.TIMES) || name.equalsIgnoreCase(Operator.REMAINDER)
					|| name.equalsIgnoreCase(Operator.NULL) || name.equalsIgnoreCase(Operator.CAR)
				|| name.equalsIgnoreCase(Operator.CDR) || name.equalsIgnoreCase(Operator.QUOTE)
				|| name.equalsIgnoreCase(Operator.DEFUN) || name.equalsIgnoreCase(Operator.EQ)
				|| name.equalsIgnoreCase(Operator.LESS) || name.equalsIgnoreCase(Operator.GREATER)
				|| name.equalsIgnoreCase(Operator.CONS)  || name.equalsIgnoreCase(Operator.INT)
				|| name.equalsIgnoreCase(Operator.COND)  || name.equalsIgnoreCase(Operator.QUOTIENT)){
				throw new EvaluationException("Cannot redefine inbuilt function : "+ name);
			}
		}else{
			throw new EvaluationException("Function Names should be Literal Atoms");
		}
		
	}


	private boolean validateFormals(BinaryTree params) throws EvaluationException {
		HashMap paramMaps =  new HashMap();
		if(params.isList()){
			validateDuplicateFormals(params, paramMaps);
			return true;
		}else{
			throw new EvaluationException("Function Formals should be a list");
		}
		
	}


	private void validateDuplicateFormals(BinaryTree params, HashMap paramMaps)
			throws EvaluationException {
		BinaryTree leftSubTree ;
		while(params instanceof CompoundNode){
			leftSubTree =  ((CompoundNode) params).getLeftSubTree();
			if(paramMaps.containsKey(((AtomNode) leftSubTree).getToken().getLexValue())){
				throw new EvaluationException("Duplicate Formals in Function definition");
			}else{
				if(T.equals(((AtomNode) leftSubTree).getToken().getLexValue())
						|| NIL.equals(((AtomNode) leftSubTree).getToken().getLexValue())){
					throw new EvaluationException("Function Formals cannot be T or NIL");
				}
				if(!TokenCategory.LITERAL_ATOM.equals(((AtomNode) leftSubTree).getToken().getCategory())){
					throw new EvaluationException("Function Formals should be literal atoms");
				}
				paramMaps.put(((AtomNode) leftSubTree).getToken().getLexValue(), 0);
			}
			params = ((CompoundNode) params).getRightSubTree();
		}
	}


	private void addPairs(BinaryTree params, BinaryTree arguments , String functionName) throws EvaluationException{
		if(params.length() == arguments.length()){
			String paramName = null;
			BinaryTree paramValue = null;
			while (!params.isNILNode()){
				paramName = ((AtomNode) ((CompoundNode) params).getCAR()).getToken().getLexValue();
				paramValue =  ((CompoundNode) arguments).getCAR();
				aListMap.put(paramName,paramValue);
				params = ((CompoundNode) params).getCDR();
				arguments =  ((CompoundNode) arguments).getCDR();
			}
			
		}else{
			throw new EvaluationException("Invalid Number of Arguments to " +  functionName);
		}
		
	}


	private BinaryTree quote(BinaryTree expression) throws EvaluationException{
		
		if(((CompoundNode) expression).length() == 1){
			return ((CompoundNode) expression).getCAR();
		}else{
			throw new EvaluationException("Invalid Number of Arguments to Quote");
		}
		
	}


	private BinaryTree evcon(BinaryTree exp, HashMap aListMap2, HashMap dListMap)  throws EvaluationException{
		
		
		if (null == exp || exp.isNILNode()){
			throw new EvaluationException ("Error evaluating COND");
		}else{
			Evaluator evaluator = new Evaluator(((CompoundNode) ((CompoundNode) exp).getCAR()).getCAR(),aListMap);
			AtomNode result = (AtomNode) evaluator.evaluate();
			if(T.equals(result.getToken().getLexValue())){
				Evaluator evaluator1 = new Evaluator(((CompoundNode) ((CompoundNode) ((CompoundNode) exp).getCAR()).getCDR()).getCAR(),aListMap);
				return evaluator1.evaluate();
			}else{
				return evcon(((CompoundNode)exp).getCDR(), aListMap , DList.getInstance().getDListMap());
			}
			
		}
		
	}
	

	private BinaryTree evlist(BinaryTree exp, HashMap aListMap, HashMap dListMap) throws EvaluationException {
		if (null == exp || exp.isNILNode()){
			//return new NilNode();
			return new AtomNode(new Token(NIL,TokenCategory.LITERAL_ATOM));
		}else{
			Evaluator evaluator = new Evaluator(((CompoundNode) exp).getCAR(),aListMap);
			return cons(evaluator.evaluate(),evlist(((CompoundNode) exp).getCDR(),aListMap,dListMap));
		}
		
	}


	private BinaryTree apply(BinaryTree exp, BinaryTree arguments,HashMap aListMap ,HashMap dListMap) throws EvaluationException {
		if(exp instanceof AtomNode){
			if(Operator.CAR.equals(((AtomNode) exp).getToken().getLexValue())){
				return evalCAR(arguments);
			}else if (Operator.CDR.equals(((AtomNode) exp).getToken().getLexValue())){
				return evalCDR(arguments);
			}else if (Operator.NULL.equals(((AtomNode) exp).getToken().getLexValue())){
				return evalUnaryOperators(arguments, Operator.NULL);
			}else if (Operator.INT.equals(((AtomNode) exp).getToken().getLexValue())){
				return evalUnaryOperators(arguments, Operator.INT);
			}else if (Operator.ATOM.equals(((AtomNode) exp).getToken().getLexValue())){
				return evalUnaryOperators(arguments, Operator.ATOM);
			}else if(Operator.CONS.equals(((AtomNode) exp).getToken().getLexValue())){
				return evalCONS(arguments);				
			}else if (Operator.PLUS.equals(((AtomNode) exp).getToken().getLexValue())){
				 return evalArithmaticOperators(arguments, Operator.PLUS);
			}else if (Operator.MINUS.equals(((AtomNode) exp).getToken().getLexValue())){		
				 return evalArithmaticOperators(arguments, Operator.MINUS);
			}else if (Operator.TIMES.equals(((AtomNode) exp).getToken().getLexValue())){
				 return evalArithmaticOperators(arguments, Operator.TIMES);
			}else if (Operator.QUOTIENT.equals(((AtomNode) exp).getToken().getLexValue())){		
				 return evalArithmaticOperators(arguments, Operator.QUOTIENT);
			}else if (Operator.REMAINDER.equals(((AtomNode) exp).getToken().getLexValue())){
				 return evalArithmaticOperators(arguments, Operator.REMAINDER);
			}else if (Operator.EQ.equals(((AtomNode) exp).getToken().getLexValue())){
				return evalRelationalOperators(arguments,Operator.EQ);
			}else if (Operator.LESS.equals(((AtomNode) exp).getToken().getLexValue())){
				return evalRelationalOperators(arguments,Operator.LESS);
			}else if (Operator.GREATER.equals(((AtomNode) exp).getToken().getLexValue())){
				return evalRelationalOperators(arguments,Operator.GREATER);
			}else{
				//TODO evaluate user defined function
				String functionName = ((AtomNode) exp).getToken().getLexValue();
				if(DList.getInstance().getDListMap().containsKey(functionName)){
					BinaryTree expression = DList.getInstance().getDListMap().get(functionName);
					BinaryTree body = ((CompoundNode) ((CompoundNode) expression).getCDR()).getCAR();
					BinaryTree params =((CompoundNode) expression).getCAR();
					
					addPairs(params,arguments,functionName);
					
					Evaluator evaluate = new Evaluator(body,aListMap);
					return evaluate.evaluate();
					
					
				}else{
					throw new EvaluationException("Undefined function " + functionName);
				}
				
				
			}
		}else{
		
			throw new EvaluationException("Error while evaluating");
		}
		
		//throw new EvaluationException("No such function");
		
	}
	
	
	


	private BinaryTree evalRelationalOperators(BinaryTree params , final String operator) throws EvaluationException{
		try{
			if(((CompoundNode) params).length() == 2){
				if(Operator.EQ.equals(operator)){
					return evalEQ(params);
				}else if(Operator.LESS.equals(operator)){
					return evalLESS(params);
				}else if(Operator.GREATER.equals(operator)){
					return evalGREATER(params);
				}
				
				return null;
				 
			 }else{
				 throw new EvaluationException("Invalid Number of Params to "+operator);
			 }
		}catch(ClassCastException ce){
			 throw new EvaluationException("Invalid Arguments to "+operator);
		}
	}


	private BinaryTree evalGREATER(BinaryTree params)
			throws EvaluationException {
		String firstParam = ((AtomNode)((CompoundNode) params).getCAR()).getToken().getLexValue();
		 String secondParam = ((AtomNode) ((CompoundNode) ((CompoundNode) params).getCDR()).getCAR()).getToken().getLexValue();
		 int param1 = getParam(firstParam);
		 int param2 =  getParam(secondParam);
		 
		 if( param1 >  param2){
			 return new AtomNode(new Token(T, TokenCategory.LITERAL_ATOM));
		 }else{
			 //return new  NilNode();
			 return new AtomNode(new Token(NIL,TokenCategory.LITERAL_ATOM));
		 }
	}


	private int getParam(String param) throws EvaluationException {
		int param1;
		 try{
			  param1 = Integer.parseInt(param);
		 }catch(NumberFormatException ne){
			 if(aListMap.containsKey(param)){
				 try{
					 AtomNode atom = (AtomNode) aListMap.get(param);
					 param1 = Integer.parseInt(atom.getToken().getLexValue());
				 }catch(NumberFormatException ne1){
					 throw new EvaluationException("Unbound Atom - " + param);
				 }
			 }else{
				 throw new EvaluationException("Unbound Atom - " + param);
			 }
		 }
		return param1;
	}


	private BinaryTree evalLESS(BinaryTree params) throws EvaluationException {
		String firstParam = ((AtomNode)((CompoundNode) params).getCAR()).getToken().getLexValue();
		 String secondParam = ((AtomNode) ((CompoundNode) ((CompoundNode) params).getCDR()).getCAR()).getToken().getLexValue();
		 int param1 = getParam(firstParam);
		 int param2 = getParam(secondParam);
		 if( param1 < param2){
			 return new AtomNode(new Token(T, TokenCategory.LITERAL_ATOM));
		 }else{
			 //return new  NilNode();
			 return new AtomNode(new Token(NIL,TokenCategory.LITERAL_ATOM));
		 }
	}


	private BinaryTree evalEQ(BinaryTree params) throws EvaluationException {
		
			 String firstParam = ((AtomNode)((CompoundNode) params).getCAR()).getToken().getLexValue();
			 String secondParam = ((AtomNode) ((CompoundNode) ((CompoundNode) params).getCDR()).getCAR()).getToken().getLexValue();
			

			 if(T.equals(firstParam) && T .equals(secondParam)){
				 return new AtomNode(new Token(T,TokenCategory.LITERAL_ATOM));

			 }else if(T.equals(firstParam) && NIL .equals(secondParam)){
				 return new AtomNode(new Token(NIL,TokenCategory.LITERAL_ATOM));
			 }else if(NIL.equals(firstParam) && T .equals(secondParam)){
				 return new AtomNode(new Token(NIL,TokenCategory.LITERAL_ATOM));
			 }else if(NIL.equals(firstParam) && NIL .equals(secondParam)){
				 return new AtomNode(new Token(T,TokenCategory.LITERAL_ATOM));

			 }else if(firstParam.equals(secondParam)){
				 return new AtomNode(new Token(T,TokenCategory.LITERAL_ATOM));
			 }else{
				 return new AtomNode(new Token(NIL,TokenCategory.LITERAL_ATOM));
			 }
		 
		 
	}


	private BinaryTree evalREMAINDER(BinaryTree params)
			throws EvaluationException {
		String firstParam = ((AtomNode)((CompoundNode) params).getCAR()).getToken().getLexValue();
		 String secondParam = ((AtomNode) ((CompoundNode) ((CompoundNode) params).getCDR()).getCAR()).getToken().getLexValue();
		 int param1 = getParam(firstParam);
		 int param2 = getParam(secondParam);
		 
		 return new AtomNode(new Token(param1 % param2 +"", TokenCategory.NUMERIC_ATOM));
	}


	private BinaryTree evalQUOTIENT(BinaryTree params)
			throws EvaluationException {
		String firstParam = ((AtomNode)((CompoundNode) params).getCAR()).getToken().getLexValue();
		 String secondParam = ((AtomNode) ((CompoundNode) ((CompoundNode) params).getCDR()).getCAR()).getToken().getLexValue();
		 int param1 = getParam(firstParam);
		 int param2 = getParam(secondParam);
		 
		 return new AtomNode(new Token(param1 / param2 +"", TokenCategory.NUMERIC_ATOM));
	}


	private BinaryTree evalTIMES(BinaryTree params) throws EvaluationException {
		String firstParam = ((AtomNode)((CompoundNode) params).getCAR()).getToken().getLexValue();
		 String secondParam = ((AtomNode) ((CompoundNode) ((CompoundNode) params).getCDR()).getCAR()).getToken().getLexValue();
		 int param1 = getParam(firstParam);
		 int param2 = getParam(secondParam);
		 
		 return new AtomNode(new Token(param1 * param2 +"", TokenCategory.NUMERIC_ATOM));
	}


	private BinaryTree evalMINUS(BinaryTree params) throws EvaluationException {
		String firstParam = ((AtomNode)((CompoundNode) params).getCAR()).getToken().getLexValue();
		 String secondParam = ((AtomNode) ((CompoundNode) ((CompoundNode) params).getCDR()).getCAR()).getToken().getLexValue();
		 int param1 = getParam(firstParam);
		 int param2 = getParam(secondParam);
		 
		 return new AtomNode(new Token(param1 - param2 +"", TokenCategory.NUMERIC_ATOM));
	}


	private BinaryTree evalArithmaticOperators(BinaryTree params ,final String operator)
			throws EvaluationException {
		try{
			if(((CompoundNode) params).length() == 2){
				if(Operator.PLUS.equals(operator)){
					return evalPLUS(params);
				}else if(Operator.MINUS.equals(operator)){
					return evalMINUS(params);
				}else if(Operator.TIMES.equals(operator)){
					return evalTIMES(params);
				}else if(Operator.QUOTIENT.equals(operator)){
					return evalQUOTIENT(params);
				}else if(Operator.REMAINDER.equals(operator)){
					return evalREMAINDER(params);
				}
				
				return null;
				 
			 }else{
				 throw new EvaluationException("Invalid Number of Params to "+operator);
			 }
		}catch(ClassCastException ce){
			 throw new EvaluationException("Invalid Params to "+operator);
		}
	}


	private BinaryTree evalPLUS(BinaryTree params) throws EvaluationException {
		String firstParam = ((AtomNode)((CompoundNode) params).getCAR()).getToken().getLexValue();
		 String secondParam = ((AtomNode) ((CompoundNode) ((CompoundNode) params).getCDR()).getCAR()).getToken().getLexValue();
		 int param1 = getParam(firstParam);
		 int param2 = getParam(secondParam);
		 
		 return new AtomNode(new Token(param1 + param2 +"", TokenCategory.NUMERIC_ATOM));
	}


	private BinaryTree evalCONS(BinaryTree params) throws EvaluationException{
		try{
			if(((CompoundNode) params).length() == 2){
				BinaryTree param1 = ((CompoundNode) params).getCAR();
				BinaryTree param2 = ((CompoundNode) ((CompoundNode) params).getCDR()).getCAR();
				
				return cons(param1,param2);
			}else{
				throw new EvaluationException("Invalid Number of Arguments to CONS");
			}
		}catch(ClassCastException ce){
			throw new EvaluationException("Invalid Arguments to CONS");
			
		}
		
	}

	private BinaryTree evalUnaryOperators(BinaryTree params ,final String operator) throws EvaluationException{
		try{
			if(((CompoundNode) params).length() == 1){
				if(Operator.INT.equals(operator)){
					return evalINT(params);
				}else if(Operator.NULL.equals(operator)){
					return evalNULL(params);
				}else if(Operator.ATOM.equals(operator)){
					return evalATOM(params);
				}
				
				return null;
				 
			 }else{
				 throw new EvaluationException("Invalid Number of Params to "+operator);
			 }
		}catch(ClassCastException ce){
			 throw new EvaluationException("Invalid Argument to "+operator);
		}
	}

	private BinaryTree evalATOM(BinaryTree params) throws EvaluationException {
		
				BinaryTree param = ((CompoundNode) params).getCAR();
				if(param instanceof AtomNode || param.isNILNode()){
					return new AtomNode(new Token(T,TokenCategory.LITERAL_ATOM));
				}else{
					//return new NilNode();
					return new AtomNode(new Token(NIL,TokenCategory.LITERAL_ATOM));
				}
			
		
	}


	private BinaryTree evalINT(BinaryTree params) throws EvaluationException{
		
				BinaryTree param = ((CompoundNode) params).getCAR();
				if(param instanceof AtomNode && TokenCategory.NUMERIC_ATOM.equals(((AtomNode) param).getToken().getCategory())){
					return new AtomNode(new Token(T,TokenCategory.LITERAL_ATOM)); 
				}else if(param instanceof AtomNode && TokenCategory.LITERAL_ATOM.equals(((AtomNode) param).getToken().getCategory()) ){
					if(aListMap.containsKey(((AtomNode) param).getToken().getLexValue())){
						try{
							 AtomNode atom = (AtomNode) aListMap.get(((AtomNode) param).getToken().getLexValue());
							 Integer.parseInt(atom.getToken().getLexValue());
							//Integer.parseInt((String)aListMap.get(((AtomNode) param).getToken().getLexValue()));
						}catch(NumberFormatException ne){
							//return new NilNode();
							return new AtomNode(new Token(NIL,TokenCategory.LITERAL_ATOM));
						}
						
						return new AtomNode(new Token(T,TokenCategory.LITERAL_ATOM));
						
					}else{
						throw new EvaluationException("Unboud Literal Atom - " + ((AtomNode) param).getToken().getLexValue());
					}
					
				}else{
					return new AtomNode(new Token(NIL,TokenCategory.LITERAL_ATOM));
				}
			}


	private BinaryTree evalNULL(BinaryTree params) throws EvaluationException {
		
				BinaryTree param = ((CompoundNode) params).getCAR();
				if(param == null || param.isNILNode() ){
					return new AtomNode(new Token(T,TokenCategory.LITERAL_ATOM));
				}else{
					//return new NilNode();
					return new AtomNode(new Token(NIL,TokenCategory.LITERAL_ATOM));
				}
			
	}


	private BinaryTree evalCDR(BinaryTree params) throws EvaluationException {
		try{	
			return ((CompoundNode) ((CompoundNode) params).getCAR()).getCDR();
		}catch(ClassCastException ce){
			throw new EvaluationException("Invalid Arguments to CDR");
		}
	}


	private BinaryTree evalCAR(BinaryTree params) throws EvaluationException {
		try{
			return ((CompoundNode) ((CompoundNode) params).getCAR()).getCAR();
		}catch(ClassCastException ce){
			throw new EvaluationException("Invalid Arguments to CAR");
		}
	}




	private BinaryTree cons(BinaryTree left, BinaryTree right) {
		
		return new CompoundNode(left,right);
		
	}

}
