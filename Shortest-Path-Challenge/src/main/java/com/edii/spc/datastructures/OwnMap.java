package com.edii.spc.datastructures;

import java.util.Collection;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Javan HashMapia vastaava oma toteutus.
 * 
 * @param <K> Tietotyyppi, jonka alkioita avaimet on
 * @param <V> Tietotyyppi, jonka alkioita arvot on
 */
public class OwnMap<K, V> implements Map<K, V> {
    private static class MapItem<K, V> implements Entry<K, V> {
        private final K key;
        private V value;
        
        public MapItem(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * @return the key
         */
        @Override
        public K getKey() {
            return key;
        }

        /**
         * @return the value
         */
        @Override
        public V getValue() {
            return value;
        }

        /**
         * @param value the value to set
         */
        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 31 * hash + Objects.hashCode(this.key);
            hash = 31 * hash + Objects.hashCode(this.value);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final MapItem<?, ?> other = (MapItem<?, ?>) obj;
            if (!Objects.equals(this.key, other.key)) {
                return false;
            }
            if (!Objects.equals(this.value, other.value)) {
                return false;
            }
            return true;
        }
    }
    
    private static final int DEFAULT_INITIAL_CAPACITY = 10;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    
    private OwnLinkedList<MapItem<K, V>>[] items;
    private float loadFactor;
    private int itemsCount = 0;
    
    /**
     * Luo uuden hajautustaulun oletusarvoilla.
     */
    public OwnMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }
    
    /**
     * Luo uuden hajautustaulun annetulla lähtökoolla ja oletusarvoisella täyttöasteella.
     * @param initialSize Sisäisen tietorakenteen lähtökoko.
     */
    public OwnMap(int initialSize) {
        this(initialSize, DEFAULT_LOAD_FACTOR);
    }
    
    /**
     * Luo uuden hajautustaulun annetuilla lähtökoolla ja täyttöasteella.
     * @param initialCapacity Lähtökoko sisäille tietorakenteelle
     * @param loadFactor Täyttöaste, jossa sisäistä tietorakennetta kasvatetaan.
     */
    public OwnMap(int initialCapacity, float loadFactor) {
        this.items = (OwnLinkedList[]) new OwnLinkedList[initialCapacity];
        this.loadFactor = loadFactor;
    }
    
    private OwnLinkedList<MapItem<K, V>> getListForKey(Object key, boolean create) {
        if (key == null) {
            return null;
        }
        
        int hashCode = key.hashCode();
        int idx = Math.abs(hashCode % items.length);
        OwnLinkedList<MapItem<K, V>> list = items[idx];
        
        if (create && list == null) {
            list = new OwnLinkedList<>();
            items[idx] = list;
        }
        
        return list;
    }
    
    private void growIfNeeded() {
        if ((float) itemsCount / (float) items.length >= loadFactor) {
            grow();
        }
    }
    
    private void grow() {
        Set<Entry<K, V>> entrySet = this.entrySet();
        
        for (int i = 0; i < items.length; i++) {
            items[i] = null;
        }
        
        items = new OwnLinkedList[items.length * 2];
        
        for (Entry<K, V> entry : entrySet) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public int size() {
        return itemsCount;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object o) {
        V item = this.get(o);
        return item != null;
    }

    @Override
    public boolean containsValue(Object o) {
        if (o == null) {
            return false;
        }
        
        for (V v : this.values()) {
            if (v.equals(o)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public V get(Object o) {
        OwnLinkedList<MapItem<K, V>> list = getListForKey(o, false);
        if (list == null) {
            return null;
        }
        
        for (MapItem<K, V> item : list) {
            if (o.equals(item.getKey())) {
                return item.getValue();
            }
        }
        
        return null;
    }

    @Override
    public V put(K k, V v) {
        OwnLinkedList<MapItem<K, V>> list = getListForKey(k, true);
        V prev = null;
        
        for (MapItem<K, V> item : list) {
            if (item.getKey().equals(k)) {
                prev = item.getValue();
                item.setValue(v);
            }
        }
        
        if (prev == null) {
            list.add(new MapItem<>(k, v));
            itemsCount++;
            growIfNeeded();
        }
        return prev;
    }

    @Override
    public V remove(Object o) {
        OwnLinkedList<MapItem<K, V>> list = getListForKey(o, false);
        if (list == null) {
            return null;
        }
        
        ListIterator<MapItem<K, V>> listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            MapItem<K, V> entry = listIterator.next();
            if (entry.getKey().equals(o)) {
                listIterator.remove();
                itemsCount--;
                return entry.getValue();
            }
        }
        
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        itemsCount = 0;
        for (int i = 0; i < items.length; i++) {
            items[i] = null;
        }
    }

    @Override
    public Set<K> keySet() {
        Set<K> keys = new OwnSet<>();
        for (OwnLinkedList<MapItem<K, V>> list : items) {
            if (list != null) {
                for (MapItem<K, V> entry : list) {
                    keys.add(entry.getKey());
                }
            }
        }
        
        return keys;
    }

    @Override
    public Collection<V> values() {
        OwnLinkedList<V> out = new OwnLinkedList<>();
        for (OwnLinkedList<MapItem<K, V>> list : items) {
            if (list != null) {
                for (MapItem<K, V> item : list) {
                    out.add(item.getValue());
                }
            }
        }
        
        return out;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entries = new OwnSet<>();
        for (OwnLinkedList<MapItem<K, V>> list : items) {
            if (list != null) {
                for (MapItem<K, V> entry : list) {
                    entries.add(entry);
                }
            }
        }
        
        return entries;
    }
}
