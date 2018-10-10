package com.edii.spc.datastructures;

import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Sellaiset testitapaukset OwnSet-luokalle, joita geneerinen CollectionTest ei kata.
 */
public class SetTest {
    private Set<Integer> set = new OwnSet<>();
    
    /**
     * Tyhjennä joukko ennen testitapauksia.
     */
    @Before
    public void clearSet() {
        set.clear();
    }
    
    /**
     * Tarkista, että joukko toimii oikein kun yritetään lisätä duplikaattiarvo.
     */
    @Test
    public void addDuplicate() {
        Assert.assertEquals(0, set.size());
        Assert.assertTrue(set.add(5));
        Assert.assertFalse(set.add(5));
        Assert.assertEquals(1, set.size());
    }
    
    /**
     * Tarkista, että alkion poisto toimii oikein.
     */
    @Test
    public void removeExisting() {
        Assert.assertTrue(set.add(5));
        Assert.assertTrue(set.remove(5));
        Assert.assertEquals(0, set.size());
    }
    
    /**
     * Tarkista, että poisto-operaatio toimii oikein kun alkiota ei löydy joukosta.
     */
    @Test
    public void removeNonExistent() {
        Assert.assertTrue(set.add(5));
        Assert.assertFalse(set.remove(6));
        Assert.assertEquals(1, set.size());
    }
    
    /**
     * Tarkista, että joukon tyhjennys toimii oikein.
     */
    @Test
    public void testClear() {
        Assert.assertEquals(0, set.size());
        Assert.assertTrue(set.add(5));
        Assert.assertEquals(1, set.size());
        set.clear();
        Assert.assertEquals(0, set.size());
    }
}
