package com.edii.spc.datastructures;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
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
    private static class MapItem<K, V> implements Map.Entry<K, V> {
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
    
    private KeySet keySet = new KeySet();
    private ValueCollection valueCollection = new ValueCollection();
    private EntrySet entrySet = new EntrySet();
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
        Object[] entries = entrySet().toArray();
        
        for (int i = 0; i < items.length; i++) {
            items[i] = null;
        }
        
        items = new OwnLinkedList[items.length * 2];
        itemsCount = 0;
        
        for (Object obj : entries) {
            Entry<K, V> entry = (Entry<K, V>) obj;
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
        List<MapItem<K, V>> list = getListForKey(o, false);
        if (list == null) {
            return false;
        }
        
        for (MapItem<K, V> item : list) {
            if (o.equals(item.getKey())) {
                return true;
            }
        }
        
        return false;
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
    public boolean remove(Object key, Object value) {
        OwnLinkedList<MapItem<K, V>> list = getListForKey(key, false);
        if (list == null) {
            return false;
        }
        
        ListIterator<MapItem<K, V>> listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            MapItem<K, V> entry = listIterator.next();
            if (entry.getKey().equals(key) && entry.getValue().equals(value)) {
                listIterator.remove();
                itemsCount--;
                return true;
            }
        }
        
        return false;
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
        return keySet;
    }

    @Override
    public Collection<V> values() {
        return valueCollection;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return entrySet;
    }
    
    private class KeySet extends OwnAbstractCollection<K> implements Set<K> {
        @Override
        public int size() {
            return OwnMap.this.size();
        }

        @Override
        public boolean contains(Object o) {
            return OwnMap.this.containsKey(o);
        }

        @Override
        public Iterator iterator() {
            return new KeySetIterator();
        }

        @Override
        public boolean add(Object e) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public boolean remove(Object o) {
            if (OwnMap.this.containsKey(o)) {
                OwnMap.this.remove(o);
                return true;
            }
            
            return false;
        }

        @Override
        public void clear() {
            OwnMap.this.clear();
        }
        
    }
    
    private class ValueCollection extends OwnAbstractCollection<V> implements Collection<V> {
        @Override
        public int size() {
            return OwnMap.this.size();
        }

        @Override
        public boolean contains(Object o) {
            return OwnMap.this.containsValue(o);
        }

        @Override
        public Iterator<V> iterator() {
            return new ValueCollectionIterator();
        }

        @Override
        public boolean add(Object e) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public boolean remove(Object o) {
            Iterator<V> it = this.iterator();
            while (it.hasNext()) {
                V val = it.next();
                if (val == o || (val != null && val.equals(o))) {
                    it.remove();
                    return true;
                }
            }
            
            return false;
        }

        @Override
        public void clear() {
            OwnMap.this.clear();
        }
        
    }
    
    private class EntrySet extends OwnAbstractCollection<Map.Entry<K, V>> implements Set<Map.Entry<K, V>> {
        @Override
        public int size() {
            return OwnMap.this.size();
        }

        @Override
        public boolean contains(Object o) {
            if (o == null) {
                return false;
            }
            
            Entry entry = (Entry) o;
            V val = OwnMap.this.get(entry.getKey());
            if (val == entry.getValue() || (val != null && val.equals(entry.getValue()))) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new EntrySetIterator();
        }

        @Override
        public boolean add(Entry<K, V> e) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public boolean remove(Object o) {
            Entry entry = (Entry) o;
            return OwnMap.this.remove(entry.getKey(), entry.getValue());
        }

        @Override
        public void clear() {
            OwnMap.this.clear();
        }
    }
    
    private abstract class MapIterator {
        private int i = 0;
        private Iterator<MapItem<K, V>> it;
        
        public MapIterator() {
            findNext();
        }
        
        private void findNext() {
            if (it != null) {
                if (it.hasNext()) {
                    return;
                }

                it = null;
                i++;
            }
            
            while (i < items.length) {
                if (items[i] != null && items[i].size() > 0) {
                    it = items[i].iterator();
                    return;
                }
                i++;
            }
        }

        public boolean hasNext() {
            return this.it != null && this.it.hasNext();
        }

        public Map.Entry<K, V> nextMapItem() {
            MapItem<K, V> item = it.next();
            findNext();
            return item;
        }

        public void remove() {
            it.remove();
            itemsCount--;
        }
    }
    
    private class KeySetIterator extends MapIterator implements Iterator<K> {
        @Override
        public K next() {
            return nextMapItem().getKey();
        }
    }
    
    private class ValueCollectionIterator extends MapIterator implements Iterator<V> {
        @Override
        public V next() {
            return nextMapItem().getValue();
        }
    }
    
    private class EntrySetIterator extends MapIterator implements Iterator<Map.Entry<K, V>> {
        @Override
        public Entry<K, V> next() {
            return nextMapItem();
        }
    }
}
