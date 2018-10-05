package com.edii.spc.game;

import com.edii.spc.datastructures.OwnSet;
import com.edii.spc.datastructures.Pair;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

/**
 * Testitapaukset luokalle GameFieldPath.
 */
public class GameFieldPathTest {
    /**
     * Testaa konstruktorin toiminta, ja että getterit palauttavat oikeat tiedot.
     */
    @Test
    public void testConstructor() {
        for (int i = 1; i < 10; i++) {
            GameFieldNode node1 = new GameFieldNode(i, i + 2);
            GameFieldNode node2 = new GameFieldNode(i + 1, i + 3);
            GameFieldNode node3 = new GameFieldNode(i + 4, i + 6);
            GameFieldEdge edge1 = new GameFieldEdge(new Pair<>(node1, node2), i * 2);
            GameFieldEdge edge2 = new GameFieldEdge(new Pair<>(node2, node3), i * 3);
            GameFieldPath path = new GameFieldPath(node1);
            path.addEdge(edge1);
            Assert.assertEquals(path.getStartNode(), node1);
            Assert.assertEquals(path.getEndNode(), node2);
            Assert.assertEquals(path.getWeight(), edge1.getWeight());
            
            path.addEdge(edge2);
            Assert.assertEquals(path.getWeight(), edge1.getWeight() + edge2.getWeight());
            Assert.assertEquals(path.getEndNode(), node3);
        }
    }
    
    /**
     * Testaa, että .removeLastEdge() -metodi poistaa polun viimeisen kaaren.
     */
    @Test
    public void testRemoveLastEdge() {
        for (int i = 1; i < 10; i++) {
            GameFieldNode node1 = new GameFieldNode(i, i + 2);
            GameFieldNode node2 = new GameFieldNode(i + 1, i + 3);
            GameFieldNode node3 = new GameFieldNode(i + 4, i + 6);
            GameFieldEdge edge1 = new GameFieldEdge(new Pair<>(node1, node2), i * 2);
            GameFieldEdge edge2 = new GameFieldEdge(new Pair<>(node2, node3), i * 3);
            GameFieldPath path = new GameFieldPath(node1);
            path.addEdge(edge1);
            
            path.addEdge(edge2);
            path.removeLastEdge();
            
            Assert.assertEquals(path.getEndNode(), edge1.getNodes().getSecond());
        }
    }
    
    /**
     * Tarkista, ettei polkuun voi lisätä kaarta, jonka alkusolmu ei ole polun sen hetkinen viimeinen solmu.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalEdge() {
        for (int i = 1; i < 10; i++) {
            GameFieldNode node1 = new GameFieldNode(i, i + 2);
            GameFieldNode node2 = new GameFieldNode(i + 1, i + 3);
            GameFieldNode node3 = new GameFieldNode(i + 4, i + 6);
            GameFieldEdge edge1 = new GameFieldEdge(new Pair<>(node1, node2), i * 2);
            GameFieldEdge edge2 = new GameFieldEdge(new Pair<>(node2, node3), i * 3);
            GameFieldEdge edge3 = new GameFieldEdge(new Pair<>(node1, node3), i * 3);
            GameFieldPath path = new GameFieldPath(node1);
            path.addEdge(edge1);
            path.addEdge(edge2);
            path.addEdge(edge3);
        }
    }
    
    /**
     * Tarkista, että .getEdges() palauttaa kaikki polun kaaret oikeassa järjestyksessä.
     */
    @Test
    public void testGetEdges() {
        for (int i = 1; i < 10; i++) {
            GameFieldNode prevNode = new GameFieldNode(i, i + 2);
            GameFieldPath path = new GameFieldPath(prevNode);
            for (int j = 1; j <= i; j++) {
                GameFieldNode node = new GameFieldNode(i + j, i + j + 2);
                GameFieldEdge edge = new GameFieldEdge(new Pair<>(prevNode, node), i * j);
                path.addEdge(edge);
                prevNode = node;
            }
            
            List<GameFieldEdge> edges = path.getEdges();
            Assert.assertEquals(i, edges.size());
            
            Assert.assertEquals(i, edges.get(0).getNodes().getFirst().getX());
            Assert.assertEquals(i + 2, edges.get(0).getNodes().getFirst().getY());
            
            Assert.assertEquals(i + i, edges.get(i - 1).getNodes().getSecond().getX());
            Assert.assertEquals(i + i + 2, edges.get(i - 1).getNodes().getSecond().getY());
        }
    }
    
    /**
     * Tarkista, että .getNodes() palauttaa kaikki polun solmut.
     */
    @Test
    public void testGetNodes() {
        
        for (int i = 1; i < 10; i++) {
            Set<GameFieldNode> addedNodes = new OwnSet<>();
            GameFieldNode prevNode = new GameFieldNode(i, i + 2);
            addedNodes.add(prevNode);
            GameFieldPath path = new GameFieldPath(prevNode);
            for (int j = 1; j <= i; j++) {
                GameFieldNode node = new GameFieldNode(i + j, i + j + 2);
                addedNodes.add(node);
                GameFieldEdge edge = new GameFieldEdge(new Pair<>(prevNode, node), i * j);
                path.addEdge(edge);
                prevNode = node;
            }
            
            Set<GameFieldNode> nodes = path.getNodes();
            Assert.assertEquals(nodes, addedNodes);
        }
    }
    
    /**
     * Tarkista että .reverse() palauttaa saman polun käänteisessä järjestyksessä.
     */
    @Test
    public void testReverse() {
        for (int i = 1; i < 10; i++) {
            GameFieldNode prevNode = new GameFieldNode(i, i + 2);
            GameFieldPath path = new GameFieldPath(prevNode);
            for (int j = 1; j <= i; j++) {
                GameFieldNode node = new GameFieldNode(i + j, i + j + 2);
                GameFieldEdge edge = new GameFieldEdge(new Pair<>(prevNode, node), i * j);
                path.addEdge(edge);
                prevNode = node;
            }
            Assert.assertEquals(path.getEdges().size(), path.reverse().getEdges().size());
            Assert.assertEquals(path.getWeight(), path.reverse().getWeight());
            for (int j = 0; j < path.getEdges().size(); j++) {
                Assert.assertEquals(path.getEdges().get(j).getNodes().getFirst(), path.reverse().getEdges().get(path.getEdges().size() - 1 - j).getNodes().getSecond());
            }
        }
    }
}
