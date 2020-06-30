package com.firecode.computer.test.theoretical.translator;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import com.firecode.computer.test.theoretical.translator.symbol.StaticSymbolTable;
import com.firecode.computer.test.theoretical.translator.symbol.Symbol;
import com.firecode.computer.test.theoretical.translator.symbol.SymbolTable;
import com.firecode.computer.test.theoretical.translator.symbol.SymbolType;

public class TAProgram {

    private ArrayList<TAInstruction> instructions = new ArrayList<>();
    private int labelCounter = 0;
    private StaticSymbolTable staticSymbolTable = new StaticSymbolTable();

    public void add(TAInstruction code) {
        instructions.add(code);
    }
    public ArrayList<TAInstruction> getInstructions() {
        return instructions;
    }


    @Override
    public String toString() {
    	ArrayList<String> lines = new ArrayList<String>();
        for(TAInstruction opcode : instructions) {
            lines.add(opcode.toString());
        }
        return StringUtils.join(lines, "\n");
    }

    public TAInstruction addLabel() {
        String label = "L" + labelCounter++;
        TAInstruction taCode = new TAInstruction(TAInstructionType.LABEL, null, null, null, null);
        taCode.setArg1(label);
        instructions.add(taCode);
        return taCode;
    }


    public void setStaticSymbols(SymbolTable symbolTable) {
        for(Symbol symbol : symbolTable.getSymbols()) {
            if(symbol.getType() == SymbolType.IMMEDIATE_SYMBOL) {
                staticSymbolTable.add(symbol);
            }
        }

        for(SymbolTable child : symbolTable.getChildren()) {
            setStaticSymbols(child);
        }
    }

    public StaticSymbolTable getStaticSymbolTable(){
        return this.staticSymbolTable;
    }
}
