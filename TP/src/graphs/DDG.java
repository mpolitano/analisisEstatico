package graphs;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import org.jgrapht.ext.DOTExporter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DirectedMultigraph;

public class DDG {

	//Data dependence graph
	
	//DefaultDirectedGraph<Node, Edge> ddg;
	
	//creo que tiene que ser multi directed
	DirectedMultigraph<Node, Edge> ddg;
	
	public DDG(CFG cfg){
		System.out.println("First computes DefUsePairs");
		Set<DefUsePair> defUsePair = cfg.defUsePairs();
		System.out.println("DefUsePair result: " + defUsePair.toString());
		
		//ddg = new DefaultDirectedGraph<Node, Edge>(Edge.class);
		ddg = new DirectedMultigraph<Node, Edge>(Edge.class);
		for (Node n : cfg.cfg.vertexSet()){
			ddg.addVertex(n);
		}
		for (Edge e : cfg.cfg.edgeSet()){
			ddg.addEdge(cfg.cfg.getEdgeSource(e), cfg.cfg.getEdgeTarget(e));
		}
		for (DefUsePair d : defUsePair){
			ddg.addEdge(d.getDefNode(), d.getUseNode());
		}
	}
	
	
	public void toDot(String fileName) throws IOException{
		FileWriter f = new FileWriter(fileName);
		DOTExporter<Node, Edge> dot = new DOTExporter<Node, Edge>();
		dot.exportGraph(ddg, f);
	}
	
}
