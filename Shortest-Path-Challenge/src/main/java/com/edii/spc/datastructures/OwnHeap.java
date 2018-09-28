package com.edii.spc.datastructures;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Minimikeko. 
 * 
 * Toistaiseksi toteutettu epätehokkaasti listalla, oikea binäärikekoon perustuva toteutus tehdään myöhemmin.
 * 
 * Tukee toimintoja add(T item), decreaseKey(T item) ja extractMin(), joiden kaikkien aikavaativuus on O(log n)
 * 
 * @param <T> Tietotyyppi jonka alkoita minimikeko sisältää. Luokan T pitää toteuttaa Javan rajapinta Comparable.
 */
public class OwnHeap<T> { 
    private List<T> list = new OwnList<>();
    private final Comparator<T> comparator;
    
    /**
     * Luo minimikeon annetulla vertailufunktiolla. 
     * 
     * @param comparator Vertailufunktio
     */
    public OwnHeap(Comparator<T> comparator) {
        this.comparator = comparator;
    }
    
    /**
     * Lisää alkion minimikekoon.
     * 
     * @param item Lisättävä alkio
     */
    public void add(T item) {
        list.add(item);
        list.sort(comparator);
    }
    
    /**
     * Lisää usean alkion minimikekoon.
     * 
     * @param items Lisättävät alkiot
     */
    public void add(Iterable<T> items) {
        for (T item : items) {
            this.add(item);
        }
    }
    
    /**
     * Poistaa minimikeosta pienimmän alkion.
     * 
     * @return Poistaa ja palauttaa minimikeon pienimmän alkion.
     */
    public T extractMin() {
        return list.remove(0);
    }
    
    /**
     * Selvittää onko keko tyhjä.
     * 
     * @return Palauttaa true jos minimikeko on tyhjä
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }
    
    /**
     * Ilmoittaa minimikeolle että annetun alkion avain on pienentynyt, ja sen sijainti keossa tulee laskea uudestaan.
     * 
     * @param item Muuttunut alkio
     */
    public void decreaseKey(T item) {
        list.sort(comparator);
    }
}
