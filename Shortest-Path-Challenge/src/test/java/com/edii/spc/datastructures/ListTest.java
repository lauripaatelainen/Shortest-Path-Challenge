package com.edii.spc.datastructures;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Testitapaukset List-rajapinnan toteuttaville luokille.
 * Toteutettu parametroituna testiluokkana, jolloin samat testitapaukset suoritetaan
 * automaattisesti kaikille määritellyille luokille. 
 */
@RunWith(Parameterized.class)
public class ListTest {
    private final Random random = new Random();
    private final List<Integer> intList;
    private final List<String> stringList;
    
    /**
     * Luo testiluokan annetuilla parametreilla.
     * 
     * @param intList Testattavan listan kokonaisluku-versio.
     * @param stringList Testattavan listan string-versio.
     */
    public ListTest(List<Integer> intList, List<String> stringList) {
        this.intList = intList;
        this.stringList = stringList;
    }
    
    private void intListFillWithSquares(int n) {
        for (int i = 0; i < n; i++) {
            intList.add(i * i);
        }
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
        lists.add(new Object[] { new OwnList<Integer>(), new OwnList<String>()});
        lists.add(new Object[] { new OwnLinkedList<Integer>(), new OwnLinkedList<String>()});
        return lists;
    }
    
    /**
     * Testaa List.clear() toiminto.
     */
    @Before
    public void clearLists() {
        intList.clear();
        stringList.clear();
    }
    
    /**
     * Testaa List.size() toiminto.
     */
    @Test
    public void testSize() {
        intList.add(1);
        stringList.add("a");
        Assert.assertTrue(intList.size() == 1);
        Assert.assertTrue(stringList.size() == 1);
    }

    /**
     * Testaa List.isEmpty() toiminto.
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
     * Testaa List.contains() toiminto.
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
     * Testaa listan add- ja remove-toiminnot.
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
        Assert.assertEquals(rand[0], stringList.get(0));
        Assert.assertEquals(rand[1], stringList.get(1));
        Assert.assertEquals(rand[3], stringList.get(2));
    }

    /**
     * Testaa listaan kohdistuvan foreach-silmukan toimivuus.
     */
    @Test
    public void testForEach() {
        intListFillWithSquares(10);
        int i = 0;
        for (Integer value : intList) {
            Assert.assertEquals(i * i, (int) value);
            i++;
        }
        Assert.assertEquals(10, i);
    }
    
    /**
     * Testaa listaan kohdistuvan foreach-silmukan toimivuus, kun listassa on vain yksi alkio.
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
     * Testaa List.toArray()-toimivuus.
     */
    @Test
    public void testToArray() {
        intListFillWithSquares(10);
        Object[] arr = intList.toArray();
        Assert.assertEquals(10, arr.length);
        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(i * i, (int) arr[i]);
        }
    }

    /**
     * Testaa List.toArray()-toimivuus.
     */
    @Test
    public void testToArray2() {
        intListFillWithSquares(10);
        Integer[] arr1 = new Integer[10];
        Integer[] arr2 = intList.toArray(arr1);
        Assert.assertSame(arr1, arr2);
        Assert.assertEquals(10, arr2.length);
        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(i * i, (int) arr2[i]);
        }
    }

    /**
     * Testaa List.toArray()-toimivuus.
     */
    @Test
    public void testToArray3() {
        intListFillWithSquares(10);
        Integer[] arr1 = new Integer[12];
        Integer[] arr2 = intList.toArray(arr1);
        Assert.assertSame(arr1, arr2);
        Assert.assertEquals(12, arr2.length);
        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(i * i, (int) arr2[i]);
        }
    }

    /**
     * Testaa List.toArray()-toimivuus.
     */
    @Test
    public void testToArray4() {
        intListFillWithSquares(10);
        Integer[] arr1 = new Integer[8];
        Integer[] arr2 = intList.toArray(arr1);
        Assert.assertNotSame(arr1, arr2);
        Assert.assertEquals(10, arr2.length);
        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(i * i, (int) arr2[i]);
        }
    }
    
    /**
     * Testaa listan add-toiminnon toimivuus, kun lisätään null-arvo.
     */
    @Test
    public void testAddNull() {
        Assert.assertTrue(stringList.add(null));
        Assert.assertTrue(intList.add(null));
        Assert.assertTrue(stringList.contains(null));
        Assert.assertTrue(intList.contains(null));
    }
    
    /**
     * Testaa listan add-toiminnon toimivuus.
     */
    @Test
    public void testAdd() {
        String rand1 = "String " + random.nextInt();
        String rand2 = "String " + random.nextInt();
        stringList.add(rand1);
        Assert.assertEquals(rand1, stringList.get(0));
        stringList.add(0, rand2);
        Assert.assertEquals(rand2, stringList.get(0));
        Assert.assertEquals(rand1, stringList.get(1));
    }
    
    /**
     * Testaa listan add(int, Object) toiminnon toimivuus.
     */
    @Test
    public void testAddWithIndex() {
        String rand1 = "String " + random.nextInt();
        stringList.add(0, rand1);
        Assert.assertEquals(rand1, stringList.get(0));
    }
    
    /**
     * Tarkista, että kun lisätään add-toiminnolla negatiiviseen indeksiin,
     * syntyy IndexOutOfBoundsException.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddNegativeIndex() {
        stringList.add(-1, "String");
    }
    
    /**
     * Tarkista, että kun lisätään add-toiminnolla liian suureen indeksiin,
     * syntyy IndexOutOfBoundsException.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOversizedIndex2() {
        stringList.add(1, "String");
    }

    /**
     * Testaa List.remove()-toiminnon toimivuus.
     */
    @Test
    public void testRemove() {
        stringList.add("String 1");
        stringList.add("String 2");
        stringList.add("String 3");
        Assert.assertTrue(stringList.remove("String 2"));
        Assert.assertTrue(stringList.contains("String 1"));
        Assert.assertFalse(stringList.contains("String 2"));
        Assert.assertTrue(stringList.contains("String 3"));
        ListIterator<String> listIterator = stringList.listIterator();
        Assert.assertEquals("String 1", listIterator.next());
        listIterator = stringList.listIterator(stringList.size());
        Assert.assertEquals("String 3", listIterator.previous());
        Assert.assertFalse(stringList.remove("String 2"));
    }

    /**
     * Testaa List.addAll() ja List.constainsAll() -toimintojen toimivuus yhdessä.
     */
    @Test
    public void testAddAllAndContainsAll() {
        List<Integer> randInts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            randInts.add(random.nextInt(1000));
        }
        Assert.assertTrue(intList.addAll(randInts));
        Assert.assertEquals(10, intList.size());
        Assert.assertTrue(intList.containsAll(randInts));
        randInts.set(5, 1005);
        Assert.assertFalse(intList.containsAll(randInts));
        randInts.remove(5);
        Assert.assertTrue(intList.containsAll(randInts));
    }

    /**
     * Testaa listan List.addAll(int, Collection) toimivuus.
     */
    @Test
    public void testAddAllWithIndex() {
        List<Integer> randInts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            intList.add(i);
            randInts.add(random.nextInt(1000));
        }
        Assert.assertTrue(intList.addAll(3, randInts));
        Assert.assertEquals(0, (int) intList.get(0));
        Assert.assertEquals(1, (int) intList.get(1));
        Assert.assertEquals(2, (int) intList.get(2));
        Assert.assertEquals(randInts.get(0), intList.get(3));
        Assert.assertEquals(randInts.get(9), intList.get(12));
        Assert.assertEquals(3, (int) intList.get(13));
        Assert.assertEquals(9, (int) intList.get(19));
    }
    
    /**
     * Testaa List.addAll(Collection) toimivuus kun parametrina annetaan tyhjä tietorakenne.
     */
    @Test
    public void testAddAllWithEmptyCollection() {
        List<Integer> randInts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            randInts.add(random.nextInt(1000));
        }
        Assert.assertTrue(intList.addAll(randInts));
        Assert.assertEquals(10, intList.size());
        Assert.assertFalse(intList.addAll(new ArrayList<>()));
        Assert.assertEquals(10, intList.size());
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
        Assert.assertTrue(intList.removeAll(removed));
        Assert.assertTrue(intList.containsAll(retained));
        Assert.assertEquals(retained.size(), intList.size());
        Assert.assertEquals(10 - removed.size(), intList.size());
        for (Integer removedInteger : removed) {
            Assert.assertFalse(intList.contains(removedInteger));
        }
    }

    /**
     * Testaa listan List.retainAll()-toiminnon toimivuus.
     */
    @Test
    public void testRetainAll() {
        intListFillWithSquares(10);
        List<Integer> retained = new ArrayList<>(intList);
        List<Integer> removed = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            removed.add(retained.remove(3));
        }
        Assert.assertTrue(intList.containsAll(retained));
        Assert.assertTrue(intList.retainAll(retained));
        Assert.assertTrue(intList.containsAll(retained));
        Assert.assertEquals(retained.size(), intList.size());
        Assert.assertEquals(10 - removed.size(), intList.size());
        for (Integer removedInteger : removed) {
            Assert.assertFalse(String.format("intList sisältää %d vaikka pitäisi olla poistettu", removedInteger), intList.contains(removedInteger));
        }
    }
    
    /**
     * Testaa listan List.indexOf() -toiminnon toimivuus.
     */
    @Test
    public void testIndexOf() {
        intListFillWithSquares(10);
        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(i, intList.indexOf(i * i));
        }
        Assert.assertEquals(-1, intList.indexOf(1005));
    }
    
    /**
     * Testaa listan List.lastIndexOf()-toiminnon toimivuus.
     */
    @Test
    public void testLastIndexOf() {
        intListFillWithSquares(10);
        intListFillWithSquares(10);
        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(i + 10, intList.lastIndexOf(i * i));
        }
        Assert.assertEquals(-1, intList.lastIndexOf(1005));
    }
    
    /**
     * Testaa listan List.set()-toiminnon toimivuus.
     */
    @Test
    public void testSet() {
        intListFillWithSquares(10);
        Assert.assertEquals(5 * 5, (int) intList.set(5, 105));
        Assert.assertTrue(intList.contains(105));
        Assert.assertEquals(105, (int) intList.get(5));
    }
    
    /**
     * Tarkista, että kun List.set()-metodia kutsutaan negatiivisella indeksillä,
     * syntyy IndexOutOfBoundsException.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetNegativeIndex() {
        intList.add(5);
        intList.set(-1, 2);
    }
    
    /**
     * Tarkista, että kun List.set()-metodia kutsutaan liian suurella indeksillä,
     * syntyy IndexOutOfBoundsException.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOversizedIndex() {
        intList.add(5);
        intList.set(2, 2);
    }
    
    /**
     * Tarkista, että kun List.set()-metodia kutsutaan liian suurella indeksillä,
     * syntyy IndexOutOfBoundsException.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetLast() {
        intList.add(5);
        intList.set(1, 2);
    }
    
    /**
     * Testaa equals-metodin toimivuus.
     */
    @Test
    public void equals() {
        try {
            Constructor<? extends List> constructor = stringList.getClass().getConstructor();
            List newList = constructor.newInstance();
            for (String str : new String[] {"Yksi", "Kaksi", "Kolme"}) {
                newList.add(str);
                stringList.add(str);
            }
            
            Assert.assertEquals(newList, stringList);
            newList.set(1, "Korvattu");
            Assert.assertFalse(newList.equals(stringList));
            newList.remove(1);
            Assert.assertFalse(newList.equals(stringList));
            Assert.assertFalse(newList.equals(null));
            Assert.assertFalse(newList.equals(new OwnSet()));
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            Assert.fail("Listan luonti oletuskonstruktorilla epäonnistui: " + e.getMessage());
        }
    }
    
    /**
     * Testaa sublist-metodin toimivuus.
     * Omissa toteutuksissa se ei ole tuettu, joten tarkistetaan että UnsupportedOperationException tapahtuu.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void sublist() {
        if (stringList instanceof OwnAbstractList) {
            stringList.add("Yksi");
            stringList.subList(0, 1);
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
