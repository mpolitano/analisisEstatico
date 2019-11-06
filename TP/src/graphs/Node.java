package graphs;

import ast.Expression;
import ast.Statement;

public class Node {

	Statement stmt;
	
	Expression expr;
	
	public Node(Statement stmt){
		this.stmt = stmt;
	} 
	
	public Node(Expression expr){
		this.expr = expr;
	}
	
}
