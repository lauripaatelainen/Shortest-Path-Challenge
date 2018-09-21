package com.edii.spc.datastructures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Javan ArrayList-tietorakennetta vastaava oma tietorakenne. 
 * 
 * Toistaiseksi extendaa suoraan ArrayListin ja omat metoditoteutukset tehdään myöhemmin. 
 */
public class OwnArrayList<T> extends ArrayList<T> implements List<T> {
    public OwnArrayList() {
        super();
    }
}
