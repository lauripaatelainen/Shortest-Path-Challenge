package com.edii.spc.perf;

/**
 *
 * @author edii
 */
public interface TestCase {
    public void run() throws InterruptedException;
    public void interrupt();
}
