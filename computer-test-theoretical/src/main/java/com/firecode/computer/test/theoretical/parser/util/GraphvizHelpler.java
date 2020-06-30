package com.firecode.computer.test.theoretical.parser.util;


import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.firecode.computer.test.theoretical.parser.ast.ASTNode;

public class GraphvizHelpler {

    HashMap<ASTNode, String> nodeLabels = new HashMap<>();
    HashSet<String> edgeSet = new HashSet<>();
    int i = 0;


    private String visNode(ASTNode node) {
        nodeLabels.put(node, "v" + ++i);
        return String.format("%s[label=\"%s\"]\n",
                "v"+i,
                node.getLabel()
        );
    }

    private String visEdge(ASTNode a, ASTNode b) {
        String edgeStr = String.format("\"%s\" -> \"%s\"\n",
                nodeLabels.get(a),
                nodeLabels.get(b)
        );
        if(!this.edgeSet.contains(edgeStr)) {
            edgeSet.add(edgeStr);
            return edgeStr;
        }
        return "";
    }

    public String toDot(ASTNode root) {
    	LinkedList<ASTNode> queue = new LinkedList<ASTNode>();
        Set<String> edges = new HashSet<String>();
        queue.add(root);
        String str = "";
        while(queue.size() > 0) {
        	ASTNode node = queue.poll();
            if(!nodeLabels.containsKey(node)) {
                str += visNode(node) ;
            }
            for(ASTNode child:node.getChildren()) {
                if(!nodeLabels.containsKey(child)) {
                    str += visNode(child);
                }

                str += visEdge(node, child);
                queue.add(child);
            }
        }
        return str;
    }

}
