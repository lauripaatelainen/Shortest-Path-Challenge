package com.edii.spc.perf;

import com.edii.spc.datastructures.OwnList;
import com.edii.spc.game.GameField;
import com.edii.spc.game.solvers.Solver;
import java.util.List;

public class SolverTest implements Test {
    private class SolverTestResults implements TestResult {
        private final TestResult.Type[] COLUMN_TYPES = new TestResult.Type[] {
            TestResult.Type.Integer, TestResult.Type.Integer
        };
        private final String[] COLUMN_NAMES = new String[] {
            "Kentän koko", "Solmujen määrä"
        };
        
        private final List<Integer> sizes = new OwnList<>();
        private final List<Long> durations = new OwnList<>();
        
        public void addResult(int size, long duration) {
            sizes.add(size);
            durations.add(duration);
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public int getRowCount() {
            return sizes.size();
        }

        @Override
        public TestResult.Type[] getColumnTypes() {
            return COLUMN_TYPES;
        }

        @Override
        public String[] getColumnNames() {
            return COLUMN_NAMES;
        }

        @Override
        public String getStringValue(int row, int column) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public Float getFloatValue(int row, int column) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public Integer getIntValue(int row, int column) {
            if (column != 0 && column != 1) {
                throw new IndexOutOfBoundsException();
            }
            
            if (column == 0) {
                return sizes.get(row);
            } else {
                return sizes.get(row) * sizes.get(row);
            }
        }

        @Override
        public String getName() {
            return SolverTest.this.getName();
        }

        @Override
        public long getDuration(int row) {
            return durations.get(row);
        }
    }
    
    private class SolverTestCase implements TestCase {
        private final int size;
        
        public SolverTestCase(int size) {
            this.size = size;
        }
        
        @Override
        public void run() throws InterruptedException {
            GameField gameField = GameField.generateRandomField(size);
            long start = System.nanoTime();
            long end = TestResult.TIMEOUT;
            try {
                solver.solve(gameField);
                end = System.nanoTime();
            } catch (InterruptedException e) {
                throw e;
            } finally {
                long duration;
                if (end == TestResult.TIMEOUT) {
                    duration = TestResult.TIMEOUT;
                } else {
                    duration = end - start;
                }
                testResults.addResult(size, duration);
            }
        }
        
        @Override
        public void interrupt() {
            solver.interrupt();
        }
    }
    
    private final Solver solver;
    private final SolverTestResults testResults = new SolverTestResults();
    private final int fromSize;
    private final int toSize;
    private final int step;

    public SolverTest(Solver solver, int fromSize, int toSize, int step) {
        this.solver = solver;
        this.fromSize = fromSize;
        this.toSize = toSize;
        this.step = step;
    }
    
    @Override
    public List<TestCase> getTestCases() {
        List<TestCase> testCases = new OwnList<>();
        for (int size = fromSize; size < toSize; size += step) {
            testCases.add(new SolverTestCase(size));
        }
        return testCases;
    }

    @Override
    public String getName() {
        return String.format("Ratkaistaan pelikenttä algoritmilla %s, pelikentän ko'oilla %d-%d, askeleen koko %d", solver.getClass().getSimpleName(), fromSize, toSize, step);
    }

    @Override
    public TestResult getTestResults() {
        return testResults;
    }
}
