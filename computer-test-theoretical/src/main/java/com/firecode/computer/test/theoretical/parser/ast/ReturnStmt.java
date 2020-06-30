package com.firecode.computer.test.theoretical.parser.ast;

import com.firecode.computer.test.theoretical.lexer.Token;
import com.firecode.computer.test.theoretical.parser.util.ParseException;
import com.firecode.computer.test.theoretical.parser.util.PeekTokenIterator;

/**
 * 返回语句
 */
public class ReturnStmt extends Stmt {
    public ReturnStmt() {
        super(ASTNodeTypes.RETURN_STMT, "return");
    }

    public static ASTNode parse(PeekTokenIterator it) throws ParseException {
    	Token lexeme = it.nextMatch("return");
    	ASTNode expr = Expr.parse(it);

    	ReturnStmt stmt = new ReturnStmt();
        stmt.setLexeme(lexeme);
        if(expr != null) {
            stmt.addChild(expr);
        }
        return stmt;
    }


}
