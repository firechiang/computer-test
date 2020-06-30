package com.firecode.computer.test.theoretical.parser;

import java.util.stream.Stream;

import org.junit.Test;

import com.firecode.computer.test.theoretical.lexer.Lexer;
import com.firecode.computer.test.theoretical.lexer.LexicalException;
import com.firecode.computer.test.theoretical.parser.ast.ASTNode;
import com.firecode.computer.test.theoretical.parser.ast.Expr;
import com.firecode.computer.test.theoretical.parser.ast.Scalar;
import com.firecode.computer.test.theoretical.parser.util.ParseException;
import com.firecode.computer.test.theoretical.parser.util.PeekTokenIterator;

import junit.framework.Assert;

public class SimpleParserTests {
	
    @Test
    public void test() throws LexicalException, ParseException{
        Stream<Character> source = "1+2+3+4".chars().mapToObj(x->(char)x);
        Lexer lexer = new Lexer();
        PeekTokenIterator it = new PeekTokenIterator(lexer.analyse(source).stream());
        ASTNode expr = SimpleParser.parse(it);

        Assert.assertEquals(2, expr.getChildren().size());
        Scalar v1 = (Scalar)expr.getChild(0);
        Assert.assertEquals("1", v1.getLexeme().getValue());
        Assert.assertEquals("+", expr.getLexeme().getValue());

        Expr e2 = (Expr)expr.getChild(1);
        Scalar v2 = (Scalar)e2.getChild(0);
        Assert.assertEquals("2", v2.getLexeme().getValue());
        Assert.assertEquals("+", e2.getLexeme().getValue());

        Expr e3 = (Expr)e2.getChild(1);
        Scalar v3 = (Scalar)e3.getChild(0);
        Assert.assertEquals("3", v3.getLexeme().getValue());
        Assert.assertEquals("+", e3.getLexeme().getValue());

        Scalar v4 = (Scalar)e3.getChild(1);
        Assert.assertEquals("4", v4.getLexeme().getValue());
        expr.print(0);
    }
}
