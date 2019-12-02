package graphs;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jgrapht.ext.DOTExporter;
import org.jgrapht.graph.DefaultDirectedGraph;

public class CDG {

	DefaultDirectedGraph<Node, Edge> cdg;
	
	Node first;
	
	
	public CDG(CFG augmentedCFG, CFG pdt){
		cdg = new DefaultDirectedGraph<Node, Edge>(Edge.class);
		HashSet<Edge>  s = new HashSet<Edge>();
		CFG aux = new CFG();
//		aux.addAllNodes(pdt);

		for (Node n : augmentedCFG.cfg.vertexSet()){
			cdg.addVertex(n);
		}
		Set<Node> a = augmentedCFG.getGraph().vertexSet();
		for (Edge e: augmentedCFG.getGraph().edgeSet()) {
			Node n1 = augmentedCFG.getGraph().getEdgeSource(e);
			Node n2 = augmentedCFG.getGraph().getEdgeTarget(e);
			//v is for control. TODO:remove
			Set<Node> v = pdt.getSubGraph(n2, n1).getGraph().vertexSet();
			if(v.size() == 2) //si es ancestro
			{
				aux.addAllEdges(pdt.getSubGraph(n2, n1));
			}
			
		}
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
