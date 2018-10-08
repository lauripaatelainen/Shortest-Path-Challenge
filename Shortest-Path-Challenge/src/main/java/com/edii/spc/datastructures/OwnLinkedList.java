package com.edii.spc.datastructures;

import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * Linkitetyn listan oma toteutus. 
 * Käyttää linkitetyn listan elementeille sisäistä luokkaa LinkedListNode. 
 * Perii luokan OwnAbstractList, jossa on toteutettu jotain yleiskäyttöisiä List-rajapinnan metodeja. 
 * Toteuttaa kaikki java.util.List-rajapinnan määrittelemät toiminnot. 
 * null-arvot ei ole sallittuja. 
 */
public class OwnLinkedList<E> extends OwnAbstractList<E> {
    private static class LinkedListNode<E> {
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
        return listIterator();
    }
    
    /**
     * Lisää annetun alkion listaan.
     * @param item Lisättävä alkio
     * @return Palauttaa aina true
     */
    @Override
    public boolean add(E item) {
        if (this.first == null) {
            LinkedListNode<E> node = new LinkedListNode<>(item);
            this.first = node;
            this.first.setNext(node);
            this.first.setPrev(node);
        } else {
            LinkedListNode<E> node = new LinkedListNode<>(item);
            node.setPrev(this.first.getPrev());
            node.setNext(this.first);
            this.first.getPrev().setNext(node);
            this.first.setPrev(node);
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
        if (i < 0 || i > size()) {
            throw new IndexOutOfBoundsException();
        } else if (isEmpty() || i == size()) {
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
        
        if (next == first) {
            first = node;
        }
        
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
        if (size() == 1) {
            clear();
            return node.getItem();
        } else {
            if (node == first) {
                first = first.getNext();
            }
            
            node.getPrev().setNext(node.getNext());
            node.getNext().setPrev(node.getPrev());
            itemsCount--;
            return node.getItem();
        }
    }

    @Override
    public ListIterator<E> listIterator(int i) {
        return new LinkedListListIterator(i);   
    }
    
    private class LinkedListListIterator implements ListIterator<E> {
        private OwnLinkedList.LinkedListNode<E> next = first;
        private LinkedListNode<E> last;
        private int nextIndex;
        
        public LinkedListListIterator(int i) {
            if (i == size()) {
                next = null;
            } else {
                next = getNode(i);
            }
            this.nextIndex = i;
        }

        @Override
        public E next() {
            last = next;
            nextIndex++;
            
            E val = next.getItem();
            next =  next.getNext();
            if (next == first) {
                next = null;
            }
            
            return val;
        }

        @Override
        public boolean hasPrevious() {
            return next != first;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public E previous() {
            if (next == null) {
                next = first;
            }
            next = next.getPrev();
            last = next;
            nextIndex--;
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
            OwnLinkedList.this.remove(last);
            last = null;
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
