package com.edii.spc.game.solvers;

import com.edii.spc.datastructures.MinHeap;
import com.edii.spc.datastructures.OwnMap;
import com.edii.spc.datastructures.OwnSet;
import com.edii.spc.game.GameField;
import com.edii.spc.game.GameFieldEdge;
import com.edii.spc.game.GameFieldNode;
import com.edii.spc.game.GameFieldPath;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

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
        distance.clear();
        edgeToPrevious.clear();
        
        GameFieldNode start = field.getStart();
        GameFieldNode finish = field.getFinish();
        
        for (GameFieldNode node : field.getNodes()) {
            distance.put(node, Integer.MAX_VALUE);
        }
        
        distance.put(start, 0);
        
        MinHeap<GameFieldNode> queue = new MinHeap<>(comparator);
        queue.add(field.getNodes());
        
        Set<GameFieldNode> handled = new OwnSet<>();
        
        while (!queue.isEmpty()) {
            GameFieldNode u = queue.extractMin();
            handled.add(u);
            for (GameFieldEdge edge : u.getEdges()) {
                if (distance.get(edge.getNodes().getSecond()) > distance.get(u) + edge.getWeight()) {
                    edgeToPrevious.put(edge.getNodes().getSecond(), edge.inverse());
                    distance.put(edge.getNodes().getSecond(), distance.get(u) + edge.getWeight());
                    queue.decreaseKey(edge.getNodes().getSecond());
                }
            }
        }
        
        GameFieldPath path = new GameFieldPath();
        GameFieldNode node = finish;
        while (!node.equals(start)) {
            path.addEdge(edgeToPrevious.get(node));
            node = edgeToPrevious.get(node).getNodes().getSecond();
        }
        
        return path.reverse();
    }
}
