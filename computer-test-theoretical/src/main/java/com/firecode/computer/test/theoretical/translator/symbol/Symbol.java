package com.firecode.computer.test.theoretical.translator.symbol;

import com.firecode.computer.test.theoretical.lexer.Token;

/**
 * 一个值或者变量的集合体
 */
public class Symbol {

    SymbolTable parent;
    Token lexeme;
    String label;
    // 地址
    int offset;
    // 记录层级关系
    int layerOffset = 0;
    SymbolType type;
    public Symbol(SymbolType type){
        this.type = type;
    }

    public static Symbol createAddressSymbol(Token lexeme, int offset){
    	Symbol symbol = new Symbol(SymbolType.ADDRESS_SYMBOL);
        symbol.lexeme = lexeme;
        symbol.offset = offset;
        return symbol;
    }

    public static Symbol createImmediateSymbol(Token lexeme){
    	Symbol symbol = new Symbol(SymbolType.IMMEDIATE_SYMBOL);
        symbol.lexeme = lexeme;
        return symbol;
    }

    public static Symbol createLabelSymbol(String label, Token lexeme) {
    	Symbol symbol = new Symbol(SymbolType.LABEL_SYMBOL);
        symbol.label = label;
        symbol.lexeme = lexeme;
        return symbol;
    }


    public Symbol copy() {
    	Symbol symbol = new Symbol(this.type);
        symbol.lexeme = this.lexeme;
        symbol.label = this.label;
        symbol.offset = this.offset;
        symbol.layerOffset = this.layerOffset;
        symbol.type = this.type;
        return symbol;
    }

    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }

    public void setOffset(int offset){
        this.offset = offset;
    }

    public SymbolType getType(){
        return this.type;
    }

    @Override
    public String toString() {
        if(this.type == SymbolType.LABEL_SYMBOL){
            return this.label;
        }
        return lexeme.getValue();
    }

    public void setLexeme(Token lexeme) {
        this.lexeme = lexeme;
    }

    public int getOffset() {
        return this.offset;
    }

    public Token getLexeme() {
        return this.lexeme;
    }

    public void setLayerOffset(int offset) {
        this.layerOffset = offset;
    }

    public int getLayerOffset(){
        return this.layerOffset;
    }

    public String getLabel() {
        return this.label;
    }
}
