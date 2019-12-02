package graphs;

import java.io.FileWriter;
import java.io.IOException;

import org.jgrapht.ext.DOTExporter;
import org.jgrapht.graph.DefaultDirectedGraph;

public class CDG {

	DefaultDirectedGraph<Node, Edge> cdg;
	
	Node first;
	
	
	public CDG(CFG augmentedCFG, DefaultDirectedGraph<Node, Edge> postDomGraph){
		cdg = new DefaultDirectedGraph<Node, Edge>(Edge.class);
		
		for (Node n : augmentedCFG.cfg.vertexSet()){
			cdg.addVertex(n);
		}
		
		//si no son ancestros agregar el edge
		
		//anteriror a n1 y n2 de edge -> buscar path y agregar edges desde n1 a cada uno del path del anterior
		
		// consultar algoritmo. creo que termina ahí (ver T y F de los edges)
		
		
	}
	
	public void toDot(String fileName) throws IOException{
		FileWriter f = new FileWriter(fileName);
		DOTExporter<Node, Edge> dot = new DOTExporter<Node, Edge>();
		dot.exportGraph(cdg, f);
	}
	
}
