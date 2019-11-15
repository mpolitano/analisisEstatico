package graphs;

import ast.Expression;
import ast.Statement;

public class Node {

	Statement stmt;
	
	Expression expr;
	
	public boolean start = false;
	
	public boolean entry = false;
	
	public boolean fin = false;
	
	public Node(){}
	
	public Node(Statement stmt){
		this.stmt = stmt;
	} 
	
	public Node(Expression expr){
		this.expr = expr;
	}
	
	public void markAsStart(){
		start = true;
	}
	
	public void markAsEntry(){
		entry = true;
	}
	
	public void markAsFinal(){
		fin = true;
	}
	
	
	@Override
	public String toString(){
		if (stmt != null)
			return stmt.toString();
		else
			if (expr != null)
				return expr.toString();
			else
				if (start)
					return "Start node";
				else
					if (entry)
						return "Entry node";
					else
						if (fin)
							return "Final node";
						else
							return "Error en node";
	}
	
}
