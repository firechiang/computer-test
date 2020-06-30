package com.firecode.computer.test.theoretical.translator;


import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.firecode.computer.test.theoretical.lexer.LexicalException;
import com.firecode.computer.test.theoretical.parser.Parser;
import com.firecode.computer.test.theoretical.parser.ast.ASTNode;
import com.firecode.computer.test.theoretical.parser.util.GraphvizHelpler;
import com.firecode.computer.test.theoretical.parser.util.ParseException;
import com.firecode.computer.test.theoretical.translator.symbol.SymbolTable;


public class TransExprTests {

    void assertOpcodes(String[] lines, ArrayList<TAInstruction> opcodes) {
        for(int i = 0; i < opcodes.size(); i++) {
        	TAInstruction opcode = opcodes.get(i);
            String strVal = opcode.toString();
            Assert.assertEquals(lines[i], strVal);
        }
    }


    @Test
    public void transExpr() throws LexicalException, ParseException {
        String source = "a+(b-c)+d*(b-c)*2";
        ASTNode p = Parser.parse(source);
        ASTNode exprNode = p.getChild(0);


        Translator translator = new Translator();
        SymbolTable symbolTable = new SymbolTable();
        TAProgram program = new TAProgram();
        translator.translateExpr(program, exprNode, symbolTable);

        String[] expectedResults = new String[] {
                "p0 = b - c",
                "p1 = b - c",
                "p2 = p1 * 2",
                "p3 = d * p2",
                "p4 = p0 + p3",
                "p5 = a + p4"
        };
        assertOpcodes(expectedResults, program.getInstructions());

    }

    @Test
    public void optimizeExpr() throws LexicalException, ParseException {
        String source = "a+(b-c)+d*(b-c)*2";
        ASTNode program = Parser.parse(source);
        ASTNode exprNode = program.getChild(0);
        ExprOptimizer optimizer = new ExprOptimizer();
        optimizer.optimize(exprNode);

        GraphvizHelpler graphvizHelper = new GraphvizHelpler();
        String actual = graphvizHelper.toDot(exprNode);

        String expected = "v1[label=\"+\"]\n" +
                "v2[label=\"a\"]\n" +
                "\"v1\" -> \"v2\"\n" +
                "v3[label=\"+\"]\n" +
                "\"v1\" -> \"v3\"\n" +
                "v4[label=\"-\"]\n" +
                "\"v3\" -> \"v4\"\n" +
                "v5[label=\"*\"]\n" +
                "\"v3\" -> \"v5\"\n" +
                "v6[label=\"b\"]\n" +
                "\"v4\" -> \"v6\"\n" +
                "v7[label=\"c\"]\n" +
                "\"v4\" -> \"v7\"\n" +
                "v8[label=\"d\"]\n" +
                "\"v5\" -> \"v8\"\n" +
                "v9[label=\"*\"]\n" +
                "\"v5\" -> \"v9\"\n" +
                "\"v9\" -> \"v4\"\n" +
                "v10[label=\"2\"]\n" +
                "\"v9\" -> \"v10\"\n";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAssignStmt() throws LexicalException, ParseException {
        String source = "a=1.0*2.0*3.0";
        ASTNode astNode = Parser.parse(source);

        Translator translator = new Translator();
        TAProgram program = translator.translate(astNode);
        String code = program.toString();

        String expected = "p0 = 2.0 * 3.0\n" +
                "p1 = 1.0 * p0\n" +
                "a = p1";
        Assert.assertEquals(expected, code);
    }

    @Test
    public void testAssignStmt1() throws LexicalException, ParseException {
        String source = "a=1";
        ASTNode astNode = Parser.parse(source);

        Translator translator = new Translator();
        TAProgram program = translator.translate(astNode);
        String code = program.toString();
        System.out.println(code);

        Assert.assertEquals("a = 1", code);
    }


    @Test
    public void testDeclareStmt() throws LexicalException, ParseException {
        String source = "var a=1.0*2.0*3.0";
        ASTNode astNode = Parser.parse(source);

        Translator translator = new Translator();
        TAProgram program = translator.translate(astNode);
        String code = program.toString();

        String expected = "p0 = 2.0 * 3.0\n" +
                "p1 = 1.0 * p0\n" +
                "a = p1";
        Assert.assertEquals(expected, code);
    }



}
