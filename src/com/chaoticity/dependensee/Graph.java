/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chaoticity.dependensee;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * @author aa496
 */
public class Graph implements Serializable {

    private static final long serialVersionUID = 6222815112980959472L;
    public TreeMap<Integer, Node> nodes;
    public List<Edge> edges;
    public Node root;

    public Graph() {
	nodes = new TreeMap<Integer, Node>();
	edges = new ArrayList<Edge>();
    }

    Graph(ArrayList<TaggedWord> t) {
	this();
	int i = 1;
	for (TaggedWord taggedWord : t) {
	    addNode(taggedWord.word() + "-" + (i++), taggedWord.tag());
	}
    }

    //TODO: fix assumption of all nodes being created before calling this function 
    public Edge addEdge(int sourceIndex, int targetIndex, String label) {
	if (sourceIndex == -1) {
	    root = nodes.get(targetIndex);
	    return null;
	}
	Edge e = new Edge();
	e.source = nodes.get(sourceIndex);
	e.target = nodes.get(targetIndex);
	e.label = label;
	e.sourceIndex = sourceIndex;
	e.targetIndex = targetIndex;
	edges.add(e);
	e.target.parent = e.source;
	e.source.addChild(e.target);
	e.source.outEdges.add(e);
	return e;
    }

    public Node addNode(String label, String pos) {
	for (Node node : nodes.values()) {
	    if (node.label.equals(label)) {
		return node;
	    }
	}
	Node n = new Node(label, pos);
	nodes.put(n.idx - 1, n);
	return n;
    }

    public Node findNode(int i) {
	return nodes.get(i);
    }

    void setRoot(String label) throws Exception {
	for (Node node : nodes.values()) {
	    if (node.label.equals(label)) {
		root = node;
		return;
	    }
	}
	throw new Exception("root not found! " + label);
    }

    public StringBuilder recurse(StringBuilder b) {
	recurse(root, b);
	return b;
    }

    private void recurse(Node t, StringBuilder b) {
	b.append("(");
	b.append(t.lex + "/" + t.pos);
	for (Node child : t.children) {
	    if (!b.toString().contains(child.label)) {
		recurse(child, b);
	    }
	}
	b.append(")");
    }

    public List<Node> getNodeList() {
	List<Node> list = new ArrayList<Node>();
	getNodeList(root, list);
	return list;
    }

    private void getNodeList(Node node, List<Node> list) {
	list.add(node);
	for (Node child : node.children) {
	    if (!list.contains(child)) {
		getNodeList(child, list);
	    }
	}
    }

    @Override
    public String toString() {
	StringBuilder s = new StringBuilder();

	for (Integer i : nodes.keySet()) {
	    s.append(nodes.get(i).lex);
	    s.append(" ");
	}
	return s.toString();
    }
    
    public String toDependencyString()
    {
	StringBuilder s = new StringBuilder();
	for (Edge edge : edges) {
	    s.append(edge.label)
		    .append("_")
		    .append(edge.source.lex)
		    .append("_")
		    .append(edge.target.lex)
		    .append(" ");
	}
	return s.toString();
    }

    public String toPOSString() {
	StringBuilder s = new StringBuilder();
	for (Integer i : nodes.keySet()) {
	    s.append(nodes.get(i).lex);
	    s.append("/");
	    s.append(nodes.get(i).pos);
	    s.append(" ");
	}
	return s.toString();
    }

    void addEdge(Node govNode, Node depNode, String rel) {
	int sourceIndex = govNode.idx - 1;
	int targetIndex = depNode.idx - 1;
	addEdge(sourceIndex, targetIndex, rel);
    }
}
