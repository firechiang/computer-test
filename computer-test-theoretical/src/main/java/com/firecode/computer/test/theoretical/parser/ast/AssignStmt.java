package com.firecode.computer.test.theoretical.parser.ast;

import com.firecode.computer.test.theoretical.lexer.Token;
import com.firecode.computer.test.theoretical.parser.util.ParseException;
import com.firecode.computer.test.theoretical.parser.util.PeekTokenIterator;

public class AssignStmt extends Stmt {

    public AssignStmt() {
        super(ASTNodeTypes.ASSIGN_STMT, "assign");
    }
    public static ASTNode parse(PeekTokenIterator it) throws ParseException {
    	AssignStmt stmt = new AssignStmt();
        Token tkn = it.peek();
        ASTNode factor = Factor.parse(it);
        if(factor == null) {
            throw new ParseException(tkn);
        }
        stmt.addChild(factor);
        Token lexeme = it.nextMatch("=");
        ASTNode expr = Expr.parse(it);
        stmt.addChild(expr);
        stmt.setLexeme(lexeme);
        return stmt;

    }

}
