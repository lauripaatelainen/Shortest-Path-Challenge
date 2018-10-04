package com.edii.spc.datastructures;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Javan ArrayList-tietorakennetta vastaava oma tietorakenne. 
 * 
 * Toistaiseksi extendaa suoraan ArrayListin ja omat metoditoteutukset tehdään myöhemmin. 
 */
public class OwnList<E> implements List<E> {
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
        while(items.length < this.size + count) {
            grow();
        }

        for (int j = this.size - 1; j >= i; j--) {
            this.items[j + count] = this.items[j];
        }
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
     * Kertoo onko lista tyhjä.
     * 
     * @return Palauttaa true jos lista on tyhjä.
     */
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Tarkistaa onko annettu alkio listassa.
     * 
     * @param o Alkio jonka olemassaoloa tarkistetaan.
     * @return Palauttaa true jos alkio löytyy listasta.
     */
    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        
        for (int i = 0; i < size; i++) {
            if (get(i).equals(o)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Luo listasta iteraattorin.
     * 
     * @return Palauttaa listan iteraattorin.
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int i = 0;
            
            @Override
            public boolean hasNext() {
                return i < size();
            }

            @Override
            public E next() {
                return items[i++];
            }
        };
    }

    /**
     * Muuttaa listan taulukkomuotoon.
     * 
     * @return 
     */
    @Override
    public Object[] toArray() {
        Object[] out = new Object[this.size];
        for (int i = 0; i < this.size; i++) {
            out[i] = (Object) items[i];
        }
        return out;
    }

    /**
     * Muuttaa listan taulukkomuotoon. 
     * Jos annettu taulukko on riittävän suuri, listan alkiot lisätään siihen. Jos taulukko on yli listan koko, viimeisen alkion perään lisätään null-arvo. 
     * Jos annettu taulukko ei riitä, allokoidaan uusi taulukko ja palautetaan se.
     * 
     * @param <T> Tyyppiparametri, minkä tyyppinen lista tehdään. 
     * @param ts Taulukko, johon listan alkiot lisätään, jos se on tarpeeksi suuri.
     * @return Palauttaa taulukon, jossa on listan elementit. 
     * @throws ArrayStoreException jos jokin listan elementti ei ole käännettävissä muotoon T. 
     */
    @Override
    public <T> T[] toArray(T[] ts) {
        T[] out;
        if (ts.length < this.size) {
            out = (T[]) new Object[this.size];
        } else {
            out = ts;
            if (ts.length > this.size) {
                out[this.size] = null;
            }
        }
        try {
            for (int i = 0; i < this.size; i++) {
                out[i] = (T) this.items[i];
            }
        } catch (Exception e) {
            throw new ArrayStoreException();
        }
        return out;
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
        this.set(this.size- 1, e);
        return true;
    }

    /**
     * Poistaa annetun alkion listasta.
     * 
     * @param o Alkio joka tulee poistaa
     * @return Palauttaa true jos alkio löytyi, false jos ei löytynyt.
     */
    @Override
    public boolean remove(Object o) {
        int idx = this.indexOf(o);
        if (idx == -1) {
            return false;
        } else {
            remove(idx);
            return true;
        }
    }

    /**
     * Tarkistaa kuuluuko kaikki annetun tietorakenteen alkiot listaan.
     * 
     * @param clctn Tietorakenne, jonka alkioiden listaan kuuluvuus tarkistetaan.
     * @return true jos kaikki kuuluu
     */
    @Override
    public boolean containsAll(Collection<?> clctn) {
        for (Object obj : clctn) {
            if (!this.contains(obj)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Lisää kaikki annetun tietorakenteen alkiot listan loppuun.
     * 
     * @param clctn Tietorakenne, jonka alkiot lisätään.
     * @return true jos listaa muutettiin, eli aina kun clctn ei ole tyhjä.
     */
    @Override
    public boolean addAll(Collection<? extends E> clctn) {
        if (clctn.isEmpty()) {
            return false;
        }
        
        for (E item : clctn) {
            this.add(item);
        }
        
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
     * Poistaa kaikki annetun tietorakenteen alkiot listasta.
     * 
     * @param clctn Tietorakenne, jonka sisältämät alkiot listasta poistetaan.
     * @return true, jos lista muuttui
     */
    @Override
    public boolean removeAll(Collection<?> clctn) {
        boolean changed = false;
        for (Object obj : clctn) {
            if (this.remove(obj)) {
                changed = true;
            }
        }
        return changed;
    }

    /**
     * Poistaa listasta kaikki muut paitsi annetussa tietorakenteessa olevat alkiot. 
     * 
     * @param clctn Tietorakenne, jonka sisältämät alkiot listassa säilytetään. 
     * @return true jos lista muuttui
     */
    @Override
    public boolean retainAll(Collection<?> clctn) {
        boolean changed = false;
        for (int i = 0; i < size;) {
            if (clctn.contains(items[i])) {
                i++;
            } else {
                remove(i);
                changed = true;
            }
        }
        return changed;
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
        if (e == null) {
            throw new IllegalArgumentException("Ei voi lisätä nullia listaan");
        }
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
     * @param e 
     */
    @Override
    public void add(int i, E e) {
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
     * Etsii annetun alkion indeksin listassa.
     * Jos sama alkio on listassa monta kertaa, palauttaa ensimmäisen esiintymän indeksin. 
     * 
     * @param o Etsittävä alkio
     * @return Palauttaa alkion indeksin, tai -1 jos listassa ei ole kyseistä alkiota. 
     */
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < this.size; i++) {
            if (this.items[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Etsii annetun alkion viimeisen esiintymän indeksin listassa.
     * Jos sama alkio on listassa monta kertaa, palauttaa viimeisen esiintymän indeksin. 
     * 
     * @param o Etsittävä alkio
     * @return Palauttaa alkion indeksin, tai -1 jos listassa ei ole kyseistä alkiota. 
     */
    @Override
    public int lastIndexOf(Object o) {
        for (int i = this.size - 1; i >= 0; i--) {
            if (this.items[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * listIterator-metodia ei tueta.
     * 
     * @return 
     * @throws UnsupportedOperationException aina
     */
    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * listIterator-metodia ei tueta.
     * 
     * @param i
     * @return 
     * @throws UnsupportedOperationException aina
     */
    @Override
    public ListIterator<E> listIterator(int i) {
        throw new UnsupportedOperationException("Not supported.");
    }

    /**
     * Palauttaa listan osan uutena listana.
     * 
     * @param start Alkuindeksi, tulee mukaan uuteen listaan.
     * @param end Loppuindeksi, ei tule mukaan listaan.
     * @return Palauttaa uuden osalistan.
     */
    @Override
    public List<E> subList(int start, int end) {
        List<E> newList = new OwnList<>();
        for (int i = start; i >= 0 && i < size && i < end; i++) {
            newList.add(this.get(i));
        }
        return newList;
    }
}
