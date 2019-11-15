package graphs;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.ext.DOTExporter;
import org.jgrapht.graph.DefaultDirectedGraph;

public class CFG {

	//the graph
	DefaultDirectedGraph<Node,Edge> cfg;
	
	String programName;
	
	//Start Node
	Node inNode;
	
	//Final Node (it is a list because in the middle of the construction it is useful)
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
	
	public void setOutNode(List<Node> nodes){
		this.outNodes = nodes;
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
		for (Node n : this.getOutNodes()){
			cfg.addEdge(n, graph.getInNode(), new Edge());
		}
		//for (Node n : graph.getOutNodes()){
		//	addOutNode(n);
		//}
		if (this.getInNode() == null) {
			this.setInNode(graph.getInNode());
		}
		this.setOutNode(graph.getOutNodes());
	}
	
	
	/*public void exportToDot(String fileName) {
    try {
        // Defines the vertex id to be displayed in the .gv file
    	IntegerComponentNameProvider<N> vertexId = new IntegerComponentNameProvider<N>(){
            public String getVertexName(N p) {
                return "0";
            }
        };
        // Defines the vertex label to be displayed in the .gv file
        StringComponentNameProvider<N> vertexLabel = new StringComponentNameProvider<N>(){
            
			public String getVertexName(N p) {
                return p.toString();
            }
        };
        // Just us a default edge label
        StringComponentNameProvider<E> edgeLabel = new StringComponentNameProvider<E>();	        
        DOTExporter<N, E> dot = new DOTExporter<N, E>(vertexId, vertexLabel, edgeLabel, null, null);
        dot.exportGraph(cfg, new FileWriter(fileName));
    } catch (IOException e) {
        e.printStackTrace();
    }
}*/
	
	private Set<Node> predecessors (Node n){
		Set<Node> result = new HashSet<Node> ();
		for (Edge e : cfg.edgeSet()){
			if (n.equals(cfg.getEdgeTarget(e))){
				result.add(cfg.getEdgeSource(e));
			}
		}
		return result;
	}
	
	private Set<Node> intersecPred (Node n, Map<Node, Set<Node>> dominators){
		Set<Node> intersection = new HashSet<Node>();
		Set<Node> preds = predecessors(n);
		if (!preds.isEmpty()){
			Node next = preds.iterator().next();
			intersection = new HashSet<Node>(dominators.get(next));
			for (Node preNode : preds){
				intersection.retainAll(dominators.get(preNode));
			}
		}
		return intersection;
	}
	
	public Map<Node, Set<Node>> dominadores(){
		Map<Node, Set<Node>> result = new HashMap<Node, Set<Node>>();
		result.put(inNode, new HashSet<Node>());
		result.get(inNode).add(inNode);
		//primero cargo todos
		for (Node n : cfg.vertexSet()){
			if (!n.equals(inNode)){
				Set<Node> nodes = new HashSet<Node>();
				nodes.addAll(cfg.vertexSet());
				result.put(n,nodes);
			}
		}
		//algoritmo para computar dominadores del paper
		boolean change = true;
		while (change){
			change = false;
			for (Node n : cfg.vertexSet()){
				if (!n.equals(inNode)){
					Set<Node> doms = result.get(n);
					Set<Node> intersec = intersecPred(n, result);
					intersec.add(n);
					if (!doms.equals(intersec)){
						change = true;
						result.put(n, intersec);
					}
				}
			}
		}
		return result;
	}
	
	//reverse of a graph
	public CFG reverse(){
		CFG result = new CFG();
		result.inNode = outNodes.get(0);
		for (Node n : cfg.vertexSet())
			result.cfg.addVertex(n);
		for (Edge e : cfg.edgeSet())
			result.cfg.addEdge(cfg.getEdgeTarget(e), cfg.getEdgeSource(e), new Edge());
		result.addOutNode(inNode);
		return result;
	}
	
	
	public Map<Node, Set<Node>> postDom(){
		CFG reverse = reverse();
		return reverse.dominadores();
	}
	
	/**
	 * Calcula el arbol de postDominadores.
	 * 
	 * ACLARACION: da un arbol (como grafo)
	 * @return
	 */
	public DefaultDirectedGraph<Node, Edge> postDomTree(){
		CFG reverse = reverse();
		Map<Node, Set<Node>> postDominators = postDom();
		DefaultDirectedGraph<Node, Edge> tree = new DefaultDirectedGraph<Node, Edge>(Edge.class); 
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.addLast(reverse.getInNode());
		for (Node n : reverse.cfg.vertexSet()){
			Set<Node> dominators = postDominators.get(n);
			dominators.remove(n);
			postDominators.put(n, dominators);
		}
		while (!queue.isEmpty()){
			Node node = queue.removeFirst();
			for (Node n : reverse.cfg.vertexSet()){
				Set<Node> dominators = postDominators.get(n);
				if (!dominators.isEmpty()){
					if (dominators.contains(node)){
						dominators.remove(node);
						if (dominators.isEmpty()){
							tree.addVertex(node);
							tree.addVertex(n);
							tree.addEdge(node, n);
							queue.addLast(n);
						}
					}
					
				}
			}
		}
		return tree;
	}
	
	//Augment the cfg to compute the Control Dependence Graphs
	public CFG getAugmentedCFG(){
		CFG augmentedCFG = new CFG();
		//start node? distinto?
		Node start = new Node();
		start.markAsStart();
		augmentedCFG.cfg.addVertex(start);
		augmentedCFG.setInNode(start);
		for (Node n : cfg.vertexSet()){
			augmentedCFG.cfg.addVertex(n);
		}
		Node entry = new Node();
		entry.markAsEntry();
		augmentedCFG.cfg.addVertex(entry);
		augmentedCFG.cfg.addEdge(start, entry, new Edge(true));

		Node fin = new Node();
		fin.markAsEntry();
		augmentedCFG.cfg.addVertex(fin);
		augmentedCFG.cfg.addEdge(start, fin, new Edge(false));

		for (Edge e : cfg.edgeSet()){
			augmentedCFG.cfg.addEdge(cfg.getEdgeSource(e), cfg.getEdgeTarget(e), new Edge());
		}
		
		return augmentedCFG;
	}
	
	public void toDot(String fileName) throws IOException{
		FileWriter f = new FileWriter(fileName);
		DOTExporter<Node, Edge> dot = new DOTExporter<Node, Edge>();
		dot.exportGraph(cfg, f);
	}
	
}
