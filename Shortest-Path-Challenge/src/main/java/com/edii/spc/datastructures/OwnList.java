package com.edii.spc.datastructures;

import java.util.ArrayList;
import java.util.List;

/**
 * Javan ArrayList-tietorakennetta vastaava oma tietorakenne. 
 * 
 * Toistaiseksi extendaa suoraan ArrayListin ja omat metoditoteutukset tehdään myöhemmin. 
 */
public class OwnList<T> extends ArrayList<T> implements List<T> {
    public OwnList() {
        super();
    }
}
