/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edii.spc.datastructures;

import com.edii.spc.game.Game;
import com.edii.spc.game.GameField;
import com.edii.spc.game.GameFieldNode;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author edii
 */
public class GameFieldTest {
    @Test
    public void verifyEmptyField() {
        for (int i = 2; i < 10; i++) {
            GameField gameField = new GameField(i);
            int y = 1;
            for (;y < i - 1; y++) {
                int x = 1;
                for (;x < i - 1; x++) {
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
    
    @Test
    public void verifyRandomField() {
        for (int i = 10; i < 20; i++) {
            boolean containsNonZeroHorizontal = false;
            boolean containsNonZeroVertical = false;
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
}
