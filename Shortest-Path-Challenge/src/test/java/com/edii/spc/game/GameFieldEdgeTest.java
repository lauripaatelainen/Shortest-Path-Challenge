package com.edii.spc.game;

import com.edii.spc.datastructures.Pair;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Testitapaukset luokalle GameFieldEdge.
 */
public class GameFieldEdgeTest {
    /**
     * Testaa GameFieldEdge-luokan instanssien muodostuksen, ja että getNodes()-metodilla haetut solmut täsmäävät konstruktorille annettuihin.
     */
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
    
    /**
     * Tarkistaa, ettei kaaren muodostus negatiivisella painolla -1 onnistu, vaan IllegalArgumentException -exception muodostuu.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeWeight1() {
        GameFieldNode node1 = new GameFieldNode(0, 1);
        GameFieldNode node2 = new GameFieldNode(1, 2);
        GameFieldEdge edge = new GameFieldEdge(new Pair(node1, node2), -1);
    }
    
    /**
     * Tarkistaa, ettei kaaren muodostus negatiivisella painolla -2 onnistu, vaan IllegalArgumentException -exception muodostuu.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeWeight2() {
        GameFieldNode node1 = new GameFieldNode(0, 1);
        GameFieldNode node2 = new GameFieldNode(1, 2);
        GameFieldEdge edge = new GameFieldEdge(new Pair(node1, node2), -2);
    }
    
    /**
     * Tarkistaa, että samat tiedot sisältävät kaaret ovat .equals()-metodin mukaan samoja, vaikka kyseessä olisi eri instanssit. 
     */
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
