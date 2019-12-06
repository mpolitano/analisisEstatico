package graphs;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jgrapht.ext.DOTExporter;
import org.jgrapht.graph.DefaultDirectedGraph;

import ast.Statement;
import tree.Tree;

public class CDG {

	DefaultDirectedGraph<Node, Edge> cdg;
	
	Node first;
	
	
	public CDG(CFG augmentedCFG, CFG postDom){
		
		//conver CFG to Tree 
		Tree<Statement> pdt= postDom.toTree();
		cdg = new DefaultDirectedGraph<Node, Edge>(Edge.class);
		HashSet<Edge>  s = new HashSet<Edge>();

		for (Node n : augmentedCFG.cfg.vertexSet()){
			cdg.addVertex(n);
		}
		for (Edge e: augmentedCFG.getGraph().edgeSet()) {
			Node n1 = augmentedCFG.getGraph().getEdgeSource(e);
			Node n2 = augmentedCFG.getGraph().getEdgeTarget(e);
			if(!pdt.isAncestor(n1, n2)) {
				s.add(e);
			}
		}
		
		
		for (Edge e: s) {
			Node n1 = augmentedCFG.getGraph().getEdgeSource(e);
			Node n2 = augmentedCFG.getGraph().getEdgeTarget(e);
		    Node least = pdt.leastCommonAncestor(n1, n2);
		      pdt.markPath(n2, least);
		      if (n1.equals(least)) {
		        pdt.addMark(n1);
		      }
		      for (Node n : pdt.getMarkedNodes()) {
		    	cdg.addVertex(n);
		        cdg.addEdge(n1, n);
		      }
		      pdt.clearMarked();
		    }

		System.out.print("finish CDG");
	}
		
		//si no son ancestros agregar el edge
		
		//anteriror a n1 y n2 de edge -> buscar path y agregar edges desde n1 a cada uno del path del anterior
		
		// consultar algoritmo. creo que termina ahí (ver T y F de los edges)
		
		
	
	
	public void toDot(String fileName) throws IOException{
		FileWriter f = new FileWriter(fileName);
		DOTExporter<Node, Edge> dot = new DOTExporter<Node, Edge>();
		dot.exportGraph(cdg, f);
	}
	
}
