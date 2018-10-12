package com.edii.spc.datastructures;

import java.util.Collection;
import java.util.Set;

/**
 * Abstrakti yläluokka Set-rajapinnan toteuttaville luokille.
 * Käytetään OwnSet-luokassa, sekä muutamassa OwnMap-luokan sisäisessä aliluokassa 
 * niiden metodien osalta, joihin ei ole toteutuskohtaista suorituskykyvaikutusta,
 * ja joita ei ole toteutettu OwnAbstractCollection-luokassa.
 */
public abstract class OwnAbstractSet<E> extends OwnAbstractCollection<E> implements Set<E> {
    /**
     * Tarkistaa joukkojen yhtäsuuruudeen.
     * Joukot ovat yhtäsuuret jos ne ovat saman kokoiset ja sisältävät samat alkiot.
     * @param o Toinen objekti, jota vasten tarkistus tehdään.
     * @return true, jos joukot ovat samat.
     */
    @Override
    public boolean equals(Object o) {
        return o == this || (o instanceof Set && ((Set) o).size() == size() && containsAll((Set) o));
    }

    /**
     * Lisää kaikki annetun tietorakenteen alkiot joukkoon.
     *
     * @param clctn Tietorakenne, jonka alkiot lisätään.
     * @return true jos joukkoa muutettiin, eli aina kun clctn ei ole tyhjä.
     */
    @Override
    public boolean addAll(Collection<? extends E> clctn) {
        boolean changed = false;
        for (E item : clctn) {
            this.add(item);
            changed = true;
        }
        return changed;
    }
}
