package com.firecode.computer.test.theoretical.parser.ast;

import com.firecode.computer.test.theoretical.lexer.Token;

public class Variable extends Factor {

    private Token typeLexeme = null;

    public Variable(Token token) {
        super(token);
        this.type = ASTNodeTypes.VARIABLE;
    }

    public void setTypeLexeme(Token token) {
        this.typeLexeme = token;
    }
    public Token getTypeLexeme(){
        return this.typeLexeme;
    }
}