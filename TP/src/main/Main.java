package main;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;

import org.jgrapht.graph.DefaultDirectedGraph;

import ast.Program;
import graphs.ASTtoCFGVisitor;
import graphs.CDG;
import graphs.CFG;
import graphs.DDG;
import graphs.Edge;
import graphs.Node;
import graphs.PDG;

public class Main {
	private static parser parser;	// Parser
    private static FileReader programFile;

	static public void main(String[] argv) {
		try {
			String fileName = "graph.dot";
			FileWriter f = new FileWriter(fileName);
			parser p = new parser(new AnalizadorLexico( new FileReader("src/test/test")));
			Program result = (Program) p.parse().value;
			System.out.println("Parser successfully created");
			System.out.println("Translating AST to CFG");
			CFG cfg = new CFG();
			ASTtoCFGVisitor visitor = new ASTtoCFGVisitor();
			cfg = result.accept(visitor);
			cfg.toDot(fileName);
			System.out.println("--> CFG successfully created in TP/graph.dot ");
			
			fileName = "reverseGraph.dot";
			f = new FileWriter(fileName);
			System.out.println("--> reverse of CFG successfully created in TP/reverseGraph.dot ");
			CFG reverse = cfg.reverse();
			reverse.toDot(fileName);
			
			System.out.println("dominadores: " + cfg.dominadores().toString());
			System.out.println("post dominadores: " + cfg.postDom().toString());
			
			
			DefaultDirectedGraph<Node, Edge> posDomTree = cfg.postDomTree();
			fileName = "postDomTree.dot";
			f = new FileWriter(fileName);
			System.out.println("--> Post dominator tree successfully created in TP/postDomTree.dot ");
			CFG toPrint = new CFG();
			toPrint.setCFG(posDomTree);
			toPrint.toDot(fileName);
			System.out.println("--> Control Dependence Graph <--");
			fileName = "controlDependenceGraph.dot";
			f = new FileWriter(fileName);
			System.out.println("--> Control Dependence Graph successfully created in TP/controlDependenceGraph.dot ");
			CDG cdg = new CDG(cfg.getAugmentedCFG(), posDomTree);
			cdg.toDot(fileName);
			
			
			System.out.println("Starting Reaching Definitions (Data flow analysis)");
			cfg.setProgram(result);
			cfg.computeGenAndKill();
			System.out.println("Gen and Kill ready");
			cfg.reachingDefs();
			System.out.println("Reaching defs ready");
			System.out.println("Results of reaching defs algorithm : ");
			for (Node n : cfg.getGraph().vertexSet()){
				n.showReachingDefResult();
			}
			
			System.out.println("Data Dependence Graph -> ");
			DDG dataDepGraph = new DDG(cfg);
			fileName = "dataDepGraph.dot";
			f = new FileWriter(fileName);
			dataDepGraph.toDot(fileName);
			System.out.println("--> Data Dependence Graph successfully created in TP/dataDepGraph.dot ");
			
			System.out.println("Program Dependence Graph -> ");
			PDG progDepGraph = new PDG(dataDepGraph, cdg);
			fileName = "progDepGraph.dot";
			f = new FileWriter(fileName);
			dataDepGraph.toDot(fileName);
			System.out.println("--> Program Dependence Graph successfully created in TP/progDepGraph.dot ");
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
            e.printStackTrace(System.out);
		}
	}

}
