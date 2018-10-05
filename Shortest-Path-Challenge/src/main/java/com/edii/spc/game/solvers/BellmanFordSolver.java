package com.edii.spc.game.solvers;

import com.edii.spc.datastructures.OwnMap;
import com.edii.spc.game.GameField;
import com.edii.spc.game.GameFieldEdge;
import com.edii.spc.game.GameFieldNode;
import com.edii.spc.game.GameFieldPath;
import java.util.Map;
import java.util.Set;

/**
 * Ratkaisee pelikentän Bellman-Ford algoritmilla.
 */
public class BellmanFordSolver implements Solver {

    private Map<GameFieldNode, Integer> distance = new OwnMap<>();
    private Map<GameFieldNode, GameFieldEdge> edgeToPrevious = new OwnMap<>();
    private Set<GameFieldNode> nodes;
    private Set<GameFieldEdge> edges;

    public BellmanFordSolver() {
    }

    /**
     * Asettaa alkuun kaikkien solmujen etäisyydeksi mahdollisimman suuren luvun
     * ja edeltäjäksi null. Lähtösolmun etäisyydeksi annetaan nolla.
     */
    private void init(GameField field) {
        nodes = field.getNodes();
        edges = field.getEdges();
        for (GameFieldNode node : nodes) {
            distance.put(node, Integer.MAX_VALUE);
            edgeToPrevious.put(node, null);
        }

        distance.put(field.getStart(), 0);
    }

    @Override
    public GameFieldPath solve(GameField field) {
        init(field);
        for (int i = 1; i < nodes.size(); i++) {
            for (GameFieldEdge edge : edges) {
                relax(edge);
            }
        }

        for (GameFieldEdge edge : edges) {
            if (distance.get(edge.getNodes().getFirst()) + edge.getWeight() < distance.get(edge.getNodes().getSecond())) {
                throw new IllegalStateException("Pelikentässä negatiivinen sykli");
            }
        }

        return formPath(field);
    }

    private void relax(GameFieldEdge edge) {
        if (distance.get(edge.getNodes().getFirst()) != Integer.MAX_VALUE && distance.get(edge.getNodes().getFirst()) + edge.getWeight() < distance.get(edge.getNodes().getSecond())) {
            distance.put(edge.getNodes().getSecond(), distance.get(edge.getNodes().getFirst()) + edge.getWeight());
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
}
