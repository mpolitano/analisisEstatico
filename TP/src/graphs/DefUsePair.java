package graphs;

import ast.AssignStmt;
import ast.Statement;

public class DefUsePair {

	public String var;
	
	public Statement def;
	
	public Node defNode;
	
	public Statement use;
	
	public Node useNode;
	
	public DefUsePair(Statement defStmt, Node defNode, Statement useStmt, Node useNode){
		if (defStmt instanceof AssignStmt){
			var = ((AssignStmt) defStmt).getID();
			def = defStmt;
			this.defNode = defNode;
			use = useStmt;
			this.useNode = useNode;
		}
	}
	
	public String getVar(){
		return var;
	}
	
	public Statement getDef(){
		return def;
	}
	
	public Node getDefNode(){
		return defNode;
	}
	
	public Statement getUse(){
		return use;
	}
	
	public Node getUseNode(){
		return useNode;
	}
	
	@Override
	public String toString(){
		return "var: " + var + " def in: " + def.toString() + " in Node: " + defNode.toString() +
				" is used in: " + use.toString();
	}
	
}
