package com.edii.spc.datastructures;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Set-tietorakenteen toteuttava luokka. 
 * 
 * Toistaiseksi käyttää Javan HashSet-luokkaa, oma toteutus tehdään myöhemmin.
 * 
 * @param <T> Tietotyyppi, jonka alkioita joukkoon laitetaan.
 */
public class OwnSet<T> extends OwnAbstractSet<T> {
    private Map<T, Object> map;

    public OwnSet() {
        this.map = new OwnMap<>();
    }
    
    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public Iterator<T> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public boolean add(T e) {
        if (map.containsKey(e)) {
            return false;
        } else {
            map.put(e, null);
            return true;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (map.containsKey(o)) {
            map.remove(o);
            return true;
        }
        
        return false;
    }

    @Override
    public void clear() {
        map.clear();
    }
}
