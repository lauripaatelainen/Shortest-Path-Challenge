package com.edii.spc.perf;

import com.edii.spc.game.GameField;
import com.edii.spc.game.solvers.DijkstraSolver;
import com.edii.spc.game.solvers.Solver;

/**
 * Pääohjelma, jolla luodaan suorituskykyraportit toteutetuista tietorakenteista ja algoritmeista.
 */
public class PerformanceTest {
    
    /**
     * Pääohjelma, jolla luodaan suorituskykyraportit toteutetuista tietorakenteista ja algoritmeista.
     * 
     * @param args Komentoriviltä annetut parametrit.
     */
    public static void main(String[] args) {
        System.out.println("Mitataan Dijkstran algoritmin suorituskykyä pelikentän ko'oilla 2 - 30");
        Solver dijkstra = new DijkstraSolver();
        for (int i = 100; i <= 10000; i *= 2) {
            GameField field = GameField.generateRandomField(i);
            long startTime = System.currentTimeMillis();
            dijkstra.solve(field);
            long endTime = System.currentTimeMillis();
            long time = endTime - startTime;
            System.out.printf("%d\t%d\t%.3f\n", i, i*i, (endTime - startTime) * 0.001f);
        }
    }
}
