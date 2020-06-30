package com.firecode.computer.test.theoretical.common;

import org.junit.Assert;
import org.junit.Test;


public class PeekIteratorTests {
	
	@Test
	public void test_peek() {
		String source = "abcdefg";
		PeekIterator<Character> it = new PeekIterator<Character>(source.chars().mapToObj(c ->(char)c));
	    Assert.assertEquals('a', (char)it.next());
	    Assert.assertEquals('b', (char)it.next());
	    it.next();
	    it.next();
	    Assert.assertEquals('e', (char)it.next());
	    Assert.assertEquals('f', (char)it.peek());
	    Assert.assertEquals('f', (char)it.peek());
	    Assert.assertEquals('f', (char)it.next());
	    Assert.assertEquals('g', (char)it.next());
	}
	
	@Test
	public void test_lookahead() {
		String source = "abcdefg";
		PeekIterator<Character> it = new PeekIterator<Character>(source.chars().mapToObj(c ->(char)c));
		Assert.assertEquals('a', (char)it.next());
		Assert.assertEquals('b', (char)it.next());
		Assert.assertEquals('c', (char)it.next());
		it.putBack();
		it.putBack();
		Assert.assertEquals('b', (char)it.next());
	}
	
	@Test
	public void testendtoken() {
		String source = "abcdefg";
		PeekIterator<Character> it = new PeekIterator<Character>(source.chars().mapToObj(c ->(char)c),(char)1);
		int i = 0;
		while(it.hasNext()) {
			if(i == 7) {
				Assert.assertEquals((char)1, (char)it.next());
			}else {
				Assert.assertEquals((char)source.charAt(i++), (char)it.next());
				
			}
		}
	}

}
