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
 * Ratkaisee pelikentän A* algoritmilla.
 */
public class AStarSolver implements Solver {
    private final Comparator<GameFieldNode> comparator = new Comparator<GameFieldNode>() {
        @Override
        public int compare(GameFieldNode node1, GameFieldNode node2) {
            return fScore.get(node1) - fScore.get(node2);
        }
    };
    
    private GameField field;
    private Set<GameFieldNode> nodes;
    private GameFieldNode start;
    private GameFieldNode goal;
    private Set<GameFieldNode> closedSet;
    private MinHeap<GameFieldNode> openSet;
    private Map<GameFieldNode, GameFieldEdge> edgeToPrevious;
    private Map<GameFieldNode, Integer> gScore;
    private Map<GameFieldNode, Integer> fScore;
    
    private int heuristicCostEstimate(GameFieldNode start, GameFieldNode goal) {
        if (start.equals(goal)) {
            return 0;
        }
        
        int startMinEdge = Integer.MAX_VALUE;
        int edgeToGoal = Integer.MAX_VALUE;
        for (GameFieldEdge edge : start.getEdges()) {
            startMinEdge = Math.min(startMinEdge, edge.getWeight());
            if (edge.getNodes().getSecond().equals(goal)) {
                edgeToGoal = edge.getWeight();
            }
        }
        
        int goalMinEdge = Integer.MAX_VALUE;
        for (GameFieldEdge edge : goal.getEdges()) {
            goalMinEdge = Math.min(goalMinEdge, edge.getWeight());
        }
        return Math.min(startMinEdge + goalMinEdge, edgeToGoal);
    }
    
    private void init(GameField field) {
        this.field = field;
        this.nodes = field.getNodes();
        this.start = field.getStart();
        this.goal = field.getFinish();
        
        this.gScore = new OwnMap<>();
        for (GameFieldNode node : this.nodes) {
            this.gScore.put(node, Integer.MAX_VALUE);
        }
        this.gScore.put(this.start, 0);
        
        this.fScore = new OwnMap<>();
        for (GameFieldNode node : this.nodes) {
            this.fScore.put(node, Integer.MAX_VALUE);
        }
        this.fScore.put(this.start, heuristicCostEstimate(this.start, this.goal));
        
        this.closedSet = new OwnSet<>();
        
        this.openSet = new MinHeap<>(comparator);
        this.openSet.add(this.start);
        
        this.edgeToPrevious = new OwnMap<>();
    }

    @Override
    public GameFieldPath solve(GameField field) {
        init(field);
        while (!openSet.isEmpty()) {
            GameFieldNode current = openSet.extractMin();
            
            if (current.equals(goal)) {
                break;
            }

            closedSet.add(current);

            for (GameFieldEdge edge : current.getEdges()) {
                GameFieldNode neighbor = edge.getNodes().getSecond();
                if (closedSet.contains(neighbor)) {
                    continue;
                }
                
                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                }
                
                int tentative_gScore = gScore.get(current) + edge.getWeight(); //dist_between(current, neighbor);

                if (gScore.get(current) == Integer.MAX_VALUE || tentative_gScore >= gScore.get(neighbor)) {
                    continue;
                }

                edgeToPrevious.put(neighbor, edge.inverse());
                gScore.put(neighbor, tentative_gScore);
                fScore.put(neighbor, tentative_gScore + heuristicCostEstimate(neighbor, goal));
                openSet.decreaseKey(neighbor);
            }
        }
        
        return formPath();
    }
    
    private GameFieldPath formPath() {
        GameFieldPath path = new GameFieldPath(field.getFinish());
        while (!path.getEndNode().equals(field.getStart())) {
            path.addEdge(edgeToPrevious.get(path.getEndNode()));
        }
        return path.reverse();
    }
}
