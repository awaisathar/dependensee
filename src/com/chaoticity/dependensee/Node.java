/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chaoticity.dependensee;

import edu.stanford.nlp.util.Pair;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author aa496
 */
public class Node implements Serializable {

    private static final long serialVersionUID = -8871645177307943816L;
    public String label;
    public int idx;
    public String lex;
    public String pos;
    public List<Node> children;
    public List<Edge> outEdges;
    public int degree = 1;
    public Node parent;
    public Rectangle2D position = new Rectangle();

    public Node(String lex, int idx, String pos) {
        this.label = lex + "-" + idx;
        this.lex = lex;
        this.idx = idx;
        this.pos = pos;
        children = new ArrayList<Node>();
        outEdges = new ArrayList<Edge>();
    }

    public Node(String label, String pos) {
        this.label = label;
        this.lex = label.substring(0, label.lastIndexOf("-"));
        this.idx = Integer.parseInt(label.substring(label.lastIndexOf("-") + 1));
        this.pos = pos;
        children = new ArrayList<Node>();
        outEdges = new ArrayList<Edge>();
    }

    public void addChild(Node c) {
        for (Node node : children) {
            if (node.label.equalsIgnoreCase(c.label)) {
                return;
            }
        }
        children.add(c);
        degree++;
    }

    @Override
    public String toString() {
        return lex;
    }

    public int getPathLength(Node n) {

        Queue<Pair<Node, Integer>> q = new LinkedList<Pair<Node, Integer>>();
        Set<Node> marked = new HashSet<Node>();
        q.add(new Pair<Node, Integer>(this, 0));
        marked.add(this);
        while (!q.isEmpty()) {
            Pair<Node, Integer> v = q.remove();
            if (v.first == n) {
                return v.second;
            }
            if (v.first.parent != null && !marked.contains(v.first.parent)) {
                q.add(new Pair<Node, Integer>(v.first.parent, v.second + 1));
                marked.add(v.first.parent);
            }
            for (Node node : v.first.children) {
                q.add(new Pair<Node, Integer>(node, v.second + 1));
                marked.add(node);
            }
        }
        return Integer.MAX_VALUE;
    }

    public String getRelationToParent() {
        String rel = null;
        if (parent == null) {
            return null;
        }
        for (Edge e : parent.outEdges ) {
            if (e.target==this) {
                return e.label;
            }
        }
        return null;
    }
    
    public String DFS()
    {
        StringBuilder b = new StringBuilder();
        Set<Node> done = new HashSet<Node>();
        done.add(this);
        DFS(this,done,b);
        return b.toString();
    }
    
    private void DFS(Node node, Set<Node> done, StringBuilder b) {
        for (Edge e : node.outEdges) {
            if ( ("amod".equalsIgnoreCase(e.label) || "advmod".equalsIgnoreCase(e.label))
                    &&  !done.contains(e.target)) {
                DFS(e.target, done,b);
                done.add(e.target);
            }
        }
        //if ("".equals(b.toString()) )
            b.append(" ").append(node.lex).append("/").append(node.pos);
    }
}
