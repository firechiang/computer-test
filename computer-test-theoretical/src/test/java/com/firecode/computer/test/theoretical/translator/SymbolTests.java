package com.firecode.computer.test.theoretical.translator;

import org.junit.Assert;
import org.junit.Test;

import com.firecode.computer.test.theoretical.lexer.Token;
import com.firecode.computer.test.theoretical.lexer.TokenType;
import com.firecode.computer.test.theoretical.translator.symbol.Symbol;
import com.firecode.computer.test.theoretical.translator.symbol.SymbolTable;

public class SymbolTests {

    @Test
    public void symbolTable(){
    	SymbolTable symbolTable = new SymbolTable();
        symbolTable.createLabel("L0", new Token(TokenType.VARIABLE, "foo"));
        symbolTable.createVariable();
        symbolTable.createSymbolByLexeme(new Token(TokenType.VARIABLE, "foo"));
        Assert.assertEquals(1, symbolTable.localSize());
    }

    @Test
    public void symbolTableChain() {
    	SymbolTable symbolTable = new SymbolTable();
        symbolTable.createSymbolByLexeme(new Token(TokenType.VARIABLE, "a"));

        SymbolTable childTable = new SymbolTable();
        symbolTable.addChild(childTable);

        SymbolTable childChildTable = new SymbolTable();
        childTable.addChild(childChildTable);
        Assert.assertEquals(true, childChildTable.exists(new Token(TokenType.VARIABLE, "a")));
        Assert.assertEquals(true, childTable.exists(new Token(TokenType.VARIABLE, "a")));
    }

    @Test
    public void symbolOffset() {
    	SymbolTable symbolTable = new SymbolTable();
        symbolTable.createSymbolByLexeme(new Token(TokenType.INTEGER, "100"));
        Symbol symbolA = symbolTable.createSymbolByLexeme(new Token(TokenType.VARIABLE, "a"));
        Symbol symbolB = symbolTable.createSymbolByLexeme(new Token(TokenType.VARIABLE, "b"));

        SymbolTable childTable = new SymbolTable();
        symbolTable.addChild(childTable);
        Symbol anotherSymbolB = childTable.createSymbolByLexeme(new Token(TokenType.VARIABLE, "b"));
        Symbol symbolC = childTable.createSymbolByLexeme(new Token(TokenType.VARIABLE, "c"));

        Assert.assertEquals(0, symbolA.getOffset());
        Assert.assertEquals(1, symbolB.getOffset());
        Assert.assertEquals(1, anotherSymbolB.getOffset());
        Assert.assertEquals(1, anotherSymbolB.getLayerOffset());
        Assert.assertEquals(0, symbolC.getOffset());
        Assert.assertEquals(0, symbolC.getLayerOffset());
    }
}
