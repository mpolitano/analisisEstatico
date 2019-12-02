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

import ast.AssignStmt;
import ast.IfStmt;
import ast.Program;
import ast.ReturnStmt;
import ast.Statement;
import ast.WhileStmt;

public class CFG {

	//the graph
	DefaultDirectedGraph<Node,Edge> cfg;
	
	String programName;
	
	Program program;
	
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
	
	public void setProgram(Program p){
		this.program = p;
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
	
	/**
	 * The set of predecessors nodes of n in a graph
	 * @param n
	 * @return
	 */
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
	public CFG postDomTree(){
		CFG reverse = reverse();
		Map<Node, Set<Node>> postDominators = postDom();
		CFG tree = new CFG(); 
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
							tree.addNode(node);
							tree.addNode(n);
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
		
		augmentedCFG.cfg.addEdge(entry, this.inNode, new Edge());
		
		Node fin = new Node();
		fin.markAsEntry();
		augmentedCFG.cfg.addVertex(fin);
		augmentedCFG.cfg.addEdge(start, fin, new Edge(false));
		
		for (Edge e : cfg.edgeSet()){
			augmentedCFG.cfg.addEdge(cfg.getEdgeSource(e), cfg.getEdgeTarget(e), new Edge());
		}
		
		for (Node n : outNodes){
			augmentedCFG.cfg.addEdge(n, fin, new Edge());
		}
		
		augmentedCFG.addOutNode(fin);
		
		return augmentedCFG;
	}

	
	public void computeGenAndKill(){
		for (Node n : cfg.vertexSet()){
			n.computeGen();
			n.computeKill(program.getMethods());
		}
	}
	
	/**
	 * 
	 * for each node n do OUT[n] = GEN[n] endfor
	 * change = true
	 * while change do
	 * 	change = false
	 *  for each node n do 
	 *  	IN[n] = U OUT[P] P = inmediate preds of n
	 *  	OLDUT = OUT[n]
	 *  	OUT[n] = GEN[n] U (IN[n]-KILL[n])
	 *  	if OUT[n] != OLDOUT then change = true endif
	 *  endfor
	 * endwhile 
	 * 
	 */
	public void reachingDefs(){
		for (Node n : cfg.vertexSet()){
			n.setOut(new HashSet<Def>(n.getGen()));
			//since OUT[n] == GEN[n] page 5
		}
		boolean change = true;
		while (change){
			change = false;
			for (Node n : cfg.vertexSet()){
				Set<Node> pred = predecessors(n);
				Set<Def> in = new HashSet<Def>();
				for (Node p : pred){
					in.addAll(new HashSet<Def>(p.getOut()));
				}
				n.setIn(in);
				Set<Def> oldOuts = n.getOut();
				Set<Def> finalOuts = new HashSet<Def>(n.getIn());
				finalOuts.removeAll(n.getKill());
				finalOuts.addAll(n.getGen());
				n.setOut(finalOuts);
				if (!finalOuts.equals(oldOuts)){
					//some changes
					change = true;
				}
			}
		}
	}
	
	public Set<Statement> getUpExpUses(Node n){
		HashSet<Statement> upExpUses = new HashSet<Statement>();
		for (Def d : n.getIn()){
			AssignStmt s = (AssignStmt) d.getStmt();
			for (Node node : predecessors(n)){
				if(use(node.stmt, s.getID())){
					upExpUses.add(node.stmt);
				}
			}
		}
		return upExpUses;
	}
	
	
	private boolean use(Statement s, String var){
		if (s instanceof AssignStmt) {
			return s.toString().contains(var) && !((AssignStmt)s).getID().contains(var);
		} else 
			if (s instanceof IfStmt){
				return ((IfStmt) s).getCondition().toString().contains(var);
			} else
				if (s instanceof WhileStmt){
					return ((WhileStmt) s).getCondition().toString().contains(var);
				} else
					if (s instanceof ReturnStmt){
						return s.toString().contains(var);
					}
		return false;
	}
	
	private boolean defUseVariable(Def d, Statement s){
		if (d.getStmt() instanceof AssignStmt) {
			String stmtVar = ((AssignStmt) d.getStmt()).getID();
			if (s instanceof AssignStmt){
				return !((AssignStmt)s).getID().equals(stmtVar) && ((AssignStmt)s).getExpression().toString().contains(stmtVar);
			} else 
				if (s instanceof IfStmt) {
					return ((IfStmt) s).getCondition().toString().equals(stmtVar);
		      } else 
		    	  if (s instanceof WhileStmt) {
		    		  return ((WhileStmt) s).getCondition().toString().equals(stmtVar);
		      } else 
		    	  if (s instanceof ReturnStmt) {
		    		  return ((ReturnStmt) s).getExpression().toString().contains(stmtVar);
		    	  }
		}
		return false;
	}
	
	public Set<DefUsePair> defUsePairs(){
		HashSet<DefUsePair> result = new HashSet<DefUsePair>();
		for (Node n : cfg.vertexSet()){
			Set<Statement> upExposed = getUpExpUses(n);
			for (Statement usedStmt : upExposed){
				for (Def d : n.getIn()){
					if(defUseVariable(d, usedStmt))
						result.add(new DefUsePair(d.getStmt(),d.getNode(), usedStmt, n));
				}
			}
		}
		return result;
	}
	
	
	public CFG getSubGraph(Node start, Node end) {
		// start can't be a join node neither end a split node
//		assert((start.nodeType!=CFGNodeTypes.JOIN) && (end.nodeType!=CFGNodeTypes.SPLIT));
		CFG result = new CFG(); 
		// build and collect all nodes and edges of the sub graph 
		HashSet<Node> nodes = new HashSet<Node>();
		HashSet<Edge> edges = new HashSet<Edge>();
		// queue of nodes of the subgraph to analyze
		LinkedList <Node> todo = new LinkedList<Node>();
		todo.add(start);
		//while the "end" node hasn't reached get successors nodes and regarding edges recursively
		while (!todo.isEmpty()) {
			Node node = todo.poll(); 
			//add this node
			nodes.add(node);
			for (Edge e : getGraph().edgesOf(node)) {
				if((getGraph().getEdgeSource(e)==node) && (node !=end)) {
					//add outgoing edges 
					edges.add(e);
					//if the successor is not the end and not yet collected, add to process
					Node succNode = getGraph().getEdgeTarget(e);
					if (!result.cfg.vertexSet().contains(succNode) && succNode != end) {
						todo.add(succNode);
					}
				}
			}	
		}
		//add the end node
		result.addNode(end);
		// ---- generate the subgraph ---- (in second phase because edges can't be added if the corresponding node don't exists)
		//put the nodes 
		for(Node n:nodes) result.addNode(n);
		//put  edges
		for(Edge e:edges) result.addEdge(getGraph().getEdgeSource(e),getGraph().getEdgeTarget(e));
	    result.setInNode(start);
	    result.addOutNode(end);
	     Set<Node> b = result.getGraph().vertexSet();
	    // return the instantiated graph
		return result;	
	}
	
	public void toDot(String fileName) throws IOException{
		FileWriter f = new FileWriter(fileName);
		DOTExporter<Node, Edge> dot = new DOTExporter<Node, Edge>();
		dot.exportGraph(cfg, f);
	}
	
}
