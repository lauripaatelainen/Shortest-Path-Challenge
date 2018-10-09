package com.edii.spc.datastructures;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

/**
 * Abstrakti yläluokka List-rajapinnan toteuttaville luokille.
 * Käytetään OwnLinkedList ja OwnList -luokissa niiden metodien osalta, joihin ei ole toteutuskohtaista suorituskykyvaikutusta. 
 */
public abstract class OwnAbstractList<E> extends OwnAbstractCollection<E> implements List<E> {
    /**
     * Tarkistaa onko annettu alkio listassa.
     * 
     * @param o Alkio jonka olemassaoloa tarkistetaan.
     * @return Palauttaa true jos alkio löytyy listasta.
     */
    @Override
    public boolean contains(Object o) {
        for (E item : this) {
            if (o == null && item == null) {
                return true;
            } else if (o.equals(item)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Poistaa annetun alkion listasta.
     * 
     * @param o Alkio joka tulee poistaa
     * @return Palauttaa true jos alkio löytyi, false jos ei löytynyt.
     */
    @Override
    public boolean remove(Object o) {
        Iterator<E> iterator = this.iterator();
        while (iterator.hasNext()) {
            E item = iterator.next();
            if (item.equals(o)) {
                iterator.remove();
                return true;
            }
        }
        return false;
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
        int i = size();
        ListIterator<E> listIterator = this.listIterator(i);
        while (listIterator.hasPrevious()) {
            E item = listIterator.previous();
            i--;
            if (item.equals(o)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Ei tuettu toiminto.
     * Javan List-rajapinnan kuvauksen mukaan kuuluisi olla pakollinen toiminto,
     * mutta tämän sovelluksen kannalta subListin toteutus on turhan työläs. subListin
     * kuuluisi palauttaa uusi lista, johon tehdyt muutokset heijastuvat alkuperäiseen listaan.
     * Jotta tämän saisi toteutettua tehokkaasti, pitäisi eri listan toteutuksilla
     * olla oma toteutus subListista. 
     * @param start listan alku
     * @param end listan loppu
     * @return Listan osa (ei tuettu)
     */
    @Override
    public List<E> subList(int start, int end) {
        throw new UnsupportedOperationException("Ei tuettu toiminto.");
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

    /**
     * Tarkistaa kahden listan yhtäsuuruuden. 
     * Tarkistuksessa käydään molemmat listat läpi, ja varmistetaan että samat
     * elementit löytyvät molemmista listoista samassa järjestyksessä. 
     * 
     * @param other Toinen lista
     * @return Palauttaa true, jos listat ovat identtiset.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (other == null || !(other instanceof List) || ((List) other).size() != size()) {
            return false;
        }
        
        Iterator thisIt = iterator();
        Iterator oIt = ((List) other).iterator();
        while (thisIt.hasNext()) {
            Object thisVal = thisIt.next();
            Object oVal = oIt.next();
            if (!Objects.equals(thisVal, oVal)) {
                return false;
            }
        }
        return true;
    }
}
