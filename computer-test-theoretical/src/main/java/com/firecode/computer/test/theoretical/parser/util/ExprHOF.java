package com.firecode.computer.test.theoretical.parser.util;

import com.firecode.computer.test.theoretical.parser.ast.ASTNode;

//HOF: High order function
@FunctionalInterface
public interface ExprHOF {

 ASTNode hoc() throws ParseException;

}
