package com.edii.spc.datastructures;

import java.util.Map;
import java.util.Objects;

/**
 * Sisäinen luokka avain-arvo parille. Toteuttaa javan yleisen rajapinnan
 * Map.Entry
 *
 * @param <K> Avaimen tyyppi
 * @param <V> Arvon tyyppi
 */
public class OwnMapItem<K, V> implements Map.Entry<K, V> {
    private final K key;
    private V value;

    /**
     * Luo avain-arvo parin annetulla avaimella ja arvolla. Luokan ainoa konstruktori. 
     * @param key Avain
     * @param value Arvo
     */
    public OwnMapItem(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Palauttaa avain-arvo -parin avaimen.
     *
     * @return Avain
     */
    @Override
    public K getKey() {
        return key;
    }

    /**
     * Palauttaa avain-arvo -parin arvon.
     *
     * @return Arvo
     */
    @Override
    public V getValue() {
        return value;
    }

    /**
     * Asettaa avain-arvo -pariin uuden arvon.
     *
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
        return (this.getKey() == null ? 0 : this.getKey().hashCode()) ^ (this.getValue() == null ? 0 : this.getValue().hashCode());
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
        final OwnMapItem<?, ?> other = (OwnMapItem<?, ?>) obj;
        if (!Objects.equals(this.key, other.key)) {
            return false;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }
}
