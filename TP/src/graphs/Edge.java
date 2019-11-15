package graphs;

import org.jgraph.graph.DefaultEdge;

public class Edge extends DefaultEdge {

	boolean kind;
	
	public Edge(){}
	
	public Edge(boolean value){
		kind = value;
	}
	
}
