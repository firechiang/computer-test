package com.firecode.computer.test.theoretical.parser;

import com.firecode.computer.test.theoretical.parser.ast.ASTNode;
import com.firecode.computer.test.theoretical.parser.ast.ASTNodeTypes;
import com.firecode.computer.test.theoretical.parser.ast.Expr;
import com.firecode.computer.test.theoretical.parser.ast.Factor;
import com.firecode.computer.test.theoretical.parser.util.ParseException;
import com.firecode.computer.test.theoretical.parser.util.PeekTokenIterator;

/**
 * 解析  1 + 2 + 3 + 4 将其转换为一个语法树如下（注意：+号，永远是父节点。以及右边的节点永远是+号，除了最后一个节点）：
 * 
 *    +
 * 1     +
 *    2     +
 *       3     4
 *
 */
public class SimpleParser {
    // Expr -> digit + Expr | digit
    // digit -> 0|1|2|3|4|5|...|9
    public static ASTNode parse(PeekTokenIterator it) throws ParseException {

    	Expr expr = new Expr();
    	ASTNode scalar = Factor.parse(it);
        // base condition
        if(!it.hasNext()){
           return scalar;
        }

        expr.setLexeme(it.peek());
        it.nextMatch("+");
        expr.setLabel("+");
        expr.addChild(scalar);
        expr.setType(ASTNodeTypes.BINARY_EXPR);
        ASTNode rightNode = parse(it);
        expr.addChild(rightNode);
        return expr;
    }
}
