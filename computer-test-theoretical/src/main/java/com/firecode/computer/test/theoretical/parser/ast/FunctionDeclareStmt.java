package com.firecode.computer.test.theoretical.parser.ast;

import com.firecode.computer.test.theoretical.lexer.Token;
import com.firecode.computer.test.theoretical.lexer.TokenType;
import com.firecode.computer.test.theoretical.parser.util.ParseException;
import com.firecode.computer.test.theoretical.parser.util.PeekTokenIterator;

/**
 * 函数语句
 */
public class FunctionDeclareStmt extends Stmt {


    public FunctionDeclareStmt() {
        super( ASTNodeTypes.FUNCTION_DECLARE_STMT, "func");
    }

    public static ASTNode parse(PeekTokenIterator it) throws ParseException {
        it.nextMatch("func");

        // func add() int {}
        FunctionDeclareStmt func = new FunctionDeclareStmt();
        Token lexeme = it.peek();
        Variable functionVariable = (Variable)Factor.parse( it);
        func.setLexeme(lexeme);
        func.addChild(functionVariable);
        it.nextMatch("(");
        ASTNode args = FunctionArgs.parse( it);
        it.nextMatch(")");
        func.addChild(args);
        Token keyword = it.nextMatch(TokenType.KEYWORD);
        if(!keyword.isType()) {
            throw new ParseException(keyword);
        }

        functionVariable.setTypeLexeme(keyword);
        ASTNode block = Block.parse(it);
        func.addChild(block);
        return func;
    }

    public ASTNode getArgs(){
        return this.getChild(1);
    }

    public Variable getFunctionVariable() {
        return (Variable)this.getChild(0);
    }

    public String getFuncType(){
        return this.getFunctionVariable().getTypeLexeme().getValue();
    }

    public Block getBlock(){
        return (Block)this.getChild(2);
    }


}
