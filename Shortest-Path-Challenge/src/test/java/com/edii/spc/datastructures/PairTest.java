package com.edii.spc.datastructures;

import java.util.Random;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Testitapaukset luokalle Pair.
 */
public class PairTest {
    /**
     * Tarkista että konstruktori toimii oikein, ja että getterit getFirst() ja getSecond() palauttavat konstruktorille annetut arvot.
     */
    @Test
    public void testConstructorAndGetters() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            Integer a = random.nextInt();
            Integer b = random.nextInt();
            Pair pair = new Pair(a, b);
            Assert.assertEquals(a, pair.getFirst());
            Assert.assertEquals(b, pair.getSecond());
        }
    }
    
    /**
     * Tarkista, että .inverse() metodi palauttaa saman parin käänteisessä järjestyksessä.
     */
    @Test
    public void testInverse() {
        for (int i = 0; i < 10; i++) {
            assertEquals(new Pair<>(i, i + 1).inverse(), new Pair(i + 1, i));
        }
    }
    
    /**
     * Tarkista, että .equals() metodi tulkitsee samanlaiset parit oikein.
     */
    @Test
    public void testEquality() {
        Pair<Integer> pair1 = new Pair<>(1, 2);
        Pair<Integer> pair2 = new Pair<>(1, 2);
        Pair<Integer> pair3 = new Pair<>(3, 4);
        
        assertEquals(pair1, pair1);
        assertEquals(pair1, pair2);
        assertFalse(pair1.equals(pair3));
        assertFalse(pair1.equals(null));
        assertFalse(pair1.equals(this));
    }
}
