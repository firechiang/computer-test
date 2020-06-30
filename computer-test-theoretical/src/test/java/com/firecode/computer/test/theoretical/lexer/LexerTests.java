package com.firecode.computer.test.theoretical.lexer;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

public class LexerTests {
	
    void assertToken(Token token, String value, TokenType type){
        Assert.assertEquals(value, token.getValue());
        Assert.assertEquals(type, token.getType());
    }

    @Test
    public void test_expression() throws LexicalException {
    	Lexer lexer = new Lexer();
        String source = "(a+b)^100.12==+100-20";
        ArrayList<Token> tokens = lexer.analyse(source.chars().mapToObj(x -> (char)x));
        Assert.assertEquals(11, tokens.size());
        assertToken(tokens.get(0), "(", TokenType.BRACKET);
        assertToken(tokens.get(1), "a", TokenType.VARIABLE);
        assertToken(tokens.get(2), "+", TokenType.OPERATOR);
        assertToken(tokens.get(3), "b", TokenType.VARIABLE);
        assertToken(tokens.get(4), ")", TokenType.BRACKET);
        assertToken(tokens.get(5), "^", TokenType.OPERATOR);
        assertToken(tokens.get(6), "100.12", TokenType.FLOAT);
        assertToken(tokens.get(7), "==", TokenType.OPERATOR);
        assertToken(tokens.get(8), "+100", TokenType.INTEGER);
        assertToken(tokens.get(9), "-", TokenType.OPERATOR);
        assertToken(tokens.get(10), "20", TokenType.INTEGER);
    }

    @Test
    public void test_function() throws LexicalException {
        String source = "func foo(a, b){\n" +
                "print(a+b)\n" +
                "}\n" +
                "foo(-100.0, 100)";
        Lexer lexer = new Lexer();
        ArrayList<Token> tokens = lexer.analyse(source.chars().mapToObj(x -> (char)x));

        assertToken(tokens.get(0), "func", TokenType.KEYWORD);
        assertToken(tokens.get(1), "foo", TokenType.VARIABLE);
        assertToken(tokens.get(2), "(", TokenType.BRACKET);
        assertToken(tokens.get(3), "a", TokenType.VARIABLE);
        assertToken(tokens.get(4), ",", TokenType.OPERATOR);
        assertToken(tokens.get(5), "b", TokenType.VARIABLE);
        assertToken(tokens.get(6), ")", TokenType.BRACKET);
        assertToken(tokens.get(7), "{", TokenType.BRACKET);
        assertToken(tokens.get(8), "print", TokenType.VARIABLE);
        assertToken(tokens.get(9), "(", TokenType.BRACKET);
        assertToken(tokens.get(10), "a", TokenType.VARIABLE);
        assertToken(tokens.get(11), "+", TokenType.OPERATOR);
        assertToken(tokens.get(12), "b", TokenType.VARIABLE);
        assertToken(tokens.get(13), ")", TokenType.BRACKET);
        assertToken(tokens.get(14), "}", TokenType.BRACKET);
        assertToken(tokens.get(15), "foo", TokenType.VARIABLE);
        assertToken(tokens.get(16), "(", TokenType.BRACKET);
        assertToken(tokens.get(17), "-100.0", TokenType.FLOAT);
        assertToken(tokens.get(18), ",", TokenType.OPERATOR);
        assertToken(tokens.get(19), "100", TokenType.INTEGER);
        assertToken(tokens.get(20), ")", TokenType.BRACKET);


    }

    @Test
    public void test_deleteComment() throws LexicalException {
        String source = "/*123123123\n123123123*/a=1";
        Lexer lexer = new Lexer();
        ArrayList<Token> tokens = lexer.analyse(source.chars().mapToObj(x -> (char)x));
        Assert.assertEquals(3, tokens.size());
    }

    @Test
    public void test_deleteErrorComment() throws LexicalException {
        String errorSource="/*/";
        Lexer lexer = new Lexer();
        try{
            List<Token> tokens = lexer.analyse(errorSource.chars().mapToObj(x ->(char)x));
        }catch (Exception e){
            return;
        }

        throw new LexicalException("error");
    }

    @Test
    public void test_deleteOneLine() throws LexicalException {
        String source = "//121212\na=1";
        Lexer lexer = new Lexer();
        ArrayList<Token> tokens = lexer.analyse(source.chars().mapToObj(x -> (char)x));
        Assert.assertEquals(3, tokens.size());
    }

    @Test
    public void minus() throws LexicalException {
        String source = "n-1";
        Lexer lexer = new Lexer();
        ArrayList<Token> tokens = lexer.analyse(source.chars().mapToObj(x -> (char)x));
        Assert.assertEquals(3, tokens.size());
    }

    @Test
    public void test_loadFile() throws FileNotFoundException, UnsupportedEncodingException, LexicalException {
    	String dir = System.getProperty("user.dir") + File.separator + "example" + File.separator + "function.ts";
    	ArrayList<Token> tokens = Lexer.fromFile(dir);
        Assert.assertEquals(16, tokens.size());
    }
}
