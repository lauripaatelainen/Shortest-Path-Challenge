package com.edii.spc.datastructures;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Hajautustaulun oma toteutus.
 * Sisäisenä tietorakenteena on Javan taulukko, jota kasvatetaan aina tarpeen mukaan.
 * Oletusarvoisesti sisäisen tietorakenteen koko on 10, ja täyttöaste 0.75. 
 * Täyttöaste määrittää, missä vaiheessa sisäistä taulukkoa kasvatetaan. Jos hajautustaulun
 * sisältämien alkioiden ja sisäisen tietorakenteen koon suhde ylittää täyttöasteen, sisäisen
 * tietorakenteen koko tuplataan ja tässä yhteydessä kaikkien alkioiden sijainti hajautustaulussa
 * lasketaan uudelleen.
 * 
 * Kun listaan lisätään alkio, sen sijainti sisäisessä taulukoossa lasketaan lisättävän
 * alkion hashCode()-metodilla modulo tietorakenteen koko. Useilla alkioilla voi olla
 * sama taulukon sijainti. Törmäykset on tässä toteutuksessa ratkaistu niin, että taulukko
 * ei sisällä suoraan elementtejä, vaan linkitetyn listan, joka sisältää avain-arvo pareja. 
 * 
 * @param <K> Tietotyyppi, jonka alkioita avaimet on
 * @param <V> Tietotyyppi, jonka alkioita arvot on
 */
public class OwnMap<K, V> implements Map<K, V> {
    /**
     * Sisäinen luokka avain-arvo parille. Toteuttaa javan yleisen rajapinnan Map.Entry<K, V>
     * 
     * @param <K> Avaimen tyyppi
     * @param <V> Arvon tyyppi
     */
    private static class MapItem<K, V> implements Map.Entry<K, V> {
        private final K key;
        private V value;
        
        public MapItem(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Palauttaa avain-arvo -parin avaimen.
         * @return Avain
         */
        @Override
        public K getKey() {
            return key;
        }

        /**
         * Palauttaa avain-arvo -parin arvon.
         * @return Arvo
         */
        @Override
        public V getValue() {
            return value;
        }

        /**
         * Asettaa avain-arvo -pariin uuden arvon.
         * @param value Uusi arvo
         */
        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }

        @Override
        public int hashCode() {
            return (this.getKey()==null   ? 0 : this.getKey().hashCode()) ^ (this.getValue()==null ? 0 : this.getValue().hashCode());
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
    
    /**
     * Oletusarvo sisäinen tietorakenteen lähtökoolle.
     */
    private static final int DEFAULT_INITIAL_CAPACITY = 10;
    
    /**
     * Oletusarvo hajautustaulun täyttöasteelle.
     */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    
    /**
     * Hajautustaulun avaimia kuvaava joukko.
     * @see #keySet() 
     */
    private final KeySet keySet = new KeySet();
    
    /**
     * Hajautustaulun arvoja kuvaava kokoelma.
     * @see #values() 
     */
    private final ValueCollection valueCollection = new ValueCollection();
    
    /**
     * Hajautustaulun arvo-avain -pareja kuvaava joukko.
     * @see #entrySet() 
     */
    private final EntrySet entrySet = new EntrySet();
    private OwnLinkedList<MapItem<K, V>>[] items;
    private final float loadFactor;
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
    
    /**
     * Sisäinen apumetodi, joka hakee kyseiselle avaimelle oikean linkitetyn listan.
     * Valinnaisena valintana voidaan määritellä että lista luodaan, jos sitä ei entuudestaan ole olemassa.
     * Hajautustaulun lisäysoperaatiot kutsuvat tätä metodia niin, että lista luodaan tarpeen mukaan.
     * @param key Avain
     * @param create Vipu, jolla määritellään luodaanko lista jos sitä ei entuudestaan ole olemassa. 
     * @return Linkitetty lista, tai null, jos sitä ei ole olemassa ja create-parametri on false.
     */
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
    
    /**
     * Tarkistaa onko hajautustaulun täyttö ylittänyt täyttöasteen ja kasvattaa
     * sisäistä tietorakennetta tarvittaessa.
     */
    private void growIfNeeded() {
        if ((float) itemsCount / (float) items.length >= loadFactor) {
            grow();
        }
    }
    
    /**
     * Kasvattaa sisäistä tietorakennetta.
     * Tietorakenteen koko tuplataan aina.
     */
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

    /**
     * Palauttaa hajautustaulun alkioiden määrän.
     * @return Hajautustaulun koko.
     */
    @Override
    public int size() {
        return itemsCount;
    }

    /**
     * Tarkistaa onko hajautustaulu tyhjä.
     * @return True, jos hajautustaulu on tyhjä.
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Tarkistaa sisältääkö hajutustaulu annetun avaimen.
     * Operaatiot ovat vakioaikaisia, joten aikavaativuus keskimäärin on O(1).
     * @param o Avain
     * @return True, jos avain löytyy hajautustaulusta.
     */
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

    /**
     * Tarkistaa sisältääkö hajautustaulu annetun arvon.
     * Hajautustaulun arvot tulee käydä läpi, joten aikavaativuus
     * on O(n)
     * @param o Arvo
     * @return True, jos arvo löytyy hajautustaulusta.
     */
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

    /**
     * Hakee hajautustaulusta annetulla avaimella löytyvän arvon.
     * @param o Avain
     * @return Avaimella löytynyt arvo, tai null, jos hajautustaulu ei sisällä arvoa kyseiselle avaimelle.
     */
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

    /**
     * Lisää hajautustauluun uuden avain-arvo -parin.
     * Jos annetulla avaimella löytyi entuudestaan arvo, se korvataan ja vanha arvo palautetaan.
     * @param k Avain
     * @param v Arvo
     * @return Vanha arvo, jos annetulla avaimella löytyi arvo ja se korvattiin.
     */
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

    /**
     * Poistaa annetulla avaimella löytyvän arvo-avain -parin hajautustaulusta.
     * @param o Avain
     * @return Poistettu arvo, tai null jos avaimella ei löytynyt arvoa.
     */
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

    /**
     * Poistaa annetulla avaimella löytyvän arvon vain jos arvo täsmää annettuun.
     * @param key Avain
     * @param value Arvo
     * @return true, jos arvo poistettiin.
     */
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

    /**
     * Lisää annetun hajautustaulun arvot tähän hajautustauluun.
     * @param map Taulu, jonka arvot lisätään.
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Tyhjentää hajautustaulun.
     */
    @Override
    public void clear() {
        itemsCount = 0;
        for (int i = 0; i < items.length; i++) {
            items[i] = null;
        }
    }

    /**
     * Palauttaa hajautustaulun avaimia kuvaavan joukon. 
     * Joukko tukee muokkausoperaatiota remove(), joka heijastuu
     * tähän hajautustauluun. 
     * @return Avaimia kuvaava joukko.
     */
    @Override
    public Set<K> keySet() {
        return keySet;
    }

    /**
     * Palauttaa hajautustaulun arvoja kuvaavan kokoelman.
     * Kokoelma tukee muokkausoperaatiota remove(), joka heijastuu
     * tähän hajautustauluun.
     * @return Arvoja kuvaava kokoelma.
     */
    @Override
    public Collection<V> values() {
        return valueCollection;
    }

    /**
     * Palauttaa hajautustaulun arvo-avain -pareja kuvaavan joukon.
     * Joukko tukee muokkausoperaatiota remove(), joka heijastuu
     * tähän hajautustauluun. 
     * @return Arvo-avain -pareja kuvaava joukko.
     */
    @Override
    public Set<Entry<K, V>> entrySet() {
        return entrySet;
    }

    @Override
    public int hashCode() {
        int sum = 0;
        for (Entry<K, V> entry : entrySet()) {
            sum += entry.hashCode();
        }
        return sum;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Map)) {
            return false;
        }
        return this.entrySet().equals(((Map) o).entrySet());
    }
    
    /**
     * Sisäinen aliluokka hajautustaulun avaimia kuvaavalle joukolle.
     * @see #keySet() 
     */
    private class KeySet extends OwnAbstractSet<K> implements Set<K> {
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
    
    private class ValueCollection extends OwnAbstractSet<V> implements Collection<V> {
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
    
    /**
     * Sisäinen aliluokka hajautustaulun arvo-avain -pareja kuvaavalle joukolle.
     * @see #entrySet() 
     */
    private class EntrySet extends OwnAbstractSet<Map.Entry<K, V>> implements Set<Map.Entry<K, V>> {
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
    
    /**
     * Sisäinen aliluokka iteraattorille, jota käytetään
     * {@link #keySet()}, {@link #values()} ja {@link #entrySet()} metodien
     * palauttamissa kokoelmissa.
     */
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
    
    /**
     * Sisäinen aliluokka {@link #keySet()} -metodin palauttaman joukon iteraattorille.
     */
    private class KeySetIterator extends MapIterator implements Iterator<K> {
        @Override
        public K next() {
            return nextMapItem().getKey();
        }
    }
    
    /**
     * Sisäinen aliluokka {@link #values()} -metodin palauttaman kokoelman iteraattorille.
     */
    private class ValueCollectionIterator extends MapIterator implements Iterator<V> {
        @Override
        public V next() {
            return nextMapItem().getValue();
        }
    }
    
    /**
     * Sisäinen aliluokka {@link #entrySet()} -metodin palauttaman kokoelman iteraattorille.
     */
    private class EntrySetIterator extends MapIterator implements Iterator<Map.Entry<K, V>> {
        @Override
        public Entry<K, V> next() {
            return nextMapItem();
        }
    }
}
