package com.edii.spc.datastructures;

import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Sellaiset testitapaukset OwnSet-luokalle, joita geneerinen CollectionTest ei kata.
 */
public class SetTest {
    Set<Integer> set = new OwnSet<>();
    
    @Before
    public void clearSet() {
        set.clear();
    }
    
    @Test
    public void addDuplicate() {
        Assert.assertEquals(0, set.size());
        Assert.assertTrue(set.add(5));
        Assert.assertFalse(set.add(5));
        Assert.assertEquals(1, set.size());
    }
    
    @Test
    public void removeExisting() {
        Assert.assertTrue(set.add(5));
        Assert.assertTrue(set.remove(5));
        Assert.assertEquals(0, set.size());
    }
    
    @Test
    public void removeNonExistent() {
        Assert.assertTrue(set.add(5));
        Assert.assertFalse(set.remove(6));
        Assert.assertEquals(1, set.size());
    }
    
    @Test
    public void testClear() {
        Assert.assertEquals(0, set.size());
        Assert.assertTrue(set.add(5));
        Assert.assertEquals(1, set.size());
        set.clear();
        Assert.assertEquals(0, set.size());
    }
}
