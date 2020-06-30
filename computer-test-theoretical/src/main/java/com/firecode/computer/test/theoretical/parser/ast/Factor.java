package com.firecode.computer.test.theoretical.parser.ast;

import com.firecode.computer.test.theoretical.lexer.Token;
import com.firecode.computer.test.theoretical.lexer.TokenType;
import com.firecode.computer.test.theoretical.parser.util.PeekTokenIterator;

public class Factor extends ASTNode {
    public Factor(Token token) {
        super();
        this.lexeme = token;
        this.label = token.getValue();
    }

    public static ASTNode parse(PeekTokenIterator it) {
        Token token = it.peek();
        TokenType type = token.getType();

        if(type == TokenType.VARIABLE) {
            it.next();
            return new Variable(token);
        } else if(token.isScalar()){
            it.next();
            return new Scalar(token);
        }
        return null;
    }
}
