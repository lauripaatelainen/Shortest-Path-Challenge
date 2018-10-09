package com.edii.spc.game.solvers;

import com.edii.spc.datastructures.OwnList;
import com.edii.spc.game.GameField;
import com.edii.spc.game.GameFieldPath;
import java.util.Collection;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Testitapaukset solvereille. 
 * Käytetään parametrointia siten, että jokainen testitapaus suoritetaan jokaiselle eri solverille. 
 */
@RunWith(Parameterized.class)
public class SolverTest {
    private final Solver solver;
    
    /**
     * Luo testiluokan annetulla solverilla. 
     * 
     * @param solver Solver, jota testataan.
     */
    public SolverTest(Solver solver) {
        this.solver = solver;
    }
    
    /**
     * Parametrin testiluokalle. 
     * Luo kaikista toteutetuista Solvereista oliot ja antaa ne parametreiksi luokalle.
     * Tällä tavalla kaikki tässä testiluokassa toteutetut testit ajetaan automaattisesti eri solvereille. 
     * 
     * @return Palauttaa testiluokan parametrit, eli useita eri Solver-luokan toteutuksia.
     */
    @Parameterized.Parameters
    public static Collection solvers() {
        List<Solver[]> solvers = new OwnList<>();
        solvers.add(new Solver[] { new DijkstraSolver() });
        solvers.add(new Solver[] { new BellmanFordSolver() });
        solvers.add(new Solver[] { new AStarSolver() });
        return solvers;
    }
    
    /**
     * Tarkistaa että algoritmin tekemä polku on polku alkusolmusta loppusolmuun, ja että se on painoltaan pienempi kuin yksinkertainen vasenta laitaa alas ja alareunaa oikealle kulkeva polku. 
     */
    @Test
    public void testSolver1() {
        for (int i = 2; i < 5; i++) {
            GameField gameField = GameField.generateRandomField(i);
            GameFieldPath path1 = new GameFieldPath(gameField.getStart());
            
            for (int j = 0; j < i - 1; j++) {
                path1.addEdge(path1.getEndNode().getDownEdge());
            }
            
            for (int j = 0; j < i - 1; j++) {
                path1.addEdge(path1.getEndNode().getRightEdge());
            }
            
            GameFieldPath shortestPath = solver.solve(gameField);
            Assert.assertEquals(path1.getStartNode(), shortestPath.getStartNode());
            Assert.assertEquals(path1.getEndNode(), shortestPath.getEndNode());
            Assert.assertTrue(
                    String.format("%d > %d", shortestPath.getWeight(), path1.getWeight()),
                    shortestPath.getWeight() <= path1.getWeight()
            );
        }
    }
    
    /**
     * Tarkistaa että algoritmin tekemä polku on polku alkusolmusta loppusolmuun, ja että se on painoltaan pienempi kuin yksinkertainen yläreunaa oikealle ja oikeaa reunaa alas kulkeva polku. 
     */
    @Test
    public void testSolver2() {
        for (int i = 2; i < 5; i++) {
            GameField gameField = GameField.generateRandomField(i);
            GameFieldPath path2 = new GameFieldPath(gameField.getStart());
            
            for (int j = 0; j < i - 1; j++) {
                path2.addEdge(path2.getEndNode().getRightEdge());
            }
            
            for (int j = 0; j < i - 1; j++) {
                path2.addEdge(path2.getEndNode().getDownEdge());
            }
            
            GameFieldPath shortestPath = solver.solve(gameField);
            Assert.assertEquals(path2.getStartNode(), shortestPath.getStartNode());
            Assert.assertEquals(path2.getEndNode(), shortestPath.getEndNode());
            Assert.assertTrue(
                    String.format("%d > %d", shortestPath.getWeight(), path2.getWeight()),
                    shortestPath.getWeight() <= path2.getWeight()
            );
        }
    }
    
    /**
     * Tarkistaa että algoritmin tekemä polku on polku alkusolmusta loppusolmuun, ja että se on painoltaan pienempi kuin yksinkertainen vuorotellen oikealle ja alas kulkeva polku.
     */
    @Test
    public void testSolver3() {
        for (int i = 2; i < 5; i++) {
            GameField gameField = GameField.generateRandomField(i);
            GameFieldPath path3 = new GameFieldPath(gameField.getStart());
            
            for (int j = 0; j < i - 1; j++) {
                path3.addEdge(path3.getEndNode().getRightEdge());
                path3.addEdge(path3.getEndNode().getDownEdge());
            }
            
            GameFieldPath shortestPath = solver.solve(gameField);
            Assert.assertEquals(path3.getStartNode(), shortestPath.getStartNode());
            Assert.assertEquals(path3.getEndNode(), shortestPath.getEndNode());
            Assert.assertTrue(
                    String.format("%d > %d", shortestPath.getWeight(), path3.getWeight()),
                    shortestPath.getWeight() <= path3.getWeight()
            );
        }
    }
    
    /**
     * Tarkistaa että algoritmin tekemä polku on polku alkusolmusta loppusolmuun, ja että se on painoltaan pienempi kuin yksinkertainen vuorotellen alas ja oikealle kulkeva polku.
     */
    @Test
    public void testSolver4() {
        for (int i = 2; i < 5; i++) {
            GameField gameField = GameField.generateRandomField(i);
            GameFieldPath path4 = new GameFieldPath(gameField.getStart());
            
            for (int j = 0; j < i - 1; j++) {
                path4.addEdge(path4.getEndNode().getDownEdge());
                path4.addEdge(path4.getEndNode().getRightEdge());
            }
            
            GameFieldPath shortestPath = solver.solve(gameField);
            Assert.assertEquals(path4.getStartNode(), shortestPath.getStartNode());
            Assert.assertEquals(path4.getEndNode(), shortestPath.getEndNode());
            Assert.assertTrue(
                    String.format("%d > %d", shortestPath.getWeight(), path4.getWeight()),
                    shortestPath.getWeight() <= path4.getWeight()
            );
            
            GameFieldPath shortestPath2 = solver.solve(gameField);
            Assert.assertEquals(shortestPath.getWeight(), shortestPath2.getWeight());
        }
    }
}
