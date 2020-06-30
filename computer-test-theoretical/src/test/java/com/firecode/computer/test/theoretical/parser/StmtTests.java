package com.firecode.computer.test.theoretical.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.junit.Test;

import com.firecode.computer.test.theoretical.lexer.Lexer;
import com.firecode.computer.test.theoretical.lexer.LexicalException;
import com.firecode.computer.test.theoretical.lexer.Token;
import com.firecode.computer.test.theoretical.parser.ast.ASTNode;
import com.firecode.computer.test.theoretical.parser.ast.AssignStmt;
import com.firecode.computer.test.theoretical.parser.ast.Block;
import com.firecode.computer.test.theoretical.parser.ast.DeclareStmt;
import com.firecode.computer.test.theoretical.parser.ast.FunctionDeclareStmt;
import com.firecode.computer.test.theoretical.parser.ast.IfStmt;
import com.firecode.computer.test.theoretical.parser.ast.ReturnStmt;
import com.firecode.computer.test.theoretical.parser.ast.Stmt;
import com.firecode.computer.test.theoretical.parser.ast.Variable;
import com.firecode.computer.test.theoretical.parser.util.ParseException;
import com.firecode.computer.test.theoretical.parser.util.ParserUtils;
import com.firecode.computer.test.theoretical.parser.util.PeekTokenIterator;

import junit.framework.Assert;

/**
 * 验证表达式解析
 */
public class StmtTests {

    @Test
    public void declare() throws LexicalException, ParseException {
    	PeekTokenIterator it = createTokenIt("var i = 100 * 2");
    	ASTNode stmt = DeclareStmt.parse(it);
        Assert.assertEquals(ParserUtils.toPostfixExpression(stmt), "i 100 2 * =");
    }

    @Test
    public void assign() throws LexicalException, ParseException {
    	PeekTokenIterator it = createTokenIt("i = 100 * 2");
    	ASTNode stmt = AssignStmt.parse( it);
        Assert.assertEquals(ParserUtils.toPostfixExpression(stmt), "i 100 2 * =");
    }

    @Test
    public void ifstmt() throws LexicalException, ParseException {
    	PeekTokenIterator it = createTokenIt("if(a){\n" +
                "a = 1\n" +
                "}"
        );

    	ASTNode stmt = (IfStmt)IfStmt.parse(it);
    	Variable expr = (Variable)stmt.getChild(0);
        Block block = (Block)stmt.getChild(1);
        AssignStmt assignStmt = (AssignStmt)block.getChild(0);

        Assert.assertEquals("a", expr.getLexeme().getValue());
        Assert.assertEquals("=", assignStmt.getLexeme().getValue());
    }

    @Test
    public void ifElseStmt() throws LexicalException, ParseException {
    	PeekTokenIterator it = createTokenIt("if(a) {\n" +
                "a = 1\n" +
                "} else {\n" +
                "a = 2\n" +
                "a = a * 3" +
                "}"
        );
    	IfStmt stmt = (IfStmt)IfStmt.parse( it);
    	Variable expr = (Variable)stmt.getChild(0);
    	Block block = (Block)stmt.getChild(1);
    	AssignStmt assignStmt = (AssignStmt)block.getChild(0);
    	Block elseBlock = (Block)stmt.getChild(2);
    	AssignStmt assignStmt2 = (AssignStmt)elseBlock.getChild(0);

        Assert.assertEquals("a", expr.getLexeme().getValue());
        Assert.assertEquals("=", assignStmt.getLexeme().getValue());
        Assert.assertEquals("=", assignStmt2.getLexeme().getValue());
        Assert.assertEquals(2, elseBlock.getChildren().size());
    }

    @Test
    public void function() throws FileNotFoundException, UnsupportedEncodingException, LexicalException, ParseException {
    	String dir = System.getProperty("user.dir") + File.separator + "example" + File.separator + "function.ts";
    	ArrayList<Token> tokens = Lexer.fromFile(dir);
    	FunctionDeclareStmt functionStmt = (FunctionDeclareStmt)Stmt.parseStmt( new PeekTokenIterator(tokens.stream()));

    	ASTNode args = functionStmt.getArgs();
        Assert.assertEquals("a", args.getChild(0).getLexeme().getValue());
        Assert.assertEquals("b", args.getChild(1).getLexeme().getValue());

        String type = functionStmt.getFuncType();
        Assert.assertEquals("int", type);

        Variable functionVariable = functionStmt.getFunctionVariable();
        Assert.assertEquals("add", functionVariable.getLexeme().getValue());

        Block block = functionStmt.getBlock();
        Assert.assertEquals(true, block.getChild(0) instanceof ReturnStmt);

    }

    @Test
    public void function1() throws FileNotFoundException, UnsupportedEncodingException, LexicalException, ParseException {
    	String dir = System.getProperty("user.dir") + File.separator + "example" + File.separator + "recursion.ts";
    	ArrayList<Token> tokens = Lexer.fromFile(dir);
    	FunctionDeclareStmt functionStmt = (FunctionDeclareStmt)Stmt.parseStmt( new PeekTokenIterator(tokens.stream()));

        Assert.assertEquals("func fact args block", ParserUtils.toBFSString(functionStmt, 4));
        Assert.assertEquals("args n", ParserUtils.toBFSString(functionStmt.getArgs(), 2));
        Assert.assertEquals("block if return", ParserUtils.toBFSString(functionStmt.getBlock(), 3));

    }




    private PeekTokenIterator createTokenIt(String src) throws LexicalException, ParseException {
    	Lexer lexer = new Lexer();
    	ArrayList<Token> tokens = lexer.analyse(src.chars().mapToObj(x ->(char)x));
    	PeekTokenIterator tokenIt = new PeekTokenIterator(tokens.stream());
        return tokenIt;
    }
}
