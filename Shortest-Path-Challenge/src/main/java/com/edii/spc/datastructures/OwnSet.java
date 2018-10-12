package com.edii.spc.datastructures;

import java.util.Iterator;
import java.util.Map;

/**
 * Set-tietorakenteen toteuttava luokka. 
 * Toteutus käyttää sisäisestä OwnMap-luokkaa, jossa on jo toteutettuna
 * nopeat haku-, lisäys-, poisto-, ja jäsenyyden tarkistusoperaatiot.
 * Hajautustaulun osalta käytetään vain avainta, koska se on hajautustaulussa
 * yksilöllinen (kuten joukon alkiot). Hajautustaulun arvoa ei käytetä ollenkaan.
 * 
 * @param <T> Tietotyyppi, jonka alkioita joukkoon laitetaan.
 */
public class OwnSet<T> extends OwnAbstractSet<T> {
    private final Map<T, Object> map = new OwnMap<>();
    
    /**
     * Palauttaa joukon koon eli yksilöllisten alkioiden määrän.
     * @return Joukon koko.
     */
    @Override
    public int size() {
        return map.size();
    }

    /**
     * Tarkistaa onko joukko tyhjä.
     * @return Palauttaa true jos joukko on tyhjä.
     */
    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Tarkistaa kuuluuko annettu alkio joukkoon.
     * @param o Alkio, jonka jäsenyys tarkistetaan.
     * @return true, jos alkio kuuluu joukkoon.
     */
    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    /**
     * Palauttaa iteraattorin joukon alkioille.
     * Kaikki iteraattorin valinnaiset toiminnot, kuten remove() ja set() on tuettuja.
     * @return Iteraattori
     */
    @Override
    public Iterator<T> iterator() {
        return map.keySet().iterator();
    }

    /**
     * Lisää uuden alkion joukkoon.
     * @param e Lisättävä alkio
     * @return Palauttaa true, jos joukkoa muutettiin, eli jos sama alkio ei entuudestaan ollut joukossa.
     */
    @Override
    public boolean add(T e) {
        if (map.containsKey(e)) {
            return false;
        } else {
            map.put(e, null);
            return true;
        }
    }

    /**
     * Poistaa annetun alkion joukosta.
     * 
     * @param o Poistettava alkio.
     * @return Palauttaa true, jos alkio oli joukossa, ja joukkoa muutettiin.
     */
    @Override
    public boolean remove(Object o) {
        if (map.containsKey(o)) {
            map.remove(o);
            return true;
        }
        
        return false;
    }

    /**
     * Tyhjentää joukon.
     */
    @Override
    public void clear() {
        map.clear();
    }
}
