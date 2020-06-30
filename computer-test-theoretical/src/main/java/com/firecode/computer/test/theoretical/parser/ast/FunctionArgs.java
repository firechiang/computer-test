package com.firecode.computer.test.theoretical.parser.ast;

import com.firecode.computer.test.theoretical.lexer.Token;
import com.firecode.computer.test.theoretical.parser.util.ParseException;
import com.firecode.computer.test.theoretical.parser.util.PeekTokenIterator;

public class FunctionArgs extends ASTNode {
    public FunctionArgs() {
        super();
        this.label = "args";
    }

    public static ASTNode parse(PeekTokenIterator it) throws ParseException {

    	FunctionArgs args = new FunctionArgs();

        while(it.peek().isType()) {
            Token type = it.next();
            Variable variable = (Variable)Factor.parse(it);
            variable.setTypeLexeme(type);
            args.addChild(variable);

            if(!it.peek().getValue().equals(")")) {
                it.nextMatch(",");
            }
        }

        return args;
    }


}
