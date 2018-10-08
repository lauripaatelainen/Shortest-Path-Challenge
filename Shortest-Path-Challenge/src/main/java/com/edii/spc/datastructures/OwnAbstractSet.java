package com.edii.spc.datastructures;

import java.util.Set;

public abstract class OwnAbstractSet<E> extends OwnAbstractCollection<E> implements Set<E> {
    @Override
    public boolean equals(Object o) {
        return o == this || (o instanceof Set && ((Set) o).size() == size() && containsAll((Set) o));
    }
}
