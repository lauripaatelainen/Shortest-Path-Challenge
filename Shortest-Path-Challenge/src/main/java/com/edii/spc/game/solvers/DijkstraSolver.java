package com.edii.spc.game.solvers;

import com.edii.spc.datastructures.MinHeap;
import com.edii.spc.datastructures.OwnMap;
import com.edii.spc.game.GameField;
import com.edii.spc.game.GameFieldEdge;
import com.edii.spc.game.GameFieldNode;
import com.edii.spc.game.GameFieldPath;
import java.util.Comparator;
import java.util.Map;

/**
 * Ratkaisee pelikentän Dijkstran algoritmilla.
 */
public class DijkstraSolver implements Solver {
    private final Comparator<GameFieldNode> comparator = new Comparator<GameFieldNode>() {
        @Override
        public int compare(GameFieldNode node1, GameFieldNode node2) {
            return distance.get(node1) - distance.get(node2);
        }
    };
    
    private final Map<GameFieldNode, Integer> distance = new OwnMap<>();
    private final Map<GameFieldNode, GameFieldEdge> edgeToPrevious = new OwnMap<>();
    
    @Override
    public GameFieldPath solve(GameField field) {
        for (GameFieldNode node : field.getNodes()) {
            distance.put(node, Integer.MAX_VALUE);
        }
        
        distance.put(field.getStart(), 0);
        MinHeap<GameFieldNode> queue = new MinHeap<>(field.getNodes(), comparator);
        
        while (!queue.isEmpty()) {
            GameFieldNode u = queue.extractMin();
            for (GameFieldEdge edge : u.getEdges()) {
                if (distance.get(edge.getNodes().getSecond()) > distance.get(u) + edge.getWeight()) {
                    edgeToPrevious.put(edge.getNodes().getSecond(), edge.inverse());
                    distance.put(edge.getNodes().getSecond(), distance.get(u) + edge.getWeight());
                    queue.decreaseKey(edge.getNodes().getSecond());
                }
            }
        }
        
        return formPath(field);
    }
    
    private GameFieldPath formPath(GameField field) {
        GameFieldPath path = new GameFieldPath(field.getFinish());
        while (!path.getEndNode().equals(field.getStart())) {
            path.addEdge(edgeToPrevious.get(path.getEndNode()));
        }
        return path.reverse();
    }
}
