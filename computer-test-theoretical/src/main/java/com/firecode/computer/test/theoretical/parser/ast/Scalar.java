package com.firecode.computer.test.theoretical.parser.ast;

import com.firecode.computer.test.theoretical.lexer.Token;

public class Scalar extends Factor{
    public Scalar(Token token) {
        super(token);
        this.type = ASTNodeTypes.SCALAR;
    }
}
