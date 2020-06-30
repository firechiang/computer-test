package com.firecode.computer.test.theoretical.parser.ast;

import com.firecode.computer.test.theoretical.parser.util.ParseException;
import com.firecode.computer.test.theoretical.parser.util.PeekTokenIterator;

public class Program extends Block {
    public Program() {
        super();
    }

    public static ASTNode parse(PeekTokenIterator it) throws ParseException {
    	Program block = new Program();
        ASTNode stmt = null;
        while( (stmt = Stmt.parseStmt(it)) != null) {
            block.addChild(stmt);
        }
        return block;

    }
}
