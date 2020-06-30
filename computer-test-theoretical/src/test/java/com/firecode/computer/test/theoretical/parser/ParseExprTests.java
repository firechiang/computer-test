package com.firecode.computer.test.theoretical.parser;

import java.util.ArrayList;

import org.junit.Test;

import com.firecode.computer.test.theoretical.lexer.Lexer;
import com.firecode.computer.test.theoretical.lexer.LexicalException;
import com.firecode.computer.test.theoretical.lexer.Token;
import com.firecode.computer.test.theoretical.parser.ast.ASTNode;
import com.firecode.computer.test.theoretical.parser.ast.Expr;
import com.firecode.computer.test.theoretical.parser.util.ParseException;
import com.firecode.computer.test.theoretical.parser.util.ParserUtils;
import com.firecode.computer.test.theoretical.parser.util.PeekTokenIterator;

import junit.framework.Assert;
/**
 * 测试遍历运算表达式语法树（就是测试 Expr 类）
 */
public class ParseExprTests {

    @Test
    public void simple() throws LexicalException, ParseException {
    	ASTNode expr = createExpr("1+1+1");
        Assert.assertEquals("1 1 1 + +", ParserUtils.toPostfixExpression(expr));
    }

    @Test
    public void simple1() throws LexicalException, ParseException {
    	ASTNode expr = createExpr("\"1\" == \"\"");
        Assert.assertEquals("\"1\" \"\" ==", ParserUtils.toPostfixExpression(expr));
    }

    @Test
    public void complex() throws LexicalException, ParseException {
    	ASTNode expr1 = createExpr("1+2*3");
    	ASTNode expr2 = createExpr("1*2+3");
    	ASTNode expr3 = createExpr("10 * (7 + 4)");
    	ASTNode expr4 = createExpr("(1*2!=7)==3!=4*5+6");

        Assert.assertEquals("1 2 3 * +", ParserUtils.toPostfixExpression(expr1));
        Assert.assertEquals("1 2 * 3 +", ParserUtils.toPostfixExpression(expr2));
        Assert.assertEquals("10 7 4 + *", ParserUtils.toPostfixExpression(expr3));
        Assert.assertEquals("1 2 * 7 != 3 4 5 * 6 + != ==", ParserUtils.toPostfixExpression(expr4));
        // i++ ++i
    }

    @Test
    public void unary() throws LexicalException, ParseException {
    	ASTNode expr1 = createExpr("1 + ++i");
        expr1.print(0);
        ASTNode expr2 = createExpr("1 + i++");
        expr2.print(0);
        ASTNode expr3 = createExpr("!(a+b+c)");
        expr3.print(0);

    }

    @Test
    public void functionCall() throws LexicalException, ParseException {
    	ASTNode expr1 = createExpr("print(a)");
    	ASTNode expr2 = createExpr("print(a,b,c)");
        System.out.println(ParserUtils.toPostfixExpression(expr2));
        Assert.assertEquals("print a", ParserUtils.toPostfixExpression(expr1));
        Assert.assertEquals("print a b c", ParserUtils.toPostfixExpression(expr2));
    }


    private ASTNode createExpr(String src) throws LexicalException, ParseException {
    	Lexer lexer = new Lexer();
    	ArrayList<Token> tokens = lexer.analyse(src.chars().mapToObj(x ->(char)x));
    	PeekTokenIterator tokenIt = new PeekTokenIterator(tokens.stream());
        return Expr.parse( tokenIt);
    }
}
