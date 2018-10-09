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
    
    private GameField field;
    private Map<GameFieldNode, Integer> distance;
    private Map<GameFieldNode, GameFieldEdge> edgeToPrevious;
    private MinHeap<GameFieldNode> queue;
    
    private void init(GameField field) {
        this.field = field;
        this.distance = new OwnMap<>();
        this.edgeToPrevious = new OwnMap<>();
        
        for (GameFieldNode node : field.getNodes()) {
            distance.put(node, Integer.MAX_VALUE);
        }
        
        distance.put(field.getStart(), 0);
        queue = new MinHeap<>(field.getNodes(), comparator);
    }
    
    private GameFieldPath formPath() {
        GameFieldPath path = new GameFieldPath(field.getFinish());
        while (!path.getEndNode().equals(field.getStart())) {
            path.addEdge(edgeToPrevious.get(path.getEndNode()));
        }
        return path.reverse();
    }
    
    @Override
    public GameFieldPath solve(GameField field) {
        init(field);
        
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
        
        return formPath();
    }
}
