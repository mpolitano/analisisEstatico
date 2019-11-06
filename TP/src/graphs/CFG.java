package graphs;

import java.util.LinkedList;
import java.util.List;

import org.jgrapht.graph.DefaultDirectedGraph;

public class CFG {

	DefaultDirectedGraph<Node,Edge> cfg;
	
	String programName;
	
	Node inNode;
	
	List<Node> outNodes;
	
	public CFG (){
		this.cfg = new DefaultDirectedGraph<Node, Edge>(Edge.class);
		this.outNodes = new LinkedList<Node>();
	}

	public DefaultDirectedGraph<Node, Edge> getGraph(){
		return cfg;
	}
	
	public void setCFG(DefaultDirectedGraph<Node, Edge> graph){
		cfg = graph;
	}
	
	public void addNode(Node node){
		cfg.addVertex(node);
	}
	
	public void addEdge(Node node1, Node node2){
		cfg.addEdge(node1, node2);
	}
	
	public void setName(String name){
		programName = name;
	}
	
	public void setInNode(Node n){
		inNode = n;
	}
	
	public void addOutNode(Node n){
		this.outNodes.add(n);
	}
	
	public Node getInNode(){
		return this.inNode;
	}
	
	public List<Node> getOutNodes(){
		return this.outNodes;
	}
	
	public void addAllNodes(CFG graph){
		for (Node n : graph.getGraph().vertexSet()){
			addNode(n);
		}
	}
	
	public void addAllEdges(CFG graph){
		for (Edge e : graph.getGraph().edgeSet()){
			Node n1 = graph.getGraph().getEdgeSource(e);
			Node n2 = graph.getGraph().getEdgeTarget(e);
			if (!cfg.containsVertex(n1)){
				addNode(n1);
			}
			if (!cfg.containsVertex(n2)){
				addNode(n2);
			}
			cfg.addEdge(n1, n2, e);
		}
	}
	
	public void concatNodeAndGraph(Node condition, CFG graph){
		graph.addNode(condition);
		graph.addEdge(condition, graph.getInNode());
		graph.setInNode(condition);
	}
	
	public void concatGraph(CFG graph){
		addAllNodes(graph);
		addAllEdges(graph);
		for (Node n : getOutNodes()){
			cfg.addEdge(n, graph.getInNode(), new Edge());
		}
		for (Node n : graph.getOutNodes()){
			addOutNode(n);
		}
	}
	
}
