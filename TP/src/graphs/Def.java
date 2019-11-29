package graphs;

import ast.Statement;

public class Def {

	private Statement stmt;
	private Node node;
	
	public Def(Statement s, Node n){
		stmt = s;
		node = n;
	}
	
	public Statement getStmt(){
		return stmt;
	}
	
	public Node getNode(){
		return node;
	}
	
	@Override
	public String toString(){
		return stmt.toString();
	}
	
}
