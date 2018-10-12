/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edii.spc.perf;

import com.edii.spc.datastructures.OwnList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author edii
 */
public class TestRunner {

    private interface Task {

        public void run() throws InterruptedException;

        public void interrupt();
    }

    private static final long DEFAULT_TEST_TIMEOUT = 5000;
    private static final long DEFAULT_TEST_CASE_TIMEOUT = 1000;

    private class TestResultIterator implements Iterator<TestResult> {

        private int i = 0;

        @Override
        public boolean hasNext() {
            return isRunning() || i < tests.size();
        }

        @Override
        public TestResult next() {
            testResultsLock.lock();
            try {
                while (i >= testResults.size()) {
                    moreResults.await();
                }
            } catch (InterruptedException e) {
                interrupted = true;
                return null;
            } finally {
                testResultsLock.unlock();
            }

            return testResults.get(i++);
        }
    }

    private class TestCaseTask implements Task {

        private final TestCase testCase;

        public TestCaseTask(TestCase testCase) {
            this.testCase = testCase;
        }

        @Override
        public void run() throws InterruptedException {
            runTestCase(testCase);
        }

        @Override
        public void interrupt() {
            testCase.interrupt();
        }
    }

    private class TestTask implements Task {

        private final Test test;

        public TestTask(Test test) {
            this.test = test;
        }

        @Override
        public void run() throws InterruptedException {
            runTest(test);
        }

        @Override
        public void interrupt() {
            currentTestInterrupted = true;
            if (currentTestCase != null) {
                currentTestCase.interrupt();
            }
        }
    }

    private final List<Test> tests = new OwnList<>();
    private final List<TestResult> testResults = new OwnList<>();
    private final Lock testResultsLock = new ReentrantLock();
    private final Condition moreResults = testResultsLock.newCondition();

    private Test currentTest = null;
    private TestCase currentTestCase = null;
    private volatile boolean currentTestInterrupted = false;

    private final long testTimeout;
    private final long testCaseTimeout;
    private volatile boolean running = false;
    private volatile boolean interrupted = false;

    public TestRunner() {
        this(DEFAULT_TEST_TIMEOUT, DEFAULT_TEST_CASE_TIMEOUT);
    }

    public TestRunner(long testTimeout, long testCaseTimeout) {
        this.testTimeout = testTimeout;
        this.testCaseTimeout = testCaseTimeout;
    }

    public void addTest(Test test) {
        this.tests.add(test);
    }

    public boolean isRunning() {
        return running;
    }

    public void interrupt() {
        interrupted = true;
        currentTestInterrupted = true;
        if (currentTestCase != null) {
            currentTestCase.interrupt();
        }
    }

    private boolean runWithTimeout(long millis, Task task) {
        Lock runLock = new ReentrantLock();
        Condition finished = runLock.newCondition();
        runLock.lock();
        final boolean[] taskInterrupted = new boolean[]{false};

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    task.run();
                } catch (InterruptedException e) {
                } finally {
                    runLock.lock();
                    try {
                        finished.signalAll();
                    } finally {
                        runLock.unlock();
                    }
                }
            }
        });

        thread.start();
        try {
            if (!finished.await(millis, TimeUnit.MILLISECONDS)) {
                task.interrupt();
                finished.await();
            }

            return true;
        } catch (InterruptedException e) {
            System.err.println("runWithTimeout interrupted");
            return false;
        } finally {
            runLock.unlock();
        }
    }

    private void runTestCase(TestCase testCase) throws InterruptedException {
        testCase.run();
    }

    private void runTest(Test test) throws InterruptedException {
        Iterator<TestCase> iterator = test.getTestCases().iterator();
        while (!interrupted && !currentTestInterrupted && iterator.hasNext()) {
            currentTestCase = iterator.next();
            Task testCaseRunnable = new TestCaseTask(currentTestCase);
            runWithTimeout(testCaseTimeout, testCaseRunnable);
        }
        currentTestCase = null;
    }

    public void run() throws InterruptedException {
        Iterator<Test> iterator = tests.iterator();
        while (!interrupted && iterator.hasNext()) {
            currentTestInterrupted = false;
            currentTest = iterator.next();
            Task testRunnable = new TestTask(currentTest);
            runWithTimeout(testTimeout, testRunnable);

            testResultsLock.lock();
            try {
                testResults.add(currentTest.getTestResults());
                moreResults.signalAll();
            } finally {
                testResultsLock.unlock();
            }
        }
        currentTest = null;
        running = false;
    }

    public boolean start() {
        final Lock startLock = new ReentrantLock();
        final Condition started = startLock.newCondition();
        startLock.lock();
        if (isRunning()) {
            throw new IllegalAccessError("Käynnissä jo");
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startLock.lock();
                    try {
                        running = true;
                        started.signalAll();
                    } finally {
                        startLock.unlock();
                    }
                    TestRunner.this.run();
                } catch (InterruptedException e) {
                    System.err.println("InterruptedException TestRunnerin ajossa");
                    e.printStackTrace(System.err);
                }
            }
        });

        try {
            thread.start();
            while (!running) {
                started.await();
            }
            return true;
        } catch (InterruptedException e) {
            thread.interrupt();
            if (currentTestCase != null) {
                currentTestCase.interrupt();
            }
            return false;
        } finally {
            startLock.unlock();
        }
    }

    public List<TestResult> getTestResults() {
        return testResults;
    }

    public Iterator<TestResult> getTestResultIterator() {
        return new TestResultIterator();
    }
}
