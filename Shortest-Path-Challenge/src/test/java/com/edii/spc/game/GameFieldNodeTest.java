package com.edii.spc.game;

import java.util.Random;
import java.util.Set;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Testitapaukset luokalle GameFieldNode.
 */
public class GameFieldNodeTest {
    /**
     * Tarkistaa, että samat tiedot sisältävät solmut ovat .equals()-metodin mukaan samoja, vaikka kyseessä olisi eri instanssit. 
     */
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
    
    /**
     * Tarkista, että .getX()- ja .getY()-metodit palauttavat konstruktorille annetut arvot. 
     */
    @Test
    public void testXAndY() {
        Random random = new Random();
        int x = random.nextInt();
        int y = random.nextInt();
        GameFieldNode node = new GameFieldNode(x, y);
        assertEquals(x, node.getX());
        assertEquals(y, node.getY());
    }
    
    /**
     * Tarkista että .getEdges() palauttaa kaikki solmusta lähtevät kaaret.
     */
    @Test
    public void testGetEdges() {
        for (int i = 3; i < 10; i++) {
            GameField gameField = GameField.generateRandomField(i);
            Set<GameFieldEdge> edges = gameField.getNode(1, 1).getEdges();
            
            Assert.assertEquals(edges.size(), 4);
            Assert.assertTrue(edges.contains(gameField.getNode(1, 1).getUpEdge()));
            Assert.assertTrue(edges.contains(gameField.getNode(1, 1).getLeftEdge()));
            Assert.assertTrue(edges.contains(gameField.getNode(1, 1).getRightEdge()));
            Assert.assertTrue(edges.contains(gameField.getNode(1, 1).getDownEdge()));
            
            edges = gameField.getNode(i - 1, i - 1).getEdges();
            
            Assert.assertEquals(edges.size(), 2);
            Assert.assertTrue(edges.contains(gameField.getNode(i - 1, i - 1).getUpEdge()));
            Assert.assertTrue(edges.contains(gameField.getNode(i - 1, i - 1).getLeftEdge()));
        }
    }
    
    /**
     * Tarkista että .getEdgeTo() palauttaa oikean kaaren, tai null jos oikeaa kaarta ei löydy. 
     */
    @Test
    public void testGetEdgeTo() {
        for (int i = 3; i < 10; i++) {
            GameField gameField = GameField.generateRandomField(i);
            Assert.assertEquals(gameField.getNode(1, 1).getLeftEdge(), gameField.getNode(1, 1).getEdgeTo(gameField.getNode(0, 1)));
            Assert.assertEquals(gameField.getNode(1, 1).getRightEdge(), gameField.getNode(1, 1).getEdgeTo(gameField.getNode(2, 1)));
            Assert.assertEquals(gameField.getNode(1, 1).getUpEdge(), gameField.getNode(1, 1).getEdgeTo(gameField.getNode(1, 0)));
            Assert.assertEquals(gameField.getNode(1, 1).getDownEdge(), gameField.getNode(1, 1).getEdgeTo(gameField.getNode(1, 2)));
            Assert.assertEquals(null, gameField.getNode(1, 1).getEdgeTo(gameField.getNode(i - 1, i - 1)));
        }
    }
}
