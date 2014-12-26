#############################################################################

Author :: Chirag Parekh (parekh.65@osu.edu)

#############################################################################

Note ::
1. All the compile code goes to bin directory. Before running the make command
   make sure that bin directory is present. Empty bin directory has been provided
   within the main directory.

#############################################################################

Description ::

The entire project is divided into 4 packages

1. Interpreter
   This package contains 
	
	
	
	Scanner.java -  reads the input from System.in, tokenizes the input into below mentioned tokens and stores it in List<Token>tokenList.
Since the scanners tokenizes the entire input first, if there is any error like a NUMERIC_ATOM having invalid characters or DOT token followed by characters other than whitespace, ( or ) that are being encountered during tokenizing, an ERROR token is stored in the tokenList with the appropriate message.
When this ERROR token is encountered while parsing Error is displayed and execution halts. This is done to avoid scenarios where in there are multiple expressions in input and a later expression has an error. This will allow the interpreter to interpreter to interprete the previous valid expressions (for now the parser to parse the previous valid expressions).

	Parser.java - it takes one token at a time and parses it using the grammar provided in Project description.
It checks the next token from the tokenList and decides which production to apply. If an invalid token is encountered an error is thrown mentioning what was the expected token and what was the actual token.
	Along with parsing, the parser also creates a Binary Tree for expression. Binary tree may have an atom node, compound node or a Nil node depending on the production that is applied.
	Once an expression is parsed successfully the control return back to MyInt where in the expression is printed using the printing algorithm provided in Project description.


	MyInt.java - main class which is the interpreter. It calls Scanner and Parser and then prints the results.
Once an expression is parsed successfully it is sent to the Evaluator.evaluate().Output of evaluate() is also a BinaryTree and it sent to writeResults() to print the result. It checks if the parseTree is just an atomNode then it prints it, else if is is compoundNode it, it checks whether it is a list or not (if right most leaf of the compoundNode is Nil Node then it is a list). Based on this it either calls writeList() or writeSExpression().

	A separate aList (aListMap) is maintained for each expression that is evaluated. A global dList is maintained by a Singleton class DList. evaluation() follows the same logic that was discussed in class.

	

2. Exception
	Different exception classes to distinguish the stage of interpreter where the error occured.
	ScannerExpception.java 
	ParserException.java
	EvaluationExpression.java

3. ParseTreeStructure
	BinaryTree.java - is the base class
	AtomNode.java -  subclass of BinaryTree - has a token field.
	NilNode.java - subclass of BinaryTree - to represent Nil.
	CompoundNode.java - subclass of BinaryTree - has two fields to store to leftSubTree and rightSubTree.

4. Tokens
	Token.java - It stores the lexValue of the token and has tokenCategory where tokenCategory can be only the values defined in the enum.

	TokenCategory.java - is an enum 
	OPEN_PARENTHESIS,
	CLOSE_PARENTHESIS,
	DOT,
	LITERAL_ATOM,
	NUMERIC_ATOM,
	EOF,
	ERROR