package com.edii.spc.datastructures;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Minimikeko. 
 * 
 * Toteutettu binäärikekona.
 * 
 * Tukee toimintoja add(T item), decreaseKey(T item) ja extractMin(), joiden kaikkien aikavaativuus on O(log n)
 * 
 * @param <T> Tietotyyppi jonka alkoita minimikeko sisältää. 
 * @see OwnAbstractCollection
 */
public class MinHeap<T> extends OwnAbstractCollection<T> {
    private static final int DEFAULT_MIN_HEAP_SIZE = 10;
    
    private List<T> list;
    private Map<T, Integer> indices;
    private final Comparator<T> comparator;
    
    /**
     * Luo minimikeon luonnollisella järjestyksellä.
     */
    public MinHeap() {
        this(new Comparator<T>() {
            @Override
            public int compare(T obj1, T obj2) {
                Comparable<T> comp1 = (Comparable<T>) obj1;
                return comp1.compareTo(obj2);
            }
        });
    }
    
    /**
     * Luo minimikeon annetulla vertailufunktiolla. 
     * 
     * @param comparator Vertailufunktio
     */
    public MinHeap(Comparator<T> comparator) {
        this.list = new OwnList<>(DEFAULT_MIN_HEAP_SIZE);
        this.indices = new OwnMap<>(DEFAULT_MIN_HEAP_SIZE);
        this.comparator = comparator;
    }
    
    /**
     * Luo minimikeon annetusta listasta annetulla vertailufunktiolla.
     * 
     * @param list Lista
     * @param comparator Vetailufunktio
     */
    public MinHeap(Collection<T> list, Comparator<T> comparator) {
        this.list = new OwnList<>(list);
        this.indices = new OwnMap<>(list.size());
        for (int i = 0; i < this.list.size(); i++) {
            indices.put(this.list.get(i), i);
        }
        
        this.comparator = comparator;
        buildMinHeap();
    }
    
    
    /**
     * Rakentaa minimikeon nykyisistä listan alkioista.
     */
    private void buildMinHeap() {
        for (int i = parent(size()); i >= 0; i--) {
            this.minHeapify(i);
        }
    }
    
    /**
     * Käytetään minimikeon ehdon ylläpitämiseen.
     */
    private void minHeapify(int i) {
        int l = left(i);
        int r = right(i);
        int smallest;
        if (l < size() && comparator.compare(list.get(l), list.get(i)) < 0) {
            smallest = l;
        } else {
            smallest = i;
        }
        
        if (r < size() && comparator.compare(list.get(r), list.get(smallest)) < 0) {
            smallest = r;
        }
        if (smallest != i) {
            exchange(i, smallest);
            minHeapify(smallest);
        }
    }
    
    /**
     * Vaihtaa alkioiden paikkoja, jotka sijaitsevat annetuissa listan indekseissä.
     * 
     * @param i ensimmäisen alkion indeksi
     * @param j toisen alkion indeksi
     */
    private void exchange(int i, int j) {
        T tmp = list.get(i);
        this.setListItem(i, list.get(j));
        this.setListItem(j, tmp);
    }
    
    /**
     * Palauttaa indeksin, josta annetun indeksin vasen alipuu alkaa.
     * 
     * @param i indeksi, josta vasen alipuu lasketaan.
     * @return Vasemman alipuun indeksi
     */
    private int left(int i) {
        return 2 * i + 1;
    }
    
    /**
     * Palauttaa indeksin, josta annetun indeksin oikea alipuu alkaa.
     * 
     * @param i indeksi josta oikea alipuu lasketaan.
     * @return Oikean alipuun indeksi
     */
    private int right(int i) {
        return 2 * i + 2;
    }
    
    /**
     * Palauttaa indeksin, josta annetun indeksin yläpuu alkaa.
     * 
     * @param i indeksi, josta yläpuu lasketaan.
     * @return Yläpuun indeksi
     */
    private int parent(int i) {
        return (i - 1) / 2;
    }
    
    /**
     * Lisää alkion minimikekoon.
     * 
     * @param item Lisättävä alkio
     * @return Palauttaa aina true
     */
    @Override
    public boolean add(T item) {
        if (indices.containsKey(item)) {
            throw new IllegalArgumentException("Löytyy jo");
        }
        
        this.addListItem(item);
        decreaseKey(size() - 1);
        return true;
    }
    
    /**
     * Lisää alkion sisäisen listan indeksiin i ja päivittää indeksitaulun.
     * 
     * @param i Listan indeksi
     * @param item Lisättävä alkio
     */
    private void setListItem(int i, T item) {
        this.list.set(i, item);
        this.indices.put(item, i);
    }
    
    /**
     * Lisää uuden alkion sisäisen listan loppuun ja päivittää indeksitaulun.
     * 
     * @param item Lisättävä alkio
     */
    private void addListItem(T item) {
        this.list.add(item);
        this.indices.put(item, this.list.size() - 1);
    }
    
    /**
     * Poistaa minimikeosta pienimmän alkion.
     * 
     * @return Poistaa ja palauttaa minimikeon pienimmän alkion.
     */
    public T extractMin() {
        T min = list.get(0);
        if (size() == 1) {
            clear();
        } else {
            this.setListItem(0, list.remove(list.size() - 1));
            this.indices.remove(min);
            minHeapify(0);
        }
        return min;
    }
    
    /**
     * Selvittää onko keko tyhjä.
     * 
     * @return Palauttaa true jos minimikeko on tyhjä
     */
    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }
    
    /**
     * Ilmoittaa minimikeolle että annetun alkion avain on pienentynyt, ja sen sijainti keossa tulee laskea uudestaan.
     * 
     * @param item Muuttunut alkio
     */
    public void decreaseKey(T item) {
        decreaseKey(indices.get(item));
    }
    
    /**
     * Ilmoittaa minimikeolle että annetun alkion avain on pienentynyt, ja sen sijainti keossa tulee laskea uudestaan.
     * 
     * Tätä funktiota voi käyttää vain jos tietää sisäisen listan indeksin kyseiselle alkiolle. 
     * 
     * @param i Muuttuneen alkion indeksi
     */
    private void decreaseKey(int i) {
        while (i >= 0 && comparator.compare(list.get(parent(i)), list.get(i)) > 0) {
            exchange(parent(i), i);
            i = parent(i);
        }
    }
    
    /**
     * Palauttaa minikeon alkioiden määrän.
     * 
     * @return Alkioiden määrä
     */
    @Override
    public int size() {
        return list.size();
    }
    
    /**
     * Tyhjentää minimikeon.
     */
    @Override
    public void clear() {
        list.clear();
        indices.clear();
    }
    
    /**
     * Poistaa annetun alkion minimikeosta.
     */
    @Override
    public boolean remove(Object obj) {
        if (indices.containsKey((T) obj)) {
            Integer i = indices.get((T) obj);
            setListItem(i, list.get(size() - 1));
            indices.remove((T) obj);
            list.remove(size() - 1);
            minHeapify(i);
            return true;
        }
        
        return false;
    }

    /**
     * Tarkistaa kuuluuko annettu alkio minimikekoon. 
     * 
     * @param o Tarkistettava alkio.
     * @return Palauttaa true jos alkio on keon jäsen.
     */
    @Override
    public boolean contains(Object o) {
        return indices.containsKey((T) o);
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    /**
     * Lisää kaikki annetun tietorakenteen alkiot minimikekoon.
     * 
     * @param clctn Tietorakenne jonka alkiot lisätään
     * @return Palauttaa true, jos minimikeko muuttui, eli jos clctn ei ole tyhjä
     */
    @Override
    public boolean addAll(Collection<? extends T> clctn) {
        boolean changed = false;
        for (T t : clctn) {
            add(t);
            changed = true;
        }
        return changed;
    }
}
