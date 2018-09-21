/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edii.spc.datastructures;

import com.edii.spc.game.GameFieldNode;
import java.util.Random;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author edii
 */
public class GameFieldNodeTest {
    @Test
    public void testEquality() {
        GameFieldNode node1 = new GameFieldNode(1, 1);
        GameFieldNode node2 = new GameFieldNode(2, 1);
        GameFieldNode node3 = new GameFieldNode(3, 1);
        GameFieldNode node4 = new GameFieldNode(4, 1);
        GameFieldNode node5 = new GameFieldNode(1, 1);
        GameFieldNode node6 = new GameFieldNode(1, 2);
        GameFieldNode node7 = new GameFieldNode(1, 3);
        GameFieldNode node8 = new GameFieldNode(1, 4);
        
        assertEquals(node1, node5);
        Assert.assertFalse(node1.equals(node2));
        Assert.assertFalse(node1.equals(null));
        Assert.assertFalse(node1.equals(this));
    }
    
    @Test
    public void testXAndY() {
        Random random = new Random();
        int x = random.nextInt();
        int y = random.nextInt();
        GameFieldNode node = new GameFieldNode(x, y);
        assertEquals(x, node.getX());
        assertEquals(y, node.getY());
    }
}
