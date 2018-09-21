/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edii.spc.datastructures;

import com.edii.spc.game.GameFieldEdge;
import com.edii.spc.game.GameFieldNode;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author edii
 */
public class GameFieldEdgeTest {
    @Test
    public void testEdgeConstructorAndGetter() {
        for (int i = 0; i < 10; i++) {
            GameFieldNode node1 = new GameFieldNode(0, i);
            GameFieldNode node2 = new GameFieldNode(1 + i, i * 2);
            GameFieldEdge edge = new GameFieldEdge(new Pair(node1, node2), i);
            Pair<GameFieldNode> nodes = edge.getNodes();
            
            Assert.assertEquals(i, edge.getWeight());
            Assert.assertSame(nodes.getFirst(), node1);
            Assert.assertSame(nodes.getSecond(), node2);
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeWeight1() {
        GameFieldNode node1 = new GameFieldNode(0, 1);
        GameFieldNode node2 = new GameFieldNode(1, 2);
        GameFieldEdge edge = new GameFieldEdge(new Pair(node1, node2), -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeWeight2() {
        GameFieldNode node1 = new GameFieldNode(0, 1);
        GameFieldNode node2 = new GameFieldNode(1, 2);
        GameFieldEdge edge = new GameFieldEdge(new Pair(node1, node2), -2);
    }
    @Test
    public void testEquality() {
        GameFieldNode node1 = new GameFieldNode(1, 1);
        GameFieldNode node2 = new GameFieldNode(2, 1);
        GameFieldNode node3 = new GameFieldNode(3, 1);
        GameFieldNode node4 = new GameFieldNode(4, 1);
        GameFieldEdge edge1 = new GameFieldEdge(new Pair<>(node1, node2), 1);
        GameFieldEdge edge2 = new GameFieldEdge(new Pair<>(node1, node2), 1);
        GameFieldEdge edge3 = new GameFieldEdge(new Pair<>(node1, node3), 1);
        GameFieldEdge edge4 = new GameFieldEdge(new Pair<>(node1, node3), 2);
        
        assertEquals(edge1, edge2);
        Assert.assertFalse(edge1.equals(edge3));
        Assert.assertFalse(edge3.equals(edge4));
        Assert.assertFalse(edge1.equals(null));
        Assert.assertFalse(edge1.equals(this));
    }
}
