package com.edii.spc.datastructures;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author edii
 */
public class OwnLinkedList<E> extends OwnAbstractList<E> {
    public static class LinkedListNode<E> {
        private LinkedListNode<E> prev;
        private LinkedListNode<E> next;
        private E item;
        
        public LinkedListNode(E item) {
            this.item = item;
        }

        /**
         * @return the prev
         */
        public LinkedListNode<E> getPrev() {
            return prev;
        }

        /**
         * @param prev the prev to set
         */
        public void setPrev(LinkedListNode<E> prev) {
            this.prev = prev;
        }

        /**
         * @return the next
         */
        public LinkedListNode<E> getNext() {
            return next;
        }

        /**
         * @param next the next to set
         */
        public void setNext(LinkedListNode<E> next) {
            this.next = next;
        }

        /**
         * @return the item
         */
        public E getItem() {
            return item;
        }

        /**
         * @param item the item to set
         */
        public void setItem(E item) {
            this.item = item;
        }
    }
    
    private LinkedListNode<E> first = null;
    private LinkedListNode<E> last = null;
    private int itemsCount = 0;
    
    private LinkedListNode<E> getNode(int i) {
        if (i < 0 || i >= size()) {
            throw new IndexOutOfBoundsException();
        }
        
        LinkedListNode<E> node = this.first;
        for (int j = 0; j < i; j++) {
            node = node.getNext();
        }
        return node;
    }
    
    @Override
    public int size() {
        return itemsCount;
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedListIterator();
    }
    
    /**
     * Lisää annetun alkion listaan.
     * @param item Lisättävä alkio
     * @return Palauttaa aina true
     */
    @Override
    public boolean add(E item) {
        if (this.last == null) {
            this.last = new LinkedListNode<>(item);
            this.first = this.last;
            this.first.setNext(last);
            this.first.setPrev(last);
        } else {
            LinkedListNode<E> node = new LinkedListNode<>(item);
            node.setPrev(this.last);
            node.setNext(this.first);
            this.last.setNext(node);
            this.first.setPrev(node);
            this.last = node;
        }
        itemsCount++;
        return true;
    }

    /**
     * Lisää kaikki annetun tietorakenteen alkiot annettuun listan indeksiin. 
     * 
     * @param i Indeksi, mihin alkiot lisätään.
     * @param clctn Tietorakenne, jonka alkiot lisätään.
     * @return Palauttaa aina true
     */
    @Override
    public boolean addAll(int i, Collection<? extends E> clctn) {
        LinkedListNode<E> next = getNode(i);
        LinkedListNode<E> prev = next.getPrev();
        for (E item : clctn) {
            LinkedListNode<E> node = new LinkedListNode<>(item);
            node.setPrev(prev);
            node.setNext(next);
            prev.setNext(node);
            next.setPrev(node);
            prev = node;
            itemsCount++;
        }
        return true;
    }

    @Override
    public void clear() {
        this.first = null;
        this.last = null;
        this.itemsCount = 0;
    }

    @Override
    public E get(int i) {
        return getNode(i).getItem();
    }

    @Override
    public E set(int i, E e) {
        LinkedListNode<E> node = getNode(i);
        E prev = node.getItem();
        node.setItem(e);
        return prev;
    }

    @Override
    public void add(int i, E e) {
        if (isEmpty()) {
            add(e);
        } else {
            add(getNode(i), e);
        }
    }
    
    private void add(LinkedListNode next, E e) {
        LinkedListNode<E> node = new LinkedListNode<>(e);
        next.getPrev().setNext(node);
        node.setPrev(next.getPrev());
        next.setPrev(node);
        node.setNext(next);
        itemsCount++;
    }

    @Override
    public E remove(int i) {
        if (isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("List is empty");
        } else if (size() == 1) {
            E item = this.first.getItem();
            clear();
            return item;
        } else {
            LinkedListNode<E> node = getNode(i);
            return remove(node);
        }
    }
    
    private E remove(LinkedListNode<E> node) {
        node.getPrev().setNext(node.getNext());
        node.getNext().setPrev(node.getPrev());
        itemsCount--;
        return node.getItem();
    }

    @Override
    public ListIterator<E> listIterator(int i) {
        return new LinkedListListIterator(i);   
    }

    private class LinkedListIterator implements Iterator<E> {
        protected OwnLinkedList.LinkedListNode<E> next = first;

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public E next() {
            E val = next.getItem();
            next =  next.getNext();
            if (next == first) {
                next = null;
            }
            return val;
        }
    }
    
    private class LinkedListListIterator extends LinkedListIterator implements ListIterator<E> {
        private LinkedListNode<E> last;
        private int nextIndex;
        
        public LinkedListListIterator(int i) {
            next = getNode(i);
            this.nextIndex = i;
        }

        @Override
        public E next() {
            last = next;
            return super.next();
        }

        @Override
        public boolean hasPrevious() {
            return next != first;
        }

        @Override
        public E previous() {
            next = next.getPrev();
            last = next;
            return next.getItem();
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        @Override
        public void remove() {
            next = last.getNext();
            OwnLinkedList.this.remove(last);
        }

        @Override
        public void set(E e) {
            last.setItem(e);
        }

        @Override
        public void add(E e) {
            OwnLinkedList.this.add(last, e);
        }
    }
}
