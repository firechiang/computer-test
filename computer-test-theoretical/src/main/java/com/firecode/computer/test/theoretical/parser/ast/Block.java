package com.firecode.computer.test.theoretical.parser.ast;

import com.firecode.computer.test.theoretical.parser.util.ParseException;
import com.firecode.computer.test.theoretical.parser.util.PeekTokenIterator;

public class Block extends Stmt {
    public Block() {
        super(ASTNodeTypes.BLOCK, "block");
    }

    public static ASTNode parse(PeekTokenIterator it) throws ParseException {
        it.nextMatch("{");
        Block block = new Block();
        ASTNode stmt = null;
        while( (stmt = Stmt.parseStmt(it)) != null) {
            block.addChild(stmt);
        }
        it.nextMatch("}");
        return block;

    }




}
