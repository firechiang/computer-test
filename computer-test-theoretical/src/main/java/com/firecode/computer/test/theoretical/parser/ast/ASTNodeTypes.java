package com.firecode.computer.test.theoretical.parser.ast;

/**
 * 类型
 *
 */
public enum ASTNodeTypes {
	/**
	 * 代码块
	 */
    BLOCK,
    BINARY_EXPR, // 1+1
    UNARY_EXPR, // ++i
    CALL_EXPR,
    VARIABLE,
    SCALAR, // 1.0, true
    /**
     * if 语句
     */
    IF_STMT,
    /**
     * while语句
     */
    WHILE_STMT,
    /**
     * for循环语句
     */
    FOR_STMT,
    /**
     * 返回语句
     */
    RETURN_STMT,
    ASSIGN_STMT,
    FUNCTION_DECLARE_STMT,
    DECLARE_STMT
}
