package com.edii.spc.game.solvers;

import com.edii.spc.datastructures.OwnMap;
import com.edii.spc.game.GameField;
import com.edii.spc.game.GameFieldEdge;
import com.edii.spc.game.GameFieldNode;
import com.edii.spc.game.GameFieldPath;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Ratkaisee pelikentän Bellman-Ford algoritmilla.
 */
public class BellmanFordSolver implements Solver {
    private volatile boolean interrupted = false;
    private Map<GameFieldNode, Integer> distance;
    private Map<GameFieldNode, GameFieldEdge> edgeToPrevious;
    private Set<GameFieldNode> nodes;
    private Set<GameFieldEdge> edges;

    /**
     * Asettaa alkuun kaikkien solmujen etäisyydeksi mahdollisimman suuren luvun
     * ja edeltäjäksi null. Lähtösolmun etäisyydeksi annetaan nolla.
     */
    private void init(GameField field) {
        distance = new OwnMap<>();
        edgeToPrevious = new OwnMap<>();
        
        nodes = field.getNodes();
        edges = field.getEdges();
        for (GameFieldNode node : nodes) {
            distance.put(node, Integer.MAX_VALUE);
            edgeToPrevious.put(node, null);
        }

        distance.put(field.getStart(), 0);
    }
 
    @Override
    public GameFieldPath solve(GameField field) throws InterruptedException {
        init(field);
        for (int i = 1; !interrupted && i < nodes.size(); i++) {
            Iterator<GameFieldEdge> iterator = edges.iterator();
            while (!interrupted && iterator.hasNext()) {
                relax(iterator.next());
            }
        }
        
        if (interrupted) {
            throw new InterruptedException();
        } else {
            for (GameFieldEdge edge : edges) {
                if (distance.get(edge.getNodes().getFirst()) + edge.getWeight() < distance.get(edge.getNodes().getSecond())) {
                    throw new IllegalArgumentException("Pelikentässä negatiivinen sykli");
                }
            }
            return formPath(field);
        }
    }

    private void relax(GameFieldEdge edge) {
        int firstDistance = distance.get(edge.getNodes().getFirst());
        int secondDistance = distance.get(edge.getNodes().getSecond());
        if (firstDistance != Integer.MAX_VALUE && firstDistance + edge.getWeight() < secondDistance) {
            distance.put(edge.getNodes().getSecond(), firstDistance + edge.getWeight());
            edgeToPrevious.put(edge.getNodes().getSecond(), edge.inverse());
        }
    }

    private GameFieldPath formPath(GameField field) {
        GameFieldPath path = new GameFieldPath(field.getFinish());
        while (!path.getEndNode().equals(field.getStart())) {
            path.addEdge(edgeToPrevious.get(path.getEndNode()));
        }
        return path.reverse();
    }

    @Override
    public void interrupt() {
        interrupted = true;
    }
}
