package com.edii.spc.game.solvers;

import com.edii.spc.datastructures.Pair;
import com.edii.spc.game.GameField;
import com.edii.spc.game.GameFieldEdge;
import com.edii.spc.game.GameFieldNode;
import org.junit.Test;

/**
 * Testitapaukset BellmanFordSolver-luokalle.
 * Toteutetaan testit sellaisiin tapauksiin, joita kaikkia solvereita koskeva SolverTest-luokka ei kata.
 */
public class BellmanFordSolverTest {
    /**
     * Tarkista, että kun BellmanFordSolveria kutsutaan pelikentällä, jossa
     * on negatiivinen sykli, syntyy IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCycle() throws InterruptedException {
        GameField field = GameField.generateRandomField(5);
        GameFieldNode node = field.getNode(2, 2);
        GameFieldNode secondNode = node.getRightEdge().getNodes().getSecond();
        GameFieldEdge edge = new GameFieldEdge(new Pair<>(node, secondNode), -50);
        node.setRightEdge(edge);
        secondNode.setLeftEdge(edge.inverse());
        
        Solver solver = new BellmanFordSolver();
        solver.solve(field);
    }
}
