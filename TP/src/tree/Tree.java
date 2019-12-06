package tree;

import java.util.LinkedList;
import java.util.List;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import ast.Statement;
import graphs.Node;


public class Tree<E> {

  private Node root;
  private DefaultDirectedGraph<Node, DefaultEdge> t;
  private int size = 0;
  private List<Node> markedNodes;


  public Tree() {
    root = null;
    t = new DefaultDirectedGraph<Node, DefaultEdge>(DefaultEdge.class);
    markedNodes = new LinkedList<>();
  }


  public Tree(Node root) {
    this.root = root;
    size++;
    t = new DefaultDirectedGraph<Node, DefaultEdge>(DefaultEdge.class);
    t.addVertex(this.root);
    markedNodes = new LinkedList<Node>();
  }

  public boolean contains(E value) {
    for (Node node : t.vertexSet()) {
      if (node.getStmt().equals(value))
        return true;
    }
    return false;
  }

  private Node getNode(Statement value) {
    for (Node node : t.vertexSet()) {
      if (node.getStmt().equals(value))
        return node;
    }
    return null;
  }

  public void addChild(Node node1, Node node2) {
	    if (node1 == null) {
	      node1 = new Node();
	      t.addVertex(node1);
	      size++;
	    }
	    if (node2 == null) {
	      node2 = new Node();
	      t.addVertex(node2);
	      size++;
	    }
	    t.addVertex(node1);
	    t.addVertex(node2);
	    node2.setParent(node1);
	    t.addEdge(node1, node2);
	  }


  public int size() {
    return size;
  }


  public boolean isAncestor(Node n1, Node n2) {
    Node curr = n2;
    while (curr != null || curr == root) {
      if (curr.equals(n1))
        return true;
      curr = curr.getParent();
    }
    return false;
  }
 
  public Node leastCommonAncestor(Node n1, Node n2) {
    Node curr = n2;
    while (curr != null) {
      if (isAncestor(curr, n1))
        return curr;
      curr = curr.getParent();
    }
    return null;
  }


  public void addMark(Node n) {
    if (n != null) {
      markedNodes.add(n);
    }
  }


  public List<Node> getMarkedNodes() {
    return markedNodes;
  }

  public void clearMarked() {
    markedNodes.clear();
  }

  public void markPath(Node in, Node end) {
    Node curr = in;
    while (curr != null && !curr.equals(end)) {
      addMark(curr);
      curr = curr.getParent();
    }
  }  
}
  

