/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edii.spc.datastructures;

import java.util.Random;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 *
 * @author edii
 */
public class PairTest {
    
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
    
    @Test
    public void testInverse() {
        for (int i = 0; i < 10; i++) {
            assertEquals(new Pair<>(i, i + 1).inverse(), new Pair(i + 1, i));
        }
    }
    
    @Test
    public void testEquality() {
        Pair<Integer> pair1 = new Pair<>(1, 2);
        Pair<Integer> pair2 = new Pair<>(1, 2);
        Pair<Integer> pair3 = new Pair<>(3, 4);
        
        assertEquals(pair1, pair2);
        assertFalse(pair1.equals(pair3));
        assertFalse(pair1.equals(null));
        assertFalse(pair1.equals(this));
    }
}
