package com.firecode.computer.test.theoretical.translator;

import org.apache.commons.lang3.NotImplementedException;

import com.firecode.computer.test.theoretical.lexer.Token;
import com.firecode.computer.test.theoretical.lexer.TokenType;
import com.firecode.computer.test.theoretical.parser.ast.ASTNode;
import com.firecode.computer.test.theoretical.parser.ast.ASTNodeTypes;
import com.firecode.computer.test.theoretical.parser.ast.Block;
import com.firecode.computer.test.theoretical.parser.ast.Expr;
import com.firecode.computer.test.theoretical.parser.ast.FunctionDeclareStmt;
import com.firecode.computer.test.theoretical.parser.ast.IfStmt;
import com.firecode.computer.test.theoretical.parser.util.ParseException;
import com.firecode.computer.test.theoretical.translator.symbol.Symbol;
import com.firecode.computer.test.theoretical.translator.symbol.SymbolTable;

import java.util.ArrayList;

public class Translator {

    public TAProgram translate(ASTNode astNode) throws ParseException {
    	TAProgram program = new TAProgram();
    	SymbolTable symbolTable = new SymbolTable();

        for(ASTNode child : astNode.getChildren()) {
            translateStmt(program, child, symbolTable);
        }

        program.setStaticSymbols(symbolTable);

        Token main = new Token(TokenType.VARIABLE, "main");
        if(symbolTable.exists(main)) {
            symbolTable.createVariable(); // 返回值
            program.add(new TAInstruction(TAInstructionType.SP, null, null,
                    -symbolTable.localSize(), null));
            program.add(new TAInstruction(
                    TAInstructionType.CALL, null, null,
                    symbolTable.cloneFromSymbolTree(main, 0),null ));
            program.add(new TAInstruction(TAInstructionType.SP, null, null,
                    symbolTable.localSize(), null));
        }
        return program;
    }

    /**
     * 语句块翻译
     * @throws ParseException
     */
    public  void translateBlock(TAProgram program, Block block, SymbolTable parent) throws ParseException {
    	SymbolTable symbolTable = new SymbolTable();

        /**
         * 每个Block增加一个作用域链
         */
    	Symbol parentOffset = symbolTable.createVariable();
        parentOffset.setLexeme(new Token(TokenType.INTEGER, symbolTable.localSize()+""));

        parent.addChild(symbolTable);
        for(ASTNode child : block.getChildren()) {
            translateStmt(program, child, symbolTable);
        }
    }

    public void translateStmt(TAProgram program, ASTNode node, SymbolTable symbolTable) throws ParseException {
        switch (node.getType()) {
            case BLOCK:
                translateBlock(program, (Block)node, symbolTable);
                return;
            case IF_STMT:
                translateIfStmt(program, (IfStmt)node, symbolTable);
                return;
            case ASSIGN_STMT:
                translateAssignStmt(program, node, symbolTable);
                return;
            case DECLARE_STMT:
                translateDeclareStmt(program, node, symbolTable);
                return;
            case FUNCTION_DECLARE_STMT:
                translateFunctionDeclareStmt(program, node, symbolTable);
                return;
            case RETURN_STMT:
                translateReturnStmt(program, node, symbolTable);
                return;
            case CALL_EXPR:
                translateCallExpr(program, node, symbolTable);
                return;
        }
        throw new NotImplementedException("Translator not impl. for " + node.getType());
    }

    private void translateReturnStmt(TAProgram program, ASTNode node, SymbolTable symbolTable) throws ParseException {
        Symbol resultValue = null;
        if(node.getChild(0) != null) {
            resultValue = translateExpr(program, node.getChild(0), symbolTable);
        }
        program.add(new TAInstruction(TAInstructionType.RETURN, null, null, resultValue, null));
    }

    private void translateFunctionDeclareStmt(TAProgram program, ASTNode node, SymbolTable parent) throws ParseException {
    	TAInstruction label = program.addLabel();

    	SymbolTable symbolTable = new SymbolTable();

        program.add(new TAInstruction(TAInstructionType.FUNC_BEGIN, null, null, null, null));
        symbolTable.createVariable(); // 返回地址

        label.setArg2(node.getLexeme().getValue());
        FunctionDeclareStmt func = (FunctionDeclareStmt)node;
        ASTNode args = func.getArgs();
        parent.addChild(symbolTable);
        parent.createLabel((String)label.getArg1(), node.getLexeme());
        for(ASTNode arg : args.getChildren()) {
            symbolTable.createSymbolByLexeme(arg.getLexeme());
        }

        for(ASTNode child : func.getBlock().getChildren()) {
            translateStmt(program, child, symbolTable);
        }

    }

    private void translateDeclareStmt(TAProgram program, ASTNode node, SymbolTable symbolTable) throws ParseException {
    	Token lexeme = node.getChild(0).getLexeme();
        if(symbolTable.exists(lexeme)) {
            throw new ParseException("Syntax Error, Identifier " + lexeme.getValue() + " is already defined.");
        }
        Symbol assigned = symbolTable.createSymbolByLexeme(node.getChild(0).getLexeme());
        ASTNode expr = node.getChild(1);
        Symbol addr = translateExpr(program, expr, symbolTable);
        program.add(new TAInstruction(TAInstructionType.ASSIGN, assigned, "=", addr, null));
    }

    private void translateAssignStmt(TAProgram program, ASTNode node, SymbolTable symbolTable) throws ParseException {
    	Symbol assigned = symbolTable.createSymbolByLexeme(node.getChild(0).getLexeme());
    	ASTNode expr = node.getChild(1);
    	Symbol addr = translateExpr(program, expr, symbolTable);
        program.add(new TAInstruction(TAInstructionType.ASSIGN, assigned, "=", addr, null));

    }


    /**
     * IF语句翻译成三地址代码
     * 1. 表达式
     * 2. 语句块
     * 3. else Tail处理
     */
    public void translateIfStmt(TAProgram program, IfStmt node, SymbolTable symbolTable) throws ParseException {
    	ASTNode expr = node.getExpr();
    	Symbol exprAddr = translateExpr(program,expr,symbolTable);
    	TAInstruction ifOpCode = new TAInstruction(TAInstructionType.IF, null, null, exprAddr, null);
        program.add(ifOpCode);

        translateBlock(program, (Block)node.getBlock(), symbolTable);

        TAInstruction gotoInstruction = null;
        if(node.getChild(2) != null) {
            gotoInstruction = new TAInstruction(TAInstructionType.GOTO, null, null, null, null);
            program.add(gotoInstruction);
            TAInstruction labelEndIf = program.addLabel();
            ifOpCode.setArg2(labelEndIf.getArg1());
        }

        if(node.getElseBlock() != null) {
            translateBlock(program, (Block)node.getElseBlock(), symbolTable);
        } else if(node.getElseIfStmt() != null) {
            translateIfStmt(program, (IfStmt) node.getElseIfStmt(), symbolTable);
        }

        TAInstruction labelEnd = program.addLabel();
        if(node.getChild(2) == null) {
            ifOpCode.setArg2(labelEnd.getArg1());
        } else {
            gotoInstruction.setArg1(labelEnd.getArg1());
        }
    }



    /**
     * Expr -> Expr1 op Expr2
     *  T: result = Expr1.addr op Expr2.addr
     * Expr1 -> Factor
     *  T: Expr1.addr = symbolTable.find(factor)
     */
    public Symbol translateExpr(
            TAProgram program,
            ASTNode node,
            SymbolTable symbolTable) throws ParseException {

        if(node.isValueType()) {
        	Symbol addr = symbolTable.createSymbolByLexeme(node.getLexeme());
            node.setProp("addr", addr);
            return addr;
        }
        else if(node.getType() == ASTNodeTypes.CALL_EXPR) {
        	Symbol addr = translateCallExpr(program,node,symbolTable);
            node.setProp("addr", addr);
            return addr;
        }
        else if(node instanceof Expr){
            for(ASTNode child : node.getChildren()) {
                translateExpr(program,child, symbolTable);
            }
            if(node.getProp("addr") == null) {
                node.setProp("addr", symbolTable.createVariable());
            }
            TAInstruction instruction = new TAInstruction(
                    TAInstructionType.ASSIGN,
                    (Symbol)(node.getProp("addr")),
                    node.getLexeme().getValue(),
                    (Symbol)(node.getChild(0).getProp("addr")),
                    (Symbol)(node.getChild(1).getProp("addr"))
            );
            program.add(instruction);
            return instruction.getResult();
        }
        throw new NotImplementedException("Unexpected node type :" + node.getType());
    }

    private Symbol translateCallExpr(TAProgram program, ASTNode node, SymbolTable symbolTable) throws ParseException {

    	ASTNode factor = node.getChild(0);



    	ArrayList<TAInstruction> list = new ArrayList<TAInstruction>();
        for(int i = 1; i < node.getChildren().size(); i++) {
        	ASTNode expr = node.getChildren().get(i);
        	Symbol addr = translateExpr(program, expr, symbolTable);
            list.add(new TAInstruction(TAInstructionType.PARAM, null, null, addr, i - 1));
        }

        for(TAInstruction instruction : list) {
            instruction.setArg2( symbolTable.localSize() + (int)instruction.getArg2() + 2);
            program.add(instruction);
        }

        Symbol returnValue = symbolTable.createVariable();
        Symbol funcAddr = symbolTable.cloneFromSymbolTree(factor.getLexeme(), 0);
        if(funcAddr == null) {
            throw new ParseException("function " + factor.getLexeme().getValue() + " not found");
        }
        program.add(new TAInstruction(TAInstructionType.SP, null, null,
                -symbolTable.localSize(), null));
        program.add(new TAInstruction(TAInstructionType.CALL, null, null, funcAddr, null));

        program.add(new TAInstruction(TAInstructionType.SP, null, null,
            symbolTable.localSize(),null));
        return returnValue;
    }
}