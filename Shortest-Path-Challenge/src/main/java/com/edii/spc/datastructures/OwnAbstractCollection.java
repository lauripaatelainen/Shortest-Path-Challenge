package com.edii.spc.datastructures;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

public abstract class OwnAbstractCollection<E> implements Collection<E> {
    private E[] e = (E[]) Array.newInstance(this.getClass(), 1);

    /**
     * Kertoo onko lista tyhjä.
     *
     * @return true jos lista on tyhjä
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Muuttaa listan taulukkomuotoon.
     *
     * @return Palauttaa listan taulukkomuodossa.
     */
    @Override
    public Object[] toArray() {
        Object[] out = new Object[size()];
        return toArray(out);
    }

    /**
     * Muuttaa listan taulukkomuotoon. Jos annettu taulukko on riittävän suuri,
     * listan alkiot lisätään siihen. Jos taulukko on yli listan koko, viimeisen
     * alkion perään lisätään null-arvo. Jos annettu taulukko ei riitä,
     * allokoidaan uusi taulukko ja palautetaan se.
     *
     * @param <T> Tyyppiparametri, minkä tyyppinen lista tehdään.
     * @param ts Taulukko, johon listan alkiot lisätään, jos se on tarpeeksi
     * suuri.
     * @return Palauttaa taulukon, jossa on listan elementit.
     * @throws ArrayStoreException jos jokin listan elementti ei ole
     * käännettävissä muotoon T.
     */
    @Override
    public <T> T[] toArray(T[] ts) {
        if (ts.length < size()) {
            ts = (T[]) Array.newInstance(ts.getClass().getComponentType(), size());
        }

        int i = 0;
        for (E item : this) {
            ts[i] = (T) item;
            i++;
        }

        return ts;
    }

    /**
     * Tarkistaa kuuluuko kaikki annetun tietorakenteen alkiot listaan.
     *
     * @param clctn Tietorakenne, jonka alkioiden listaan kuuluvuus
     * tarkistetaan.
     * @return true jos kaikki kuuluu
     */
    @Override
    public boolean containsAll(Collection<?> clctn) {
        for (Object obj : clctn) {
            if (!this.contains((E) obj)) {
                return false;
            }
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
            if (this.remove((E) obj)) {
                changed = true;
            }
        }
        return changed;
    }

    /**
     * Poistaa listasta kaikki muut paitsi annetussa tietorakenteessa olevat
     * alkiot.
     *
     * @param clctn Tietorakenne, jonka sisältämät alkiot listassa säilytetään.
     * @return true jos lista muuttui
     */
    @Override
    public boolean retainAll(Collection<?> clctn) {
        Iterator<E> iterator = this.iterator();
        boolean changed = false;
        while (iterator.hasNext()) {
            E item = iterator.next();
            if (!clctn.contains(item)) {
                iterator.remove();
                changed = true;
            }
        }
        return changed;
    }
}
