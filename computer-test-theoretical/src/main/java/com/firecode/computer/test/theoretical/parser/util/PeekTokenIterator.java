package com.firecode.computer.test.theoretical.parser.util;


import java.util.stream.Stream;

import com.firecode.computer.test.theoretical.common.PeekIterator;
import com.firecode.computer.test.theoretical.lexer.Token;
import com.firecode.computer.test.theoretical.lexer.TokenType;

public class PeekTokenIterator extends PeekIterator<Token> {

    public PeekTokenIterator(Stream<Token> stream) {
        super(stream);
    }

    /**
     * 获取下一个并验证下一个值
     * @param value
     * @return
     * @throws ParseException
     */
    public Token nextMatch(String value) throws ParseException {
        Token token = this.next();
        if(!token.getValue().equals(value)) {
            throw new ParseException(token);
        }
        return token;
    }

    public Token nextMatch(TokenType type) throws ParseException {
        Token token = this.next();
        if(!token.getType().equals(type)) {
            throw new ParseException(token);
        }
        return token;
    }
}
