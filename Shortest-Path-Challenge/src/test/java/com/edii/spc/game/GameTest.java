package com.edii.spc.game;

import org.junit.Assert;
import org.junit.Test;

/**
 * Testitapaukset luokalle Game.
 */
public class GameTest {
    /**
     * Tarkista, että konstruktori new Game(i) luo oikean kokoisen pelin.
     */
    @Test
    public void testConstructorAndVerifySize() {
        for (int i = 2; i < 100; i++) {
            Game game = new Game(i);
            Assert.assertEquals(i, game.getSize());
            Assert.assertEquals(i, game.getGameField().getSize());
        }
    }
    
    /**
     * Tarkista ettei yhden kokoista peliä voi luoda.
     */
    @Test(expected = IllegalArgumentException.class)
    public void illegalSizeOne() {
        new Game(1);
    }
    
    /**
     * Tarkista ettei nollan kokoista peliä voi luoda.
     */
    @Test(expected = IllegalArgumentException.class)
    public void illegalSizeZero() {
        new Game(0);
    }
    
    /**
     * Tarkista ettei negatiivisen kokoista peliä voi luoda.
     */
    @Test(expected = IllegalArgumentException.class)
    public void illegalSizeNegative() {
        new Game(-1);
    }
}
