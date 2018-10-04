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
     * Tulostaa lukemat konsoliin, josta tarvittavat tiedot poimitaan käsin raporttiin.
     * 
     * @param args Komentoriviltä annetut parametrit.
     */
    public static void main(String[] args) {
        /*
        System.out.println("Mitataan MinHeap tietorakenteen suorituskykyä");
        int minHeapSize = 100000000;
        MinHeap<String> minHeap = new MinHeap<>(minHeapSize, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return a.compareTo(b);
            }
        });
        
        
        
        for (int i = 0; i < minHeapSize; i++) {
            long start = System.currentTimeMillis();
            
            minHeap.insert(java.util.UUID.randomUUID().toString());
            
            long end = System.currentTimeMillis();
            if (i % 100000 == 0 || end - start > 10) {
                System.out.printf("%d\t%d\n", i, end - start);
            }
        }
        */
        
        
        System.out.println("Mitataan Dijkstran algoritmin suorituskykyä pelikentän ko'oilla 2 - 30");
        Solver dijkstra = new DijkstraSolver();
        for (int i = 100; i <= 10000; i *= 2) {
            GameField field = GameField.generateRandomField(i);
            long startTime = System.currentTimeMillis();
            dijkstra.solve(field);
            long endTime = System.currentTimeMillis();
            long time = endTime - startTime;
            System.out.printf("%d\t%d\t%.3f\n", i, i * i, (endTime - startTime) * 0.001f);
        }
        
    }
}
