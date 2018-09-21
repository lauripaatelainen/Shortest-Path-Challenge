/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edii.spc.datastructures;

import com.edii.spc.game.Game;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author edii
 */
public class GameTest {
    
    @Test
    public void testConstructorAndVerifySize() {
        for (int i = 1; i < 100; i++) {
            Game game = new Game(i);
            Assert.assertEquals(i, game.getSize());
            Assert.assertEquals(i, game.getGameField().getSize());
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void illegalSizeZero() {
        new Game(0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void illegalSizeNegative() {
        new Game(-1);
    }
}
