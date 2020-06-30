package com.firecode.computer.test.theoretical.translator;


import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.junit.Test;

import com.firecode.computer.test.theoretical.lexer.LexicalException;
import com.firecode.computer.test.theoretical.parser.util.ParseException;
import com.firecode.computer.test.theoretical.parser.Parser;
import com.firecode.computer.test.theoretical.parser.ast.ASTNode;


public class TransFunctionTests {

    @Test
    public void testSimpleFunction() throws FileNotFoundException, ParseException, LexicalException, UnsupportedEncodingException {
    	ASTNode astNode = Parser.fromFile("./example/function.ts");
    	Translator translator = new Translator();
    	TAProgram program = translator.translate(astNode);
        String expect = "L0:\n" +
                "FUNC_BEGIN\n" +
                "p1 = a + b\n" +
                "RETURN p1";
        Assert.assertEquals(expect, program.toString());

    }

    @Test
    public void testRecursiveFunction() throws FileNotFoundException, ParseException, LexicalException, UnsupportedEncodingException {
    	ASTNode astNode = Parser.fromFile("./example/recursion.ts");
    	Translator translator = new Translator();
    	TAProgram program = translator.translate(astNode);
        System.out.println(program.toString());

        String expect = "L0:\n" +
                "FUNC_BEGIN\n" +
                "p1 = n == 0\n" +
                "IF p1 ELSE L1\n" +
                "RETURN 1\n" +
                "L1:\n" +
                "p2 = n - 1\n" +
                "PARAM p2 6\n" +
                "SP -5\n" +
                "CALL L0\n" +
                "SP 5\n" +
                "p4 = p3 * n\n" +
                "RETURN p4";

        Assert.assertEquals(expect, program.toString());
    }
}
