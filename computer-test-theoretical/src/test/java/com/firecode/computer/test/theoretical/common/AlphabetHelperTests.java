package com.firecode.computer.test.theoretical.common;

import org.junit.Test;

import junit.framework.Assert;

public class AlphabetHelperTests {
	
    @Test
    public void test(){
    	Assert.assertEquals(true, AlphabetHelper.isLetter('a'));
        Assert.assertEquals(false, AlphabetHelper.isLetter('*'));
        Assert.assertEquals(true, AlphabetHelper.isLiteral('a'));
        Assert.assertEquals(true, AlphabetHelper.isLiteral('_'));
        Assert.assertEquals(true, AlphabetHelper.isLiteral('9'));
        Assert.assertEquals(false, AlphabetHelper.isLiteral('*'));
        Assert.assertEquals(false, AlphabetHelper.isLetter('*'));
        Assert.assertEquals(true, AlphabetHelper.isNumber('1'));
        Assert.assertEquals(true, AlphabetHelper.isNumber('9'));
        Assert.assertEquals(false, AlphabetHelper.isNumber('x'));
        Assert.assertEquals(true, AlphabetHelper.isOperator('&'));
        Assert.assertEquals(true, AlphabetHelper.isOperator('*'));
        Assert.assertEquals(true, AlphabetHelper.isOperator('+'));
        Assert.assertEquals(true, AlphabetHelper.isOperator('/'));
        Assert.assertEquals(true, AlphabetHelper.isOperator('='));
        Assert.assertEquals(true, AlphabetHelper.isOperator(','));
        Assert.assertEquals(false, AlphabetHelper.isOperator('a'));
    }

}
