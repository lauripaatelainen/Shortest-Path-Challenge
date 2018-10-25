package com.edii.spc.datastructures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Testitapaukset Collection-rajapinnan toteuttaville luokille.
 * Toteutettu parametroituna testiluokkana, jolloin samat testitapaukset suoritetaan
 * automaattisesti kaikille määritellyille luokille. 
 */
@RunWith(Parameterized.class)
public class CollectionTest {
    private final Random random = new Random();
    private final Collection<Integer> intList;
    private final Collection<String> stringList;
    
    /**
     * Luo testiluokan annetuilla parametreilla.
     * 
     * @param intClctn Testattavan kokoelman kokonaisluku-versio.
     * @param strClctn Testattavan kokoelman string-versio.
     */
    public CollectionTest(Collection<Integer> intClctn, Collection<String> strClctn) {
        this.intList = intClctn;
        this.stringList = strClctn;
    }

    /**
     * Parametrit testiluokalle. Kaikki tämän funktion palauttamat oliot annetaan
     * parametreiksi testiluokan konstruktorille, eli nämä testit ajetaan automaattisesti
     * useammalle luokalle.
     * 
     * @return Palauttaa testiluokan parametrit, eli useita eri List-toteutuksia.
     */
    @Parameterized.Parameters
    public static Collection lists() {
        List<Object[]> lists = new OwnList<>();
        lists.add(new Object[] { new ArrayList<Integer>(), new ArrayList<String>()});
        lists.add(new Object[] { new LinkedList<Integer>(), new LinkedList<String>()});
        lists.add(new Object[] { new HashSet<Integer>(), new HashSet<String>()});
        lists.add(new Object[] { new OwnList<Integer>(), new OwnList<String>()});
        lists.add(new Object[] { new OwnLinkedList<Integer>(), new OwnLinkedList<String>()});
        lists.add(new Object[] { new OwnSet<Integer>(), new OwnSet<String>()});
        lists.add(new Object[] { new MinHeap<Integer>(), new MinHeap<String>()});
        return lists;
    }
    
    private void intListFillWithSquares(int n) {
        for (int i = 0; i < n; i++) {
            intList.add(i * i);
        }
    }
    
    /**
     * Tyhjennä listat ennen testejä.
     */
    @Before
    public void clearCollections() {
        intList.clear();
        stringList.clear();
    }
    
    /**
     * Testaa Collection.size() toiminto.
     */
    @Test
    public void testSize() {
        Assert.assertEquals(0, intList.size());
        Assert.assertEquals(0, stringList.size());
        intList.add(1);
        stringList.add("a");
        Assert.assertEquals(1, intList.size());
        Assert.assertEquals(1, stringList.size());
    }

    /**
     * Testaa Collection.isEmpty() toiminto.
     */
    @Test
    public void testIsEmpty() {
        Assert.assertTrue(intList.isEmpty());
        Assert.assertTrue(stringList.isEmpty());
        intList.add(1);
        stringList.add("a");
        Assert.assertFalse(intList.isEmpty());
        Assert.assertFalse(stringList.isEmpty());
    }

    /**
     * Testaa Collection.contains() toiminto.
     */
    @Test
    public void testContains() {
        int[] randomInts = new int[] {
            random.nextInt(),
            random.nextInt(),
            random.nextInt()
        };
        Assert.assertFalse(intList.contains(randomInts[0]));
        Assert.assertFalse(intList.contains(randomInts[1]));
        Assert.assertFalse(intList.contains(randomInts[2]));
        intList.add(randomInts[2]);
        intList.add(randomInts[1]);
        intList.add(randomInts[0]);
        Assert.assertTrue(intList.contains(randomInts[0]));
        Assert.assertTrue(intList.contains(randomInts[1]));
        Assert.assertTrue(intList.contains(randomInts[2]));
    }
    
    /**
     * Testaa kokoelman add- ja remove-toiminnot.
     */
    @Test
    public void testAddAndRemove() {
        String[] rand = new String[] {"One", "Two", "Three", "Four"};
        stringList.add(rand[0]);
        stringList.add(rand[1]);
        stringList.add(rand[2]);
        stringList.add(rand[3]);
        Assert.assertTrue(stringList.remove(rand[2]));
        Assert.assertEquals(3, stringList.size());
        Assert.assertTrue(stringList.contains(rand[0]));
        Assert.assertTrue(stringList.contains(rand[1]));
        Assert.assertTrue(stringList.contains(rand[3]));
    }
    
    /**
     * Testaa kokoelmaan kohdistuvan foreach-silmukan toimivuus, kun kokoelmassa on vain yksi alkio.
     */
    @Test
    public void testForEachWithOneItem() {
        int rand = random.nextInt(1000);
        intList.add(rand);
        int value = -1;
        int i = 0;
        for (Integer val : intList) {
            value = val;
            i++;
        }
        Assert.assertEquals(value, rand);
        Assert.assertEquals(1, i);
    }

    /**
     * Testaa Collection.addAll() ja Collection.constainsAll() -toimintojen toimivuus yhdessä.
     */
    @Test
    public void testAddAllAndContainsAll() {
        Set<Integer> randInts = new HashSet<>();
        while (randInts.size() < 10) {
            randInts.add(random.nextInt(1000));
        }
        
        List<Integer> randIntsList = new ArrayList<>();
        randIntsList.addAll(randInts);
        Assert.assertTrue(intList.addAll(randIntsList));
        Assert.assertEquals(10, intList.size());
        Assert.assertTrue(intList.containsAll(randIntsList));
        randIntsList.set(5, 1005);
        Assert.assertFalse(intList.containsAll(randIntsList));
        randIntsList.remove(5);
        Assert.assertTrue(intList.containsAll(randIntsList));
    }
    
    /**
     * Testaa Collection.addAll(Collection) toimivuus kun parametrina annetaan tyhjä tietorakenne.
     */
    @Test
    public void testAddAllWithEmptyCollection() {
        Set<Integer> randInts = new HashSet<>();
        while (randInts.size() < 10) {
            randInts.add(random.nextInt(1000));
        }
        
        List<Integer> randIntsList = new ArrayList<>();
        randIntsList.addAll(randInts);
        Assert.assertTrue(intList.addAll(randIntsList));
        Assert.assertEquals(10, intList.size());
        Assert.assertFalse(intList.addAll(new ArrayList<>()));
        Assert.assertEquals(10, intList.size());
    }
    
    /**
     * Testaa kokoelman ainoan alkion poiston.
     */
    @Test
    public void testRemoveLastItem() {
        intList.add(1);
        intList.remove(1);
        Assert.assertTrue(intList.isEmpty());
    }

    /**
     * Testaa List.removeAll()-toiminnon toimivuus.
     */
    @Test
    public void testRemoveAll() {
        intListFillWithSquares(10);
        List<Integer> retained = new ArrayList<>(intList);
        List<Integer> removed = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            removed.add(retained.remove(3));
        }
        Assert.assertTrue(intList.containsAll(retained));
        Assert.assertTrue(intList.containsAll(removed));
        
        System.out.println(Arrays.toString(intList.toArray()));
        Assert.assertTrue(intList.removeAll(removed));
        System.out.println(Arrays.toString(intList.toArray()));
        
        Assert.assertTrue(intList.containsAll(retained));
        Assert.assertEquals(retained.size(), intList.size());
        Assert.assertEquals(10 - removed.size(), intList.size());
        for (Integer removedInteger : removed) {
            Assert.assertFalse("Kokoelma sisältää poistetun luvun: " + removedInteger, intList.contains(removedInteger));
        }
    }
    
    /**
     * Testaa alkoiden poiston iteraattorin kautta.
     */
    @Test
    public void testRemoveWithIterator() {
        intListFillWithSquares(10);
        Iterator<Integer> it = intList.iterator();
        it.next();
        it.remove();
        Assert.assertEquals(9, intList.size());
    }
}
