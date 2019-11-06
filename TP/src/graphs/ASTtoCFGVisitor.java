package graphs;

import ast.ASTVisitor;
import ast.AssignStmt;
import ast.BinOpExpr;
import ast.Block;
import ast.IfStmt;
import ast.IntLiteral;
import ast.Program;
import ast.ReturnStmt;
import ast.Statement;
import ast.Variable;
import ast.WhileStmt;

public class ASTtoCFGVisitor implements ASTVisitor<CFG>{

	CFG finalCFG;
	
	public ASTtoCFGVisitor(){
		finalCFG = new CFG();
	}
	
	@Override
	public CFG visit(Program prog) {
		finalCFG.setName(prog.getId());
		CFG result = new CFG();
		ASTtoCFGVisitor v;
		for (Statement s : prog.getMethods()){
			v = new ASTtoCFGVisitor();
			CFG partialResult = s.accept(v);
			result.concatGraph(partialResult);
		}
	//	finalCFG = prog.accept(this);
		return result;
	}

	@Override
	public CFG visit(ReturnStmt r) {
		Node node = new Node(r);
		CFG graph = new CFG();
		graph.addNode(node);
		return graph;
	}

	@Override
	public CFG visit(AssignStmt stmt) {
		Node node = new Node(stmt);
		CFG graph = new CFG();
		graph.addNode(node);
		return graph;
	}

	@Override
	public CFG visit(IfStmt stmt) {
		System.out.println("in if stmt : " + stmt.toString());
		CFG result = new CFG();
		Node condition = new Node(stmt.getCondition());
		//result.addNode(condition);
		//result.setInNode(condition);
		
		CFG thenCFG = new CFG();
		ASTtoCFGVisitor v1 = new ASTtoCFGVisitor();
		thenCFG = stmt.getIfBlock().accept(v1);
		System.out.println(stmt.getIfBlock());
		
		CFG elseCFG = new CFG();
		ASTtoCFGVisitor v2 = new ASTtoCFGVisitor();
		elseCFG = stmt.getElseBlock().accept(v2);

		result.addNode(condition);
		
		result.addAllNodes(thenCFG);
		result.addAllEdges(thenCFG);
		System.out.println(thenCFG.getGraph().vertexSet());
		result.addEdge(condition, thenCFG.getInNode());
		
		result.addAllNodes(elseCFG);
		result.addAllEdges(elseCFG);
		result.addEdge(condition, elseCFG.getInNode());
		
		result.setInNode(condition);
		for (Node n : thenCFG.getOutNodes()){
			result.addOutNode(n);
		}
		for (Node n : elseCFG.getOutNodes()){
			result.addOutNode(n);
		}
		return result;
	}

	@Override
	public CFG visit(Block stmt) {
		CFG result = new CFG();
		ASTtoCFGVisitor v;
		for (Statement s : stmt.getStatements()){
			v = new ASTtoCFGVisitor();
			CFG partialCFG = s.accept(v);
			result.concatGraph(partialCFG);
		}
		// TODO Auto-generated method stub
		return result;
	}

	@Override
	public CFG visit(WhileStmt stmt) {
		Node condition = new Node (stmt.getCondition());
		
		CFG result = stmt.getBlock().accept(this);
		
		
		
		// TODO Auto-generated method stub
		return result;
	}

	@Override
	public CFG visit(BinOpExpr expr) {
		Node node = new Node(expr);
		CFG result = new CFG();
		result.addNode(node);
		return result;
	}

	@Override
	public CFG visit(Variable expr) {
		Node node = new Node (expr);
		CFG result = new CFG();
		result.addNode(node);
		return result;
	}

	@Override
	public CFG visit(IntLiteral lit) {
		Node node = new Node (lit);
		CFG result = new CFG();
		result.addNode(node);
		return result;
	}

}
