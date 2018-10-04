package com.edii.spc.datastructures;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * Abstrakti yläluokka List-rajapinnan toteuttaville luokille.
 * Käytetään OwnLinkedList ja OwnList -luokissa niiden metodien osalta, joihin ei ole toteutuskohtaista suorituskykyvaikutusta. 
 */
public abstract class OwnAbstractList<E> implements List<E> {
    /**
     * Kertoo onko lista tyhjä.
     * @return true jos lista on tyhjä
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
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
        
        for (E item : this) {
            if (item.equals(o)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Muuttaa listan taulukkomuotoon.
     * 
     * @return 
     */
    @Override
    public Object[] toArray() {
        Object[] out = new Object[size()];
        return toArray(out);
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
        if (ts.length < size()) {
            ts = (T[]) new Object[size()];
        }
        
        int i = 0;
        for (E item : this) {
            ts[i] = (T) item;
        }
        
        return ts;
    }
    
    /**
     * Poistaa annetun alkion listasta.
     * 
     * @param o Alkio joka tulee poistaa
     * @return Palauttaa true jos alkio löytyi, false jos ei löytynyt.
     */
    @Override
    public boolean remove(Object o) {
        ListIterator<E> listIterator = this.listIterator();
        while (listIterator.hasNext()) {
            E item = listIterator.next();
            if (item.equals(o)) {
                listIterator.remove();
                return true;
            }
        }
        return false;
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
        ListIterator<E> listIterator = this.listIterator();
        boolean changed = false;
        while (listIterator.hasNext()) {
            E item = listIterator.next();
            if (clctn.contains(item)) {
                listIterator.remove();
                changed = true;
            }
        }
        return changed;
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
        int i = 0;
        for (E item : this) {
            if (item.equals(o)) {
                return i;
            }
            i++;
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
        int i = size() - 1;
        ListIterator<E> listIterator = this.listIterator(i);
        while (listIterator.hasPrevious()) {
            E item = listIterator.previous();
            if (item.equals(o)) {
                return i;
            }
            i--;
        }
        return -1;
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
        int i = start;
        ListIterator<E> listIterator = this.listIterator(start);
        List<E> newList = new OwnList<>();
        while (i < end && listIterator.hasNext()) {
            newList.add(listIterator.next());
            i++;
        }
        return newList;
    }
    
    /**
     * Palauttaa ListIterator-rajapinnan toteuttavan objektin.
     * Kutsuu listIterator(0) -metdia.
     * 
     * @return ListIterator
     * @throws UnsupportedOperationException jos toteuttava luokka ei tue listIterator(int) metodia.
     */
    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }
}
