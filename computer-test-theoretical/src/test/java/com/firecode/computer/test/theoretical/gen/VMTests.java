package com.firecode.computer.test.theoretical.gen;


import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.firecode.computer.test.theoretical.gen.operand.Register;
import com.firecode.computer.test.theoretical.lexer.LexicalException;
import com.firecode.computer.test.theoretical.parser.Parser;
import com.firecode.computer.test.theoretical.parser.ast.ASTNode;
import com.firecode.computer.test.theoretical.parser.util.ParseException;
import com.firecode.computer.test.theoretical.translator.TAProgram;
import com.firecode.computer.test.theoretical.translator.Translator;
import com.firecode.computer.test.theoretical.vm.VirtualMachine;


public class VMTests {

    @Test
    public void calcExpr() throws LexicalException, ParseException, GeneratorException {
        String source = "func main() int { var a = 2*3+4 \n return \n }";
        ASTNode astNode = Parser.parse(source);
        Translator translator = new Translator();
        TAProgram taProgram = translator.translate(astNode);
        OpCodeGen gen = new OpCodeGen();
        OpCodeProgram program = gen.gen(taProgram);
        ArrayList<Integer> statics = program.getStaticArea(taProgram);
        Integer entry = program.getEntry();
        ArrayList<Integer> opcodes = program.toByteCodes();

        VirtualMachine vm = new VirtualMachine(statics, opcodes, entry);
        // CALL main
        vm.runOneStep();
        vm.runOneStep();
        vm.runOneStep();
        System.out.println("RA:" + vm.getRegisters()[Register.RA.getAddr()]);
        Assert.assertEquals(18, vm.getSpMemory(0));

        // p0 = 2 * 3
        vm.runOneStep();
        vm.runOneStep();
        vm.runOneStep();
        vm.runOneStep();
        vm.runOneStep();
        Assert.assertEquals(2, vm.getRegisters()[Register.S0.getAddr()]);
        Assert.assertEquals(3, vm.getRegisters()[Register.S1.getAddr()]);
        Assert.assertEquals(6, vm.getRegisters()[Register.LO.getAddr()]);
        Assert.assertEquals(6, vm.getRegisters()[Register.S2.getAddr()]);
        Assert.assertEquals(6, vm.getSpMemory(-2));

        // p1 = p0 + 4
        vm.runOneStep();

        vm.runOneStep();

        vm.runOneStep();
        vm.runOneStep();
        Assert.assertEquals(6, vm.getRegisters()[Register.S0.getAddr()]);
        Assert.assertEquals(4, vm.getRegisters()[Register.S1.getAddr()]);
        Assert.assertEquals(10, vm.getRegisters()[Register.S2.getAddr()]);
        Assert.assertEquals(10, vm.getSpMemory(-3));

        // a = p1
        vm.runOneStep();
        vm.runOneStep();
        Assert.assertEquals(10, vm.getSpMemory(-1));
        Assert.assertEquals(10, vm.getRegisters()[Register.S0.getAddr()]);

        // RETURN null
        vm.runOneStep();
        vm.runOneStep();
        vm.runOneStep();

        System.out.println("SP:" + vm.getRegisters()[Register.SP.getAddr()]);
    }

    @Test
    public void recursiveFunction() throws FileNotFoundException, ParseException, LexicalException, UnsupportedEncodingException, GeneratorException {
    	ASTNode astNode = Parser.fromFile("./example/fact2.ts");
    	Translator translator = new Translator();
    	TAProgram taProgram = translator.translate(astNode);
    	OpCodeGen gen = new OpCodeGen();
    	OpCodeProgram program = gen.gen(taProgram);
    	ArrayList<Integer> statics = program.getStaticArea(taProgram);
    	Integer entry = program.getEntry();
    	ArrayList<Integer> opcodes = program.toByteCodes();
        System.out.println(taProgram.getStaticSymbolTable());
        VirtualMachine vm = new VirtualMachine(statics, opcodes, entry);
        // CALL main
        vm.runOneStep();
        vm.runOneStep();
        vm.runOneStep();
        System.out.println("RA:" + vm.getRegisters()[Register.RA.getAddr()]);
        Assert.assertEquals(39, vm.getSpMemory(0));

        // PARAM 10 0
        vm.runOneStep();
        vm.runOneStep();
        Assert.assertEquals(2, vm.getSpMemory(-3));

        // SP -2
        vm.runOneStep();
        vm.runOneStep();
        System.out.println("RA:" + vm.getRegisters()[Register.RA.getAddr()]);

        // #FUNC_BEGIN
        vm.runOneStep();
        Assert.assertEquals(33, vm.getSpMemory(0));

        // #p1 = n == 0
        Assert.assertEquals(2, vm.getSpMemory(-1));
        vm.runOneStep();
        vm.runOneStep();
        vm.runOneStep();
        vm.runOneStep();
        Assert.assertEquals(false, vm.getSpMemory(-2) == 0);

        // #IF p1 ELSE L1
        vm.runOneStep();
        vm.runOneStep();

        // #p3 = n - 1
        vm.runOneStep();
        vm.runOneStep();
        vm.runOneStep();
        Assert.assertEquals(1, vm.getSpMemory(-3));

        // #PARAM p3 0
        // #SP-5
        vm.runOneStep();
        vm.runOneStep();
        vm.runOneStep();
        Assert.assertEquals(1, vm.getSpMemory(-1));
        System.out.println(vm.getSpMemory(-2));

        vm.runOneStep();
        vm.runOneStep();

        // #p1 = n == 0
        vm.runOneStep();
        vm.runOneStep();
        vm.runOneStep();
        vm.runOneStep();
        Assert.assertEquals(false, vm.getSpMemory(-2) == 0);

        // #IF p1 ELSE L1
        vm.runOneStep();

        // #p3 = n - 1
        vm.runOneStep();
        vm.runOneStep();
        vm.runOneStep();
        vm.runOneStep();


        // #PARAM p3 0
        vm.runOneStep();
        vm.runOneStep();
        vm.runOneStep();

        // CALL
        vm.runOneStep();
        vm.runOneStep();

        // #p1 = n == 0
        vm.runOneStep();
        vm.runOneStep();
        vm.runOneStep();
        vm.runOneStep();
        Assert.assertEquals(true, vm.getSpMemory(-2) == 0);

        // #IF p1 ELSE L1
        vm.runOneStep();

        // RETURN 1
        vm.runOneStep();
        vm.runOneStep();

        vm.runOneStep();
        vm.runOneStep();

        // #p4 = p2 * n 计算递归值
        vm.runOneStep();
        vm.runOneStep();
        vm.runOneStep();
        vm.runOneStep();
        vm.runOneStep();
        // #RETURN p4
        vm.runOneStep();
        vm.runOneStep();
        //RETURN
        vm.runOneStep();
        vm.runOneStep();

        //#p4 = p2 * n
        vm.runOneStep();
        vm.runOneStep();
        vm.runOneStep();
        vm.runOneStep();
        vm.runOneStep();

        Assert.assertEquals(2, vm.getSpMemory(-5));

        vm.runOneStep();
        vm.runOneStep();
        // RETURN MAIN
        vm.runOneStep();

        // SP 2
        vm.runOneStep();



        // #RETURN p1 : from main
        vm.runOneStep();
        Assert.assertEquals(2, vm.getSpMemory(-1));

        while(vm.runOneStep());
        Assert.assertEquals(2, vm.getSpMemory(0));
    }


    @Test
    public void recursiveFunction1() throws FileNotFoundException, ParseException, LexicalException, UnsupportedEncodingException, GeneratorException {
    	ASTNode astNode = Parser.fromFile("./example/fact5.ts");
    	Translator translator = new Translator();
    	TAProgram taProgram = translator.translate(astNode);
    	OpCodeGen gen = new OpCodeGen();
    	OpCodeProgram program = gen.gen(taProgram);
    	ArrayList<Integer> statics = program.getStaticArea(taProgram);
    	Integer entry = program.getEntry();
    	ArrayList<Integer> opcodes = program.toByteCodes();
        System.out.println(taProgram.getStaticSymbolTable());
        VirtualMachine vm = new VirtualMachine(statics, opcodes, entry);
        vm.run();

        Assert.assertEquals(120, vm.getSpMemory(0));

    }
}
