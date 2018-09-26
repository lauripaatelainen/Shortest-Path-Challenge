package com.edii.spc.datastructures;

import java.util.Objects;

/**
 * Geneerinen tietorakenne, joka sisältää kaksi objektia: pari. 
 * 
 * @param <T> Objektien tietotyyppi.
 */
public class Pair<T> {
    /**
     * Ensimmäinen kahdesta objektista. 
     */
    private final T first;
    /**
     * Toinen kahdesta objektista.
     */
    private final T second;
    
    /**
     * Luokan ainoa konstruktori, joka saa paremetreikseen kaksi oliota, jotka muodostavat parin. 
     * 
     * @param first Ensimmäinen objekti
     * @param second Toinen objekcti
     */
    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }
    
    /**
     * Hae parin ensimmäinen objekti
     * 
     * @return Palauttaa ensimmäisen kahdesta objektista. 
     */
    public T getFirst() {
        return first;
    }
    
    /**
     * Hae parin toinen objekti
     * 
     * @return Palauttaa toisen kahdesta objektista. 
     */
    public T getSecond() {
        return second;
    }
    
    /**
     * Palauttaa parin objektit käänteisessä järjestyksessä. 
     * 
     * @return Uusi pari, jossa first ja second objektit on käännetty toisin päin. 
     */
    public Pair<T> inverse() {
        return new Pair<>(this.getSecond(), this.getFirst());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        
        return this.hashCode() == o.hashCode();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.first);
        hash = 53 * hash + Objects.hashCode(this.second);
        return hash;
    }
}
