package com.edii.spc.perf;

import com.edii.spc.game.GameField;
import com.edii.spc.game.solvers.AStarSolver;
import com.edii.spc.game.solvers.BellmanFordSolver;
import com.edii.spc.game.solvers.DijkstraSolver;
import com.edii.spc.ui.TextUtil;
import java.util.Iterator;

/**
 * Pääohjelma, jolla luodaan suorituskykyraportit toteutetuista tietorakenteista ja algoritmeista.
 */
public class PerformanceTest {
    private static void printTestResultsHeader(TestResult testResults) {
        System.out.printf("Näytetään testin '%s' tulokset.\n", testResults.getName());
    }
    
    private static void printTestResultsColumnHeaders(TestResult results, int colWidth) {
        String[] columnNames = results.getColumnNames();
        for (int i = 0; i < columnNames.length; i++) {
            if (i != 0) {
                System.out.printf("| ");
            }
            System.out.printf(TextUtil.rightPad(columnNames[i], colWidth, ' '));
        }
        
        System.out.printf("| ");
        System.out.printf(TextUtil.rightPad("Kesto", colWidth, ' '));
        
        System.out.printf("| ");
        System.out.printf(TextUtil.rightPad("Keston kerroin", colWidth, ' '));
        
        System.out.printf("\n");
        
        for (int i = 0; i <= columnNames.length + 1; i++) {
            if (i != 0) {
                System.out.printf("|-");
            }
            System.out.printf(TextUtil.rightPad("", colWidth, '-'));
        }
        
        System.out.printf("\n");
    } 
    
    private static void printTestResultsRows(TestResult results, int colWidth) {
        TestResult.Type[] columnTypes = results.getColumnTypes();
        
        for (int row = 0; row < results.getRowCount(); row++) {
            for (int col = 0; col < columnTypes.length; col++) {
                if (col != 0) {
                    System.out.printf("| ");
                }
                
                String strVal;
                switch (columnTypes[col]) {
                    case String:
                        strVal = results.getStringValue(row, col);
                        break;
                    case Integer:
                        strVal = Integer.toString(results.getIntValue(row, col));
                        break;
                    case Float:
                        strVal = String.format("%.5f", results.getFloatValue(row, col));
                        break;
                    default:
                        strVal = "n/a";
                        break;
                }
                
                System.out.printf(TextUtil.rightPad(strVal, colWidth, ' '));
            }
            
            if (results.getDuration(row) == TestResult.TIMEOUT) {
                System.out.printf("| %s", TextUtil.rightPad("timeout", colWidth, ' '));
            } else {
                System.out.printf("| %s", TextUtil.rightPad(String.format("%.5f", ((float) results.getDuration(row)) / 1000000000.0f), colWidth, ' '));
            }
            
            if (row == 0 || results.getDuration(row) == TestResult.TIMEOUT || results.getDuration(row - 1) == TestResult.TIMEOUT) {
                System.out.printf("| n/a");
            } else {
                System.out.printf("| %.5f", ((float) results.getDuration(row)) / ((float) results.getDuration(row - 1)));
            }
            
            System.out.printf("\n");
        }
    }
    
    private static void printTestResultsFooter(TestResult results) {
        System.out.println("");
    }
    
    private static void printTestResults(TestResult results) {
        printTestResultsHeader(results);
        printTestResultsColumnHeaders(results, 20);
        printTestResultsRows(results, 20);
        printTestResultsFooter(results);
    }
    
    /**
     * Pääohjelma, jolla luodaan suorituskykyraportit toteutetuista tietorakenteista ja algoritmeista.
     * 
     * Tulostaa lukemat konsoliin, josta tarvittavat tiedot poimitaan käsin raporttiin.
     * 
     * @param args Komentoriviltä annetut parametrit.
     */
    public static void main(String[] args) {
        TestRunner tests = new TestRunner(5400000, 1800000);
        
        int[][] values = new int[][] {
            { 2,     10,     1},
            { 10,    100,    10},
            { 100,   1000,   100},
            { 1000,  5000,  1000}
        };
        
        for (int[] valueSet : values) {
            tests.addTest(new SolverTest(new AStarSolver(), valueSet[0], valueSet[1], valueSet[2]));
            tests.addTest(new SolverTest(new DijkstraSolver(), valueSet[0], valueSet[1], valueSet[2]));
            tests.addTest(new SolverTest(new BellmanFordSolver(), valueSet[0], valueSet[1], valueSet[2]));
        }
        
        tests.start();
        Iterator<TestResult> iterator = tests.getTestResultIterator();
        while (iterator.hasNext()) {
            printTestResults(iterator.next());
        }
    }
}
