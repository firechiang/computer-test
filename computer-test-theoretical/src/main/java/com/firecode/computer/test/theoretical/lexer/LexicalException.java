package com.firecode.computer.test.theoretical.lexer;

public class LexicalException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private String msg;
	
	public LexicalException(String msg) {
		this.msg = msg;
	}

	public LexicalException(char c) {
		this.msg = String.format("Unexpected character %s", c);
	}
	
	@Override
	public String getMessage() {
		return msg;
	}
	
}
