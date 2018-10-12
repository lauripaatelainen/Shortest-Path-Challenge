package com.edii.spc.perf;

/**
 *
 * @author edii
 */
public interface TestResult {
    public static int TIMEOUT = -1;
    public static enum Type {
        String, Float, Integer
    };
    
    public String getName();
    public int getColumnCount();
    public int getRowCount();
    public String[] getColumnNames();
    public Type[] getColumnTypes();
    public String getStringValue(int row, int column);
    public Float getFloatValue(int row, int column);
    public Integer getIntValue(int row, int column);
    public long getDuration(int row);
}
