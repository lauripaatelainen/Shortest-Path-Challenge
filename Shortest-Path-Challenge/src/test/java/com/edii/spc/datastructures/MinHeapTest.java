package com.edii.spc.datastructures;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import junit.framework.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Testitapaukset luokalla MinHeap.
 */
public class MinHeapTest {
    private final Comparator<Integer> intComparator = new Comparator<Integer>() {
        @Override
        public int compare(Integer a, Integer b) {
            return Integer.compare(a, b);
        }
    };
    
    private final Comparator<String> stringLengthComparator = new Comparator<String>() {
        @Override
        public int compare(String t, String t1) {
            return t.length() - t1.length();
        }
    };
    
    private Random random;
    
    /**
     * Alustaa Random-objektin testien käyttöön.
     */
    @Before
    public void initClass() {
        random = new Random();
    }
    
    /**
     * Testaa konstruktorin, muutaman insertin ja että lopputulos on mitä pitää.
     */
    @Test
    public void testConstructors1() {
        MinHeap<Integer> minHeap = new MinHeap<>(intComparator);
        Assert.assertTrue(minHeap.add(10));
        Assert.assertTrue(minHeap.add(30));
        Assert.assertTrue(minHeap.add(11));
        Assert.assertTrue(minHeap.add(50));
        assertEquals(4, minHeap.size());
        assertEquals(10, (int) minHeap.extractMin());
        assertEquals(3, minHeap.size());
    }
    
    /**
     * Testaa konstruktorin, muutaman insertin ja että lopputulos on mitä pitää. 
     */
    @Test
    public void testConstructors2() {
        MinHeap<String> minHeap = new MinHeap<>(stringLengthComparator);
        minHeap.add("Testi");
        minHeap.add("Pidempi testi");
        minHeap.add("A");
        minHeap.add("ABC");
        assertEquals(4, minHeap.size());
        assertEquals("A", minHeap.extractMin());
        assertEquals(3, minHeap.size());
        assertEquals("ABC", minHeap.extractMin());
        assertEquals(2, minHeap.size());
    }
    
    /**
     * Testaa konstruktorin joka ottaa parametrina listan, ja testaa että extractMin palauttaa edelleen pienimmän alkion.
     */
    @Test
    public void testConstructors3() {
        List<Integer> list = new OwnList<>();
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < 1000; i++) {
            int luku = random.nextInt();
            min = Math.min(luku, min);
            list.add(luku);
        }
        
        MinHeap<Integer> minHeap = new MinHeap<>(list, intComparator);
        assertEquals(min, (int) minHeap.extractMin());
    }
    
    /**
     * Lisää satunnaisia lukuja minimikekoon ja tarkistaa että kun ne yksitellen
     * poistetaan extractMin-metodilla, niin ne poistuvat keosta oikeassa järjestyksessä.
     */
    @Test
    public void testExtractMin() {
        MinHeap<Integer> minHeap = new MinHeap(intComparator);
        for (int i = 0; i < 50; i++) {
            minHeap.add(random.nextInt());
        }
        
        int edellinen = Integer.MIN_VALUE;
        for (int i = 0; i < 50; i++) {
            int luku = minHeap.extractMin();
            assertTrue(luku >= edellinen);
            edellinen = luku;
        }
    }
    
    /**
     * Tarkistaa että IndexOutOfBoundsException tapahtuu jos extractMiniä kutsutaan, 
     * kun keko on jo tyhjä. 
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testExtractMin2() {
        MinHeap<Integer> minHeap = new MinHeap(intComparator);
        for (int i = 0; i < 50; i++) {
            minHeap.add(random.nextInt());
        }
        
        for (int i = 0; i < 51; i++) {
            minHeap.extractMin();
        }
    }
    
    /**
     * Testaa että konstruktorin parametrina annetun listan alkiot tulevat keosta
     * ulos oikeassa järjestyksessä.
     */
    @Test
    public void testExtractMin3() {
        List<Integer> list = new OwnList<>();
        for (int i = 0; i < 50; i++) {
            list.add(random.nextInt());
        }
        
        MinHeap<Integer> minHeap = new MinHeap<>(list, intComparator);
        
        
        int edellinen = Integer.MIN_VALUE;
        for (int i = 0; i < 50; i++) {
            int luku = minHeap.extractMin();
            assertTrue(luku >= edellinen);
            edellinen = luku;
        }
    }
    
    /**
     * Testaa lähekkäin toisia olevien lukujen järjestyksen extractMin-toiminnossa.
     */
    @Test
    public void testExtractMin4() {
        MinHeap<Integer> minHeap = new MinHeap<>(Arrays.asList(new Integer[]{10, 12, 11}), intComparator);
        assertEquals(10, (int) minHeap.extractMin());
        assertEquals(11, (int) minHeap.extractMin());
        assertEquals(12, (int) minHeap.extractMin());
        minHeap = new MinHeap<>(Arrays.asList(new Integer[]{12, 11, 10}), intComparator);
        assertEquals(10, (int) minHeap.extractMin());
        assertEquals(11, (int) minHeap.extractMin());
        assertEquals(12, (int) minHeap.extractMin());
        minHeap = new MinHeap<>(Arrays.asList(new Integer[]{10, 11, 12}), intComparator);
        assertEquals(10, (int) minHeap.extractMin());
        assertEquals(11, (int) minHeap.extractMin());
        assertEquals(12, (int) minHeap.extractMin());
        minHeap = new MinHeap<>(Arrays.asList(new Integer[]{11, 12, 10}), intComparator);
        assertEquals(10, (int) minHeap.extractMin());
        assertEquals(11, (int) minHeap.extractMin());
        assertEquals(12, (int) minHeap.extractMin());
    }
    
    /**
     * Luokka, jolla testataan minimikeon toimintaa kun alkion avain vaihtuu. 
     */
    private class WeightedObject {
        private int id;
        private int weight;
        
        public WeightedObject(int weight) {
            id = random.nextInt();
            this.weight = weight;
        }
        
        public int getWeight() {
            return weight;
        }
        
        public void setWeight(int weight) {
            this.weight = weight;
        }

        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public boolean equals(Object o) {
            return this == o;
        }
    }
    
    /**
     * Vertailija WeightedObject -luokalle.
     */
    private final Comparator<WeightedObject> weightedObjectComparator = new Comparator<WeightedObject>() {
        @Override
        public int compare(WeightedObject a, WeightedObject b) {
            return Integer.compare(a.getWeight(), b.getWeight());
        }       
    };
    
    /**
     * Tarkistaa että extractMin palauttaa oikean objektin, myös avaimen muuttumisen jälkeen.
     */
    @Test
    public void testChangingWeight() {
        WeightedObject a = new WeightedObject(100);
        WeightedObject b = new WeightedObject(200);
        WeightedObject c = new WeightedObject(300);
        
        MinHeap<WeightedObject> minHeap = new MinHeap<>(weightedObjectComparator);
        minHeap.add(a);
        minHeap.add(b);
        minHeap.add(c);
        
        b.setWeight(50);
        minHeap.decreaseKey(b);
        assertEquals(b, minHeap.extractMin());
    }
    
    /**
     * Tarkistaa, että samaa objektia lisätessä uudestaan syntyy IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSameObjectTwice() {
        MinHeap<Integer> minHeap = new MinHeap<>(intComparator);
        minHeap.add(1);
        minHeap.add(1);
    }
    
    /**
     * Tarkistaa, että clear tyhjentää listan ja normaalit operaatiot toimivat sen jälkeen.
     */
    @Test
    public void testClear() {
        MinHeap<Integer> minHeap = new MinHeap<>(intComparator);
        Integer[] testData = new Integer[] { 5, 10, 3, 8, 1, 15 };
        for (Integer i : testData) {
            minHeap.add(i);
        }
        
        minHeap.clear();
        Assert.assertTrue(minHeap.isEmpty());
        Assert.assertEquals(0, minHeap.size());
        for (Integer i : testData) {
            Assert.assertFalse(minHeap.contains(i));
            minHeap.add(i);
        }
        
        Assert.assertEquals(1, (int) minHeap.extractMin());
    }
    
    /**
     * Tarkistaa remove() metodin toimivuuden.
     */
    @Test
    public void testRemove() {
        MinHeap<Integer> minHeap = new MinHeap<>(intComparator);
        Integer[] testData = new Integer[] { 5, 10, 3, 8, 1, 15 };
        for (Integer i : testData) {
            minHeap.add(i);
        }
        Assert.assertTrue(minHeap.contains(1));
        Assert.assertTrue(minHeap.contains(3));
        Assert.assertTrue(minHeap.contains(5));
        
        Assert.assertTrue(minHeap.remove(1));
        Assert.assertTrue(minHeap.remove(3));
        Assert.assertFalse(minHeap.remove(3));
        Assert.assertEquals(5, (int) minHeap.extractMin());
        
        Assert.assertFalse(minHeap.contains(1));
        Assert.assertFalse(minHeap.contains(3));
        Assert.assertFalse(minHeap.contains(5));
    }
    
    /**
     * Tarkistaa foreach-silmukan toimivuuden.
     */
    @Test
    public void testForeach() {
        MinHeap<String> minHeap = new MinHeap<>(stringLengthComparator);
        List<String> testData = new OwnList<>();
        for (String s : new String[] {"Yksi", "Kaksi", "Kolme"}) {
            testData.add(s);
        }
        
        minHeap.addAll(testData);
        for (String s : minHeap) {
            Assert.assertTrue(testData.remove(s));
        }
        Assert.assertTrue(testData.isEmpty());
    }
}
