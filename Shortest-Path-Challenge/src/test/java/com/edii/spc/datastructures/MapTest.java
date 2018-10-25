package com.edii.spc.datastructures;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Testitapaukset OwnMap-luokalle.
 */
public class MapTest {
    private Map<Integer, String> map;
    
    @Before
    public void initList() {
        map = new OwnMap<>();
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(map.isEmpty());
        map.put(1, "Testi");
        assertFalse(map.isEmpty());
    }
    
    @Test
    public void testSize() {
        assertEquals(0, map.size());
        for (int i = 0; i < 10; i++) {
            map.put(i, Integer.toString(i));
            assertEquals(i + 1, map.size());
        }
    }
    
    @Test
    public void testClear() {
        map.put(1, "Testi");
        assertTrue(map.containsKey(1));
        map.clear();
        assertFalse(map.containsKey(1));
    }
    
    @Test
    public void testClearWithKeySet() {
        map.put(1, "Testi");
        assertTrue(map.containsKey(1));
        map.keySet().clear();
        assertFalse(map.containsKey(1));
    }
    
    @Test
    public void testClearWithValues() {
        map.put(1, "Testi");
        assertTrue(map.containsKey(1));
        map.values().clear();
        assertFalse(map.containsKey(1));
    }
    
    @Test
    public void testClearWithEntrySet() {
        map.put(1, "Testi");
        assertTrue(map.containsKey(1));
        map.entrySet().clear();
        assertFalse(map.containsKey(1));
    }
    
    @Test
    public void testContainsKey() {
        assertFalse(map.containsKey(1));
        map.put(1, "Testi");
        assertTrue(map.containsKey(1));
    }
    
    @Test
    public void testContainsValue() {
        assertFalse(map.containsValue("Testi"));
        map.put(1, "Testi");
        assertTrue(map.containsValue("Testi"));
    }
    
    @Test
    public void testGet() {
        map.put(1, "Testi 1");
        map.put(2, "Testi 2");
        assertEquals("Testi 1", map.get(1));
        assertEquals("Testi 2", map.get(2));
    }
    
    @Test
    public void testGetWithoutValue() {
        assertNull(map.get(1));
    }
    
    @Test
    public void testPut() {
        assertNull(map.put(1, "Testi 1"));
        assertEquals("Testi 1", map.put(1, "Testi 2"));
        assertEquals("Testi 2", map.get(1));
    }
    
    @Test
    public void testPutMany() {
        for (int i = 0; i < 1000; i++) {
            map.put(i, "Testi " + i);
        }
        
        assertEquals(1000, map.size());
    }
    
    @Test
    public void testRemove() {
        map.put(1, "Testi 1");
        assertEquals("Testi 1", map.remove(1));
        assertFalse(map.containsKey(1));
    }
    
    @Test
    public void testRemoveWithoutValue() {
        assertNull(map.remove(1));
    }

    @Test
    public void testRemoveExact() {
        map.put(1, "Testi 1");
        assertFalse(map.remove(2, "Testi 2"));
        assertFalse(map.remove(1, "Testi 2"));
        assertFalse(map.remove(2, "Testi 1"));
        assertTrue(map.remove(1, "Testi 1"));
        assertFalse(map.containsKey(1));
    }
    
    @Test
    public void testPutAll() {
        Map<Integer, String> anotherMap = new OwnMap<>();
        anotherMap.put(1, "Testi 1");
        anotherMap.put(2, "Testi 2");
        anotherMap.put(3, "Testi 3");
        map.putAll(anotherMap);
        assertEquals("Testi 1", map.get(1));
        assertEquals("Testi 2", map.get(2));
        assertEquals("Testi 3", map.get(3));
        assertEquals(3, map.size());
    }
    
    @Test
    public void testKeySet() {
        map.put(1, "Testi 1");
        map.put(2, "Testi 2");
        map.put(3, "Testi 3");
        Set<Integer> keySet = map.keySet();
        assertTrue(keySet.contains(1));
        assertTrue(keySet.contains(2));
        assertTrue(keySet.contains(3));
        assertEquals(3, keySet.size());
    }
    
    @Test
    public void testKeySetRemove() {
        map.put(1, "Testi 1");
        map.put(2, "Testi 2");
        map.put(3, "Testi 3");
        Set<Integer> keySet = map.keySet();
        assertTrue(keySet.remove(2));
        assertFalse(keySet.remove(2));
        assertTrue(keySet.contains(1));
        assertFalse(keySet.contains(2));
        assertTrue(keySet.contains(3));
        assertEquals(2, keySet.size());
    }
    
    @Test
    public void testValues() {
        map.put(1, "Testi 1");
        map.put(2, "Testi 2");
        map.put(3, "Testi 3");
        Collection<String> values = map.values();
        assertTrue(values.contains("Testi 1"));
        assertTrue(values.contains("Testi 2"));
        assertTrue(values.contains("Testi 3"));
        assertEquals(3, values.size());
    }
    
    @Test
    public void testValuesRemove() {
        map.put(1, "Testi 1");
        map.put(2, "Testi 2");
        map.put(3, "Testi 3");
        Collection<String> values = map.values();
        assertTrue(values.remove("Testi 2"));
        assertFalse(values.remove("Testi 2"));
        assertTrue(values.contains("Testi 1"));
        assertFalse(values.contains("Testi 2"));
        assertTrue(values.contains("Testi 3"));
        assertEquals(2, values.size());
    }
    
    @Test
    public void testEntrySet() {
        map.put(1, "Testi 1");
        map.put(2, "Testi 2");
        map.put(3, "Testi 3");
        boolean[] checked = new boolean[] {
            false, false, false
        };
        Set<Entry<Integer, String>> entrySet = map.entrySet();
        for (Entry<Integer, String> entry : entrySet) {
            assertEquals("Testi " + entry.getKey(), entry.getValue());
            checked[entry.getKey() - 1] = true;
        }
        assertTrue(checked[0]);
        assertTrue(checked[1]);
        assertTrue(checked[2]);
        assertEquals(3, entrySet.size());
    }
    
    @Test
    public void testEquals() {
        Map<Integer, String> anotherMap = new OwnMap<>();
        for (int i = 0; i < 10; i++) {
            map.put(i, "Testi" + i);
            anotherMap.put(i, "Testi" + i);
        }
        assertEquals(map, anotherMap);
        assertEquals(map.hashCode(), anotherMap.hashCode());
    }
    
    @Test
    public void testEqualsWithNull() {
        map.put(1, "Testi 1");
        assertFalse(map.equals(null));
    }
    
    @Test
    public void testEqualsWithDifferentValues() {
        Map<Integer, String> anotherMap = new OwnMap<>();
        map.put(1, "Testi 1");
        anotherMap.put(1, "Testi 2");
        assertFalse(map.equals(anotherMap));
        assertNotSame(map.hashCode(), anotherMap.hashCode());
    }
    
    @Test
    public void testEqualsWithDifferentKeys() {
        Map<Integer, String> anotherMap = new OwnMap<>();
        anotherMap.put(1, "Testi 1");
        map.put(2, "Testi 1");
        assertFalse(map.equals(anotherMap));
        assertNotSame(map.hashCode(), anotherMap.hashCode());
    }
}
