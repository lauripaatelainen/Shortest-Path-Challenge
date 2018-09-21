/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edii.spc.ui;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author edii
 */
public class UtilTest {
    @Test
    public void testRightPad() {
        String str = "str";
        char ch = '+';
        
        assertEquals("str", Util.rightPad(str, -1, ch));
        assertEquals("str", Util.rightPad(str, 0, ch));
        assertEquals("str", Util.rightPad(str, 1, ch));
        assertEquals("str", Util.rightPad(str, 2, ch));
        assertEquals("str", Util.rightPad(str, 3, ch));
        assertEquals("str+", Util.rightPad(str, 4, ch));
        assertEquals("str++", Util.rightPad(str, 5, ch));
    }
}
