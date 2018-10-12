package com.edii.spc.perf;

import java.util.List;

/**
 *
 * @author edii
 */
public interface Test {
    public String getName();
    public List<TestCase> getTestCases();
    public TestResult getTestResults();
}
