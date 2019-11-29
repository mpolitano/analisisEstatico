package graphs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ast.AssignStmt;
import ast.Expression;
import ast.IfStmt;
import ast.Statement;
import ast.WhileStmt;

public class Node {

	Statement stmt;
	
	Expression expr;
	
	public boolean start = false;
	
	public boolean entry = false;
	
	public boolean fin = false;
	
	public Set<Def> gen;
	
	public Set<Def> kill;
	
	public Set<Def> in;
	
	public Set<Def> out;
	
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
	
	public Set<Def> getGen(){
		return this.gen;
	}
	
	public Set<Def> getKill(){
		return this.kill;
	}
	
	public Set<Def> getIn(){
		return this.in;
	}
	
	public Set<Def> getOut(){
		return this.out;
	}
	
	public void setGen(Set<Def> g){
		gen = g;
	}
	
	public void setKill(Set<Def> k){
		kill = k;
	}
	
	public void setIn(Set<Def> i){
		in = i;
	}
	
	public void setOut(Set<Def> o){
		out = o;
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
	
	public void computeGen(){
		gen = new HashSet<Def>();
		if (stmt instanceof AssignStmt){
			gen.add(new Def(stmt, this));
		}
	}
	
	public void computeKill(List<Statement> programStmts){
		kill = new HashSet<Def>();
		if (stmt instanceof AssignStmt){
			kill.add(new Def(stmt, this));
			computeOtherKills(kill, (AssignStmt) stmt, programStmts);
		}
	}
	
	private boolean computeOtherKills(Set<Def> killSet, AssignStmt stmt, List<Statement> programStmts){
		boolean contCondition = true;
		for (Statement s : programStmts){
			if (s instanceof AssignStmt){
				if (s.equals(stmt)){
					contCondition = false;
				}else{
					if (stmt.getID().equals(((AssignStmt) s).getID())){
						killSet.add(new Def(s, this));
					}
				}
			}
			if (s instanceof IfStmt){
				contCondition = computeOtherKills(killSet, stmt, ((IfStmt) s).getElseBlock().getStatements());
				if (contCondition)
					contCondition = computeOtherKills(killSet, stmt, ((IfStmt) s).getElseBlock().getStatements());
			}
			if (s instanceof WhileStmt){
				contCondition = computeOtherKills(killSet, stmt, ((WhileStmt) s).getBlock().getStatements());
			}
			if(!contCondition)
				break;
		}
		return contCondition;
	}

	public void showReachingDefResult() {
		System.out.println("For Node " + this.toString() + 
							" -> IN set : " + this.getIn() + 
							" -> OUT set : " + this.getOut() +
							" -> GEN set : " + this.getGen() + 
							" -> KILL set : " + this.getKill());
	}
	
}
