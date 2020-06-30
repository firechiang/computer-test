package com.firecode.computer.test.theoretical.gen.operand;


public class Label extends Offset{
    String label;
    public Label(String label){
        super(0);
        this.label = label;
    }

   public String getLabel() {
        return this.label;
    }
    @Override
    public String toString() {
        return this.label;
    }
}
