package com.edii.spc.datastructures;

import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * Javan ArrayList-tietorakennetta vastaava oma tietorakenne. 
 * null-arvot ei ole sallittuja.
 */
public class OwnList<E> extends OwnAbstractList<E> {
    /**
     * Talukko, jossa listan alkiot säilytetään. 
     */
    private E[] items;
    
    /**
     * Listan tämän hetkinen koko.
     */
    private int size;
    
    /**
     * Luo tyhjän listan.
     */
    public OwnList() {
        this(2);
    }
    
    /**
     * Luo tyhjän listan annetun kokoisella tietorakenteella.
     * 
     * @param initialCapacity Tietorakenteen koko
     */
    public OwnList(int initialCapacity) {
        items = (E[]) new Object[initialCapacity];
        size = 0;
    }
    
    /**
     * Luo listan annetuilla alkioilla.
     * 
     * @param items Alkiot jotka listaan lisätään
     */
    public OwnList(Collection<E> items) {
        this(items.size());
        this.addAll(items);
    }
    
    /**
     * Kasvattaa listaa, jotta uusia alkioita mahtuu. 
     * Tuplaa listan koon. 
     */
    private void grow() {
        E[] newItems = (E[]) new Object[items.length * 2];
        for (int i = 0; i < this.size; i++) {
            newItems[i] = items[i];
        }
        items = newItems;
    }

    /**
     * Siirtää listan alkioita eteenpäin annetun määrän. 
     * Tarvittaessa kasvattaa taulukon kokoa, mutta ei muuta size-parametrin määrää.
     * 
     * @param i indeksi, josta alkaen loppuja alkioita siirretään
     * @param count siirrettävä määrä
     */
    private void shift(int i, int count) {
        while (items.length < this.size + count) {
            grow();
        }

        for (int j = this.size - 1; j >= i; j--) {
            this.items[j + count] = this.items[j];
        }
        size += count;
    }
    
    /**
     * Siirtää listan alkioita taaksepäin annetun määrän. 
     * Ei muuta size-parametria.
     * 
     * @param i indeksi, josta alkaen loppuja alkioita siirretään
     * @param count siirrettävä määrä
     */
    private void shiftBack(int i, int count) {
        for (int j = i; j < size; j++) {
            this.items[j - count] = this.items[j];
        }
    }

    /**
     * Palauttaa listan koon.
     * 
     * @return Palauttaa listan koon.
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Luo listasta iteraattorin.
     * 
     * @return Palauttaa listan iteraattorin.
     */
    @Override
    public Iterator<E> iterator() {
        return new OwnListIterator(0);
    }

    /**
     * Lisää annetun alkion listan viimeiseksi. 
     * 
     * @param e Lisättävä alkio
     * @return Palauttaa aina true
     */
    @Override
    public boolean add(E e) {
        if (size == this.items.length) {
            grow();
        }
        
        this.size++;
        this.set(this.size - 1, e);
        return true;
    }

    /**
     * Lisää kaikki annetun tietorakenteen alkiot annettuun listan indeksiin. 
     * Kyseisestä indeksistä eteenpäin olevia alkioita siirretään tarvittava määrä eteenpäin. 
     * 
     * @param i Indeksi, mihin alkiot lisätään.
     * @param clctn Tietorakenne, jonka alkiot lisätään.
     * @return true jos listaa muutettiin, eli aina kun clctn ei ole tyhjä.
     */
    @Override
    public boolean addAll(int i, Collection<? extends E> clctn) {
        if (clctn.isEmpty()) {
            return false;
        }
        
        shift(i, clctn.size());
        
        int j = 0;
        for (E item : clctn) {
            set(i + j, item);
            j++;
        }
        
        this.size += clctn.size();
        
        return true;
    }

    /**
     * Tyhjentää listan.
     */
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            items[i] = null;
        }
        this.size = 0;
    }

    /**
     * Hakee annetussa indeksissä olevan alkion.
     * 
     * @param i indeksi, jossa oleva alkio haetaan.
     * @return Palauttaa annetussa indeksissä olevan alkion.
     */
    @Override
    public E get(int i) {
        if (i >= size) {
            throw new IndexOutOfBoundsException(String.format("%d >= %d", i, size));
        }
        if (i < 0) {
            throw new IndexOutOfBoundsException(String.format("%d < 0", i));
        }
        
        return items[i];
    }

    /**
     * Asettaa alkion annettuun indeksiin. 
     * 
     * @param i indeksi mihin alkio asetetaan.
     * @param e alkio joka lisätään.
     * @return alkio, joka oli samassa indeksissä aiemmin.
     */
    @Override
    public E set(int i, E e) {
        if (i >= size) {
            throw new IndexOutOfBoundsException(String.format("%d >= %d", i, size));
        }
        if (i < 0) {
            throw new IndexOutOfBoundsException(String.format("%d < 0", i));
        }
        
        E prev = items[i];
        items[i] = e;
        return prev;
    }

    /**
     * Lisää alkion annetuun listan indeksiin. 
     * Indeksin jälkeen tulevia alkioita siirretään eteenpäin. 
     * 
     * @param i indeksi
     * @param e lisättävä alkio.
     */
    @Override
    public void add(int i, E e) {
        if (i < 0 || i > size()) {
            throw new IndexOutOfBoundsException();
        }
        shift(i, 1);
        set(i, e);
    }

    /**
     * Poistaa alkion annetusta indeksistä. 
     * 
     * @param i indeksi, josta alkio poistetaan.
     * @return poistettu alkio.
     */
    @Override
    public E remove(int i) {
        E item = items[i];
        shiftBack(i + 1, 1);
        this.size--;
        return item;
    }

    /**
     * Palauttaa ListIterator-objektin listaan kohdistuvia toimintoja varten.
     * 
     * @param i indeksi
     * @return Palautta ListIterator-objektin.
     */
    @Override
    public ListIterator<E> listIterator(int i) {
        return new OwnListIterator(i);
    }
    
    private class OwnListIterator implements ListIterator<E> {
        private int i = 0;
        private int last = -1;
        
        public OwnListIterator(int i) {
            this.i = i;
        }
        
        @Override
        public boolean hasPrevious() {
            return i > 0;
        }

        @Override
        public boolean hasNext() {
            return i < size();
        }

        @Override
        public E previous() {
            i--;
            last = i;
            return get(i);
        }
        
        @Override
        public E next() {
            last = i;
            return items[i++];
        }

        @Override
        public int nextIndex() {
            return i;
        }

        @Override
        public int previousIndex() {
            return i - 1;
        }

        @Override
        public void remove() {
            OwnList.this.remove(last);
            i--;
        }

        @Override
        public void set(E e) {
            OwnList.this.set(last, e);
        }

        @Override
        public void add(E e) {
            OwnList.this.set(last, e);
        }
    }
}
