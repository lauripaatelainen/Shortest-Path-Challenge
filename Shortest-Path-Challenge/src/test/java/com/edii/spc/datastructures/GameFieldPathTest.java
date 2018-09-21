package com.edii.spc.datastructures;

import com.edii.spc.game.GameFieldEdge;
import com.edii.spc.game.GameFieldNode;
import com.edii.spc.game.GameFieldPath;
import org.junit.Assert;
import org.junit.Test;


public class GameFieldPathTest {
    @Test
    public void testConstructor() {
        for (int i = 1; i < 10; i++) {
            GameFieldNode node1 = new GameFieldNode(i, i + 2);
            GameFieldNode node2 = new GameFieldNode(i + 1, i + 3);
            GameFieldNode node3 = new GameFieldNode(i + 4, i + 6);
            GameFieldEdge edge1 = new GameFieldEdge(new Pair<>(node1, node2), i * 2);
            GameFieldEdge edge2 = new GameFieldEdge(new Pair<>(node2, node3), i * 3);
            GameFieldPath path = new GameFieldPath(edge1);
            Assert.assertEquals(path.getStartNode(), node1);
            Assert.assertEquals(path.getEndNode(), node2);
            Assert.assertEquals(path.getWeight(), edge1.getWeight());
            
            path.addEdge(edge2);
            Assert.assertEquals(path.getWeight(), edge1.getWeight() + edge2.getWeight());
            Assert.assertEquals(path.getEndNode(), node3);
        }
    }
}
