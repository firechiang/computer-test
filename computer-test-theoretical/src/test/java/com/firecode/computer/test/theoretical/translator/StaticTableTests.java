package com.firecode.computer.test.theoretical.translator;

import org.junit.Assert;
import org.junit.Test;

import com.firecode.computer.test.theoretical.lexer.LexicalException;
import com.firecode.computer.test.theoretical.parser.Parser;
import com.firecode.computer.test.theoretical.parser.ast.ASTNode;
import com.firecode.computer.test.theoretical.parser.util.ParseException;

public class StaticTableTests {


    @Test
    public void staticTest() throws LexicalException, ParseException {
        String source = "if(a) { a = 1 } else { b = a + 1 * 5 }";
        ASTNode astNode = Parser.parse(source);
        Translator translator = new Translator();
        TAProgram program =  translator.translate(astNode);
        Assert.assertEquals(2, program.getStaticSymbolTable().size());
    }
}
