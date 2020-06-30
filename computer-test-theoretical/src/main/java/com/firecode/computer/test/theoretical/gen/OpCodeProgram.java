package com.firecode.computer.test.theoretical.gen;

import org.apache.commons.lang3.StringUtils;

import com.firecode.computer.test.theoretical.translator.TAProgram;
import com.firecode.computer.test.theoretical.translator.symbol.Symbol;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * 指令
 */
public class OpCodeProgram {

	/**
	 * 入口
	 */
    Integer entry = null;
    /**
     * 存储指令
     */
    ArrayList<Instruction> instructions = new ArrayList<>();
    /**
     * 存储注释
     */
    Hashtable<Integer, String> comments = new Hashtable<>();
    public void add(Instruction i) {
        this.instructions.add(i);
    }

    @Override
    public String toString() {
        ArrayList<String> prts = new ArrayList<>();
        for(int i = 0; i < instructions.size(); i++) {
            if(this.comments.containsKey(i)) {
                prts.add("#" + this.comments.get(i));
            }
            String str = instructions.get(i).toString();
            if(this.entry != null && i == this.entry) {
                str = "MAIN:" + str;
            }
            prts.add(str);
        }
        return StringUtils.join(prts, "\n");
    }

    public ArrayList<Integer> toByteCodes() {
    	ArrayList<Integer> codes = new ArrayList<Integer>();

        for(Instruction instruction : instructions) {
            codes.add(instruction.toByteCode());
        }
        return codes;

    }

    public void setEntry(int entry) {
        this.entry = entry;
    }

    /**
     * 添加一行注释
     * @param comment
     */
    public void addComment(String comment) {
        this.comments.put(this.instructions.size(), comment);
    }

    public Integer getEntry() {
        return this.entry;

    }

    public ArrayList<Integer> getStaticArea(TAProgram taProgram) {
    	ArrayList<Integer> list = new ArrayList<Integer>();
        for(Symbol symbol : taProgram.getStaticSymbolTable().getSymbols()) {
            list.add(Integer.parseInt(symbol.getLexeme().getValue()));
        }
        return list;
    }
}
