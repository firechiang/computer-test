package com.firecode.computer.test.theoretical.parser.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.firecode.computer.test.theoretical.parser.ast.ASTNode;
import com.firecode.computer.test.theoretical.parser.ast.Factor;

/**
 * 遍历语法树是各个节点（测试类：ParseExprTests）
 */
public class ParserUtils {
    // Prefix
    // Postfix
    public static String toPostfixExpression(ASTNode node){

        if(node instanceof Factor) {
            return node.getLexeme().getValue();
        }

        List<String> prts = new ArrayList<String> ();
        for(ASTNode child : node.getChildren()) {
            prts.add(toPostfixExpression(child));
        }
        String lexemeStr = node.getLexeme() != null ? node.getLexeme().getValue() : "";
        if(lexemeStr.length() > 0) {
            return StringUtils.join(prts, " ") + " " + lexemeStr;
        } else {
            return StringUtils.join(prts, " ");
        }
    }

    public static String toBFSString(ASTNode root, int max) {

        LinkedList<ASTNode> queue = new LinkedList<ASTNode>();
        List<String> list = new ArrayList<String>();
        queue.add(root);

        int c = 0;
        while(queue.size() > 0 && c++ < max) {
            ASTNode node = queue.poll();
            list.add(node.getLabel());
            for(ASTNode child : node.getChildren()) {
                queue.add(child);
            }
        }
        return StringUtils.join(list, " ");
    }
}
