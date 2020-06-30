package com.firecode.computer.test.theoretical.gen;


import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

import com.firecode.computer.test.theoretical.gen.operand.ImmediateNumber;
import com.firecode.computer.test.theoretical.gen.operand.Label;
import com.firecode.computer.test.theoretical.gen.operand.Offset;
import com.firecode.computer.test.theoretical.gen.operand.Operand;
import com.firecode.computer.test.theoretical.gen.operand.Register;
import com.firecode.computer.test.theoretical.translator.symbol.Symbol;
import com.firecode.computer.test.theoretical.translator.symbol.SymbolType;


public class ByteCodeTests {

    @Test
    public void add() throws GeneratorException {
    	Instruction a = new Instruction(OpCode.ADD);
        a.opList.add(Register.S2);
        a.opList.add(Register.S0);
        a.opList.add(Register.S1);
        assertSameInstruction(a, Instruction.fromByCode(a.toByteCode()));
    }

    void assertSameInstruction(Instruction a, Instruction b) throws GeneratorException {
        Assert.assertEquals(a.getOpCode(), b.getOpCode());
        Assert.assertEquals(a.opList.size(), b.opList.size());
        for(int i = 0; i  < a.opList.size(); i++) {
        	Operand p = a.opList.get(i);
        	Operand q = b.opList.get(i);
            if(p.getClass() == Label.class) {
                Assert.assertEquals(q.getClass(), Offset.class);
            } else {
                Assert.assertEquals(p.getClass(), q.getClass());
            }
            if(p.getClass() == ImmediateNumber.class) {
                Assert.assertEquals(((ImmediateNumber) p).getValue(), ((ImmediateNumber)q).getValue());
            } else if(p.getClass() == Offset.class) {
                Assert.assertEquals(((Offset)p).getOffset(), ((Offset)q).getOffset());
            } else if(p.getClass() == Register.class) {
                Assert.assertEquals(((Register)p).getAddr(), ((Register)q).getAddr());
            } else if(p.getClass() == Label.class) {
                Assert.assertEquals(((Label)p).getOffset(), ((Offset)q).getOffset());
            } else {
                throw new GeneratorException("Unsupported encode/decode type:" + p.getClass());
            }
        }
    }

    @Test
    public void mult() throws GeneratorException {
    	Instruction a = new Instruction(OpCode.MULT);
        a.opList.add(Register.S0);
        a.opList.add(Register.S1);

        Integer byteCode = a.toByteCode();
        Instruction b = Instruction.fromByCode(byteCode);
        assertSameInstruction(a, b);
    }


    @Test
    public void jump() throws GeneratorException {
    	Instruction a = new Instruction(OpCode.JUMP);
    	Label label = new Label("L0");
        a.opList.add(label);
        label.setOffset(-100);
        assertSameInstruction(a, Instruction.fromByCode(a.toByteCode()));
    }

    @Test
    public void jr() throws GeneratorException {
    	Instruction a = new Instruction(OpCode.JR);
    	Label label = new Label("L0");
        a.opList.add(label);
        label.setOffset(100);
        assertSameInstruction(a, Instruction.fromByCode(a.toByteCode()));
    }

    @Test
    public void sw() throws GeneratorException {
    	Symbol symbol = new Symbol(SymbolType.ADDRESS_SYMBOL);
        symbol.setOffset(-100);
        Instruction a = Instruction.saveToMemory(Register.S0, symbol);

        assertSameInstruction(a, Instruction.fromByCode(a.toByteCode()));
    }


    @Test
    public void sw1() throws GeneratorException {
    	Symbol symbol = new Symbol(SymbolType.IMMEDIATE_SYMBOL);
        symbol.setOffset(-100);
        Instruction a = Instruction.saveToMemory(Register.S0, symbol);

        assertSameInstruction(a, Instruction.fromByCode(a.toByteCode()));
    }

    @Test
    public void lw() throws GeneratorException {
    	Symbol symbol = new Symbol(SymbolType.ADDRESS_SYMBOL);
        symbol.setOffset(100);
        Instruction a = Instruction.loadToRegister(Register.S0, symbol);

        assertSameInstruction(a, Instruction.fromByCode(a.toByteCode()));
    }


    @Test
    public void lw1() throws GeneratorException {
    	Symbol symbol = new Symbol(SymbolType.IMMEDIATE_SYMBOL);
        symbol.setOffset(100);
        Instruction a = Instruction.loadToRegister(Register.S0, symbol);

        assertSameInstruction(a, Instruction.fromByCode(a.toByteCode()));
    }

    @Test
    public void sp() throws GeneratorException {
    	Instruction a = Instruction.immediate(OpCode.ADDI, Register.SP, new ImmediateNumber(100));
        assertSameInstruction(a, Instruction.fromByCode(a.toByteCode()));
    }

    @Test
    public void bne() throws GeneratorException {
    	Instruction a = Instruction.bne(Register.S0, Register.S1, "L0");
        ((Label)a.opList.get(2)).setOffset(100);

        assertSameInstruction(a, Instruction.fromByCode(a.toByteCode()));
    }

}
