package com.firecode.computer.test.theoretical.translator;


import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.junit.Test;

import com.firecode.computer.test.theoretical.lexer.LexicalException;
import com.firecode.computer.test.theoretical.parser.Parser;
import com.firecode.computer.test.theoretical.parser.ast.ASTNode;
import com.firecode.computer.test.theoretical.parser.util.ParseException;

public class TransIfStmtTests {
	
    @Test
    public void testIfStmt() throws LexicalException, ParseException {

        String source = "if(a){" +
                "b = 1" +
                "}";
        ASTNode astNode = Parser.parse(source);
        Translator translator = new Translator();

        TAProgram program = translator.translate(astNode);
        String expected = "IF a ELSE L0\n" +
                "b = 1\n" +
                "L0:";
        Assert.assertEquals(expected, program.toString());
    }

    @Test
    public void testIfElseStmt() throws LexicalException, ParseException {

        String source = "if(a){\n" +
                "b = 1\n" +
                "} else {\n" +
                "b=2\n" +
                "}\n";
        ASTNode astNode = Parser.parse(source);
        Translator translator = new Translator();

        TAProgram program = translator.translate(astNode);
        String expected = "IF a ELSE L0\n" +
                "b = 1\n" +
                "GOTO L1\n" +
                "L0:\n" +
                "b = 2\n" +
                "L1:";
        Assert.assertEquals(expected, program.toString());
    }

    @Test
    public void testIfElseIf() throws FileNotFoundException, ParseException, LexicalException, UnsupportedEncodingException {
    	ASTNode astNode = Parser.fromFile("./example/complex-if.ts");
    	Translator translator = new Translator();
    	TAProgram program = translator.translate(astNode);
        System.out.println(program.toString());

        String expectd = "p0 = a == 1\n" +
                "IF p0 ELSE L0\n" +
                "b = 100\n" +
                "GOTO L5\n" +
                "L0:\n" +
                "p1 = a == 2\n" +
                "IF p1 ELSE L1\n" +
                "b = 500\n" +
                "GOTO L4\n" +
                "L1:\n" +
                "p2 = a == 3\n" +
                "IF p2 ELSE L2\n" +
                "p1 = a * 1000\n" +
                "b = p1\n" +
                "GOTO L3\n" +
                "L2:\n" +
                "b = -1\n" +
                "L3:\n" +
                "L4:\n" +
                "L5:";

        Assert.assertEquals(expectd, program.toString());
    }
}
