package com.firecode.computer.test.theoretical.gen;
/**
 * 寻址类型
 */
public enum AddressingType {
	
    IMMEDIATE,
    // 寄存器寻址
    REGISTER,
    // 跳转
    JUMP,
    BRANCH,
    // 偏移量寻址
    OFFSET;
}
