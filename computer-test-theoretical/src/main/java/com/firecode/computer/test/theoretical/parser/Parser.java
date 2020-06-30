package com.firecode.computer.test.theoretical.parser;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.firecode.computer.test.theoretical.common.PeekIterator;
import com.firecode.computer.test.theoretical.lexer.Lexer;
import com.firecode.computer.test.theoretical.lexer.LexicalException;
import com.firecode.computer.test.theoretical.lexer.Token;
import com.firecode.computer.test.theoretical.parser.ast.ASTNode;
import com.firecode.computer.test.theoretical.parser.ast.Program;
import com.firecode.computer.test.theoretical.parser.util.ParseException;
import com.firecode.computer.test.theoretical.parser.util.PeekTokenIterator;

/**
 * 将代码块解析成语法树
 */
public class Parser {

    public static ASTNode parse(String source) throws LexicalException, ParseException {
    	Lexer lexer = new Lexer();
    	ArrayList<Token> tokens = lexer.analyse(new PeekIterator<>(source.chars().mapToObj(x ->(char)x), '\0'));
        return Program.parse(new PeekTokenIterator(tokens.stream()));
    }

    public static ASTNode fromFile(String file) throws FileNotFoundException, UnsupportedEncodingException, LexicalException, ParseException {
    	ArrayList<Token> tokens = Lexer.fromFile(file);
        return Program.parse(new PeekTokenIterator(tokens.stream()));
    }
}
