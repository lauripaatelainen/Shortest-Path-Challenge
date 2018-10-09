package com.edii.spc.game.solvers;

import com.edii.spc.datastructures.OwnList;
import com.edii.spc.game.GameField;
import com.edii.spc.game.GameFieldPath;
import java.util.Collection;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tekee ristiintarkistukset eri algoritmien välillä, ja varmistaa että kaikki palauttavat saman tuloksen lyhimmäksi poluksi.
 */
public class SolverCrossCheck {
    private Collection<Solver> solvers;
    
    /**
     * Alustaa testattavat solverit. 
     */
    @Before
    public void initSolvers() {
        solvers = new OwnList<>();
        solvers.add(new DijkstraSolver());
        solvers.add(new BellmanFordSolver());
        solvers.add(new AStarSolver());
    }
    
    /**
     * Luo erikokoisia pelikenttiä, ja tarkistaa että kaikilla solvereilla ratkaistun lyhimmän polun paino on sama.
     */
    @Test
    public void crossCheckAllSolvers() {
        for (int i = 2; i <= 20; i++) {
            GameField field = GameField.generateRandomField(i);
            GameFieldPath lastSolution = null;
            for (Solver solver : solvers) {
                GameFieldPath solution = solver.solve(field);
                if (lastSolution == null) {
                    lastSolution = solution;
                } else {
                    Assert.assertEquals(lastSolution.getWeight(), solution.getWeight());
                    lastSolution = solution;
                }
            }
        }
    }
}
