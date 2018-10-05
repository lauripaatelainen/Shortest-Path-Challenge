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
    Solver solver;
    
    public SolverTest(Solver solver) {
        this.solver = solver;
    }
    
    @Parameterized.Parameters
    public static Collection solvers() {
        List<Solver[]> solvers = new OwnList<>();
        solvers.add(new Solver[] { new DijkstraSolver() });
        solvers.add(new Solver[] { new BellmanFordSolver() });
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
            
            Solver dijkstra = new DijkstraSolver();
            GameFieldPath shortestPath = dijkstra.solve(gameField);
            Assert.assertEquals(path1.getStartNode(), shortestPath.getStartNode());
            Assert.assertEquals(path1.getEndNode(), shortestPath.getEndNode());
            Assert.assertTrue(shortestPath.getWeight() <= path1.getWeight());
        }
    }
    
    /**
     * Tarkistaa että algoritmin tekemä polku on polku alkusolmusta loppusolmuun, ja että se on painoltaan pienempi kuin yksinkertainen yläreunaa oikealle ja oikeaa reunaa alas kulkeva polku. 
     */
    @Test
    public void testDijkstra2() {
        for (int i = 2; i < 5; i++) {
            GameField gameField = GameField.generateRandomField(i);
            GameFieldPath path2 = new GameFieldPath(gameField.getStart());
            
            for (int j = 0; j < i - 1; j++) {
                path2.addEdge(path2.getEndNode().getRightEdge());
            }
            
            for (int j = 0; j < i - 1; j++) {
                path2.addEdge(path2.getEndNode().getDownEdge());
            }
            
            Solver dijkstra = new DijkstraSolver();
            GameFieldPath shortestPath = dijkstra.solve(gameField);
            Assert.assertEquals(path2.getStartNode(), shortestPath.getStartNode());
            Assert.assertEquals(path2.getEndNode(), shortestPath.getEndNode());
            Assert.assertTrue(shortestPath.getWeight() <= path2.getWeight());
        }
    }
    
    /**
     * Tarkistaa että algoritmin tekemä polku on polku alkusolmusta loppusolmuun, ja että se on painoltaan pienempi kuin yksinkertainen vuorotellen oikealle ja alas kulkeva polku.
     */
    @Test
    public void testDijkstra3() {
        for (int i = 2; i < 5; i++) {
            GameField gameField = GameField.generateRandomField(i);
            GameFieldPath path3 = new GameFieldPath(gameField.getStart());
            
            for (int j = 0; j < i - 1; j++) {
                path3.addEdge(path3.getEndNode().getRightEdge());
                path3.addEdge(path3.getEndNode().getDownEdge());
            }
            
            Solver dijkstra = new DijkstraSolver();
            GameFieldPath shortestPath = dijkstra.solve(gameField);
            Assert.assertEquals(path3.getStartNode(), shortestPath.getStartNode());
            Assert.assertEquals(path3.getEndNode(), shortestPath.getEndNode());
            Assert.assertTrue(shortestPath.getWeight() <= path3.getWeight());
        }
    }
    
    /**
     * Tarkistaa että algoritmin tekemä polku on polku alkusolmusta loppusolmuun, ja että se on painoltaan pienempi kuin yksinkertainen vuorotellen alas ja oikealle kulkeva polku.
     */
    @Test
    public void testDijkstra4() {
        for (int i = 2; i < 5; i++) {
            GameField gameField = GameField.generateRandomField(i);
            GameFieldPath path4 = new GameFieldPath(gameField.getStart());
            
            for (int j = 0; j < i - 1; j++) {
                path4.addEdge(path4.getEndNode().getDownEdge());
                path4.addEdge(path4.getEndNode().getRightEdge());
            }
            
            Solver dijkstra = new DijkstraSolver();
            GameFieldPath shortestPath = dijkstra.solve(gameField);
            Assert.assertEquals(path4.getStartNode(), shortestPath.getStartNode());
            Assert.assertEquals(path4.getEndNode(), shortestPath.getEndNode());
            Assert.assertTrue(shortestPath.getWeight() <= path4.getWeight());
            
            GameFieldPath shortestPath2 = dijkstra.solve(gameField);
            Assert.assertEquals(shortestPath.getWeight(), shortestPath2.getWeight());
        }
    }
}
