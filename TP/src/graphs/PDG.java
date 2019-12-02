package graphs;

import org.jgrapht.graph.DirectedMultigraph;

public class PDG {

	
	DirectedMultigraph<Node, Edge> pdg;
	
	
	/**
	 * Constructs the ProgramDependenceGraph. Keeps olds edges.
	 * For better visualization it should have distinction between Edge types (future improve).
	 * @param ddg
	 * @param cdg
	 */
	public PDG(DDG ddg, CDG cdg) {
	    pdg = new DirectedMultigraph<Node, Edge>(Edge.class);
	    for (Node node : cdg.cdg.vertexSet()) {
	      pdg.addVertex(node);
	    }
	    for (Edge edge : cdg.cdg.edgeSet()) {
	      pdg.addEdge(cdg.cdg.getEdgeSource(edge), cdg.cdg.getEdgeTarget(edge), edge);
	    }
	    for (Edge edge : ddg.ddg.edgeSet()) {
	      pdg.addEdge(ddg.ddg.getEdgeSource(edge), ddg.ddg.getEdgeTarget(edge), edge);
	    }
	  }
	
}
