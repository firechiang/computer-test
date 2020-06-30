package com.firecode.computer.test.theoretical.translator;

import org.junit.Assert;
import org.junit.Test;

import com.firecode.computer.test.theoretical.lexer.LexicalException;
import com.firecode.computer.test.theoretical.parser.util.ParseException;
import com.firecode.computer.test.theoretical.parser.Parser;
import com.firecode.computer.test.theoretical.parser.ast.ASTNode;

public class BlockTest {

    @Test
    public void test() throws LexicalException, ParseException {

        String source = "var a = 1\n" +
                "{\n" +
                "var b = a * 100\n" +
                "}\n" +
                "{\n" +
                "var b = a * 100\n" +
                "}\n";

        ASTNode ast = Parser.parse(source);

        Translator translator = new Translator();

        TAProgram program = translator.translate(ast);


        Assert.assertEquals("a = 1\n" +
                "p1 = a * 100\n" +
                "b = p1\n" +
                "p1 = a * 100\n" +
                "b = p1", program.toString());

    }
}
