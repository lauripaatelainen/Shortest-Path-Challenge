package com.edii.spc.game;

import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

/**
 * Testitapaukset luokalle GameField.
 */
public class GameFieldTest {
    /**
     * Tarkista että GameField(int) -konstruktori luo oikean kokoisen tyhjän kentän.
     */
    @Test
    public void verifyEmptyField() {
        for (int i = 2; i < 10; i++) {
            GameField gameField = new GameField(i);
            int y = 1;
            for (; y < i - 1; y++) {
                int x = 1;
                for (; x < i - 1; x++) {
                    GameFieldNode node = gameField.getNode(x, y);
                    Assert.assertEquals(0, node.getRightEdge().getWeight());
                    Assert.assertEquals(0, node.getDownEdge().getWeight());
                    Assert.assertEquals(0, node.getLeftEdge().getWeight());
                    Assert.assertEquals(0, node.getUpEdge().getWeight());
                    
                    Assert.assertEquals(node.getRightEdge().inverse(), node.getRightEdge().getNodes().getSecond().getLeftEdge());
                }
                
                Assert.assertNull(gameField.getNode(x, y).getRightEdge());
            }
        }
    }
    
    /**
     * Tarkista että GameField.generateRandomField(i) luo oikean kokoisen satunnaisen kentän.
     */
    @Test
    public void verifyRandomField() {
        for (int i = 10; i < 20; i++) {
            boolean containsNonZeroHorizontal = false, containsNonZeroVertical = false;
            GameField gameField = GameField.generateRandomField(i);
            for (int y = 0; (!containsNonZeroVertical || !containsNonZeroHorizontal) && y < i - 1; y++) {
                for (int x = 0; (!containsNonZeroVertical || !containsNonZeroHorizontal) && x < i - 1; x++) {
                    if (gameField.getNode(x, y).getRightEdge().getWeight() != 0) {
                        containsNonZeroHorizontal = true;
                    }
                    if (gameField.getNode(x, y).getDownEdge().getWeight() != 0) {
                        containsNonZeroVertical = true;
                    }
                    Assert.assertEquals(gameField.getNode(x, y).getRightEdge().inverse(), gameField.getNode(x, y).getRightEdge().getNodes().getSecond().getLeftEdge());
                    Assert.assertEquals(gameField.getNode(x, y).getDownEdge().inverse(), gameField.getNode(x, y).getDownEdge().getNodes().getSecond().getUpEdge());
                }
            }
            Assert.assertTrue(containsNonZeroHorizontal);
            Assert.assertTrue(containsNonZeroVertical);
        }
    }
    
    /**
     * Tarkista, että .getNodes() palauttaa kaikki pelikentän solmut.
     */
    @Test
    public void testGetNodes() {
        for (int i = 10; i < 20; i++) {
            GameField gameField = GameField.generateRandomField(i);
            int count = 0;
            Set<GameFieldNode> nodes = gameField.getNodes();
            Assert.assertEquals(nodes.size(), gameField.getSize() * gameField.getSize());
            
            for (int y = 0; y < i; y++) {
                for (int x = 0; x < i; x++) {
                    Assert.assertTrue(nodes.contains(gameField.getNode(x, y)));
                }
            }
        }
    }
    
    /**
     * Tarkista että .getStart()- ja .getFinish() -metodit palauttavat oikeat solmut.
     */
    @Test
    public void verifyStartAndFinish() {
        for (int i = 10; i < 20; i++) {
            GameField gameField = GameField.generateRandomField(i);
            Assert.assertSame(gameField.getStart(), gameField.getNode(0, 0));
            Assert.assertSame(gameField.getFinish(), gameField.getNode(i - 1, i - 1));
        }
    }
}
