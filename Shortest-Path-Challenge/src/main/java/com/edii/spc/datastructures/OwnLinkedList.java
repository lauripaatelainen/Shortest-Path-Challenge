package com.edii.spc.datastructures;

import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * Linkitetyn listan oma toteutus. 
 * Käyttää linkitetyn listan elementeille sisäistä luokkaa LinkedListNode. 
 * Perii luokan OwnAbstractList, jossa on toteutettu jotain yleiskäyttöisiä List-rajapinnan metodeja. 
 * Toteuttaa suurimman osan java.util.List-rajapinnan määrittelemistä toiminnoista. 
 * 
 * @see OwnAbstractList
 */
public class OwnLinkedList<E> extends OwnAbstractList<E> {
    private static class LinkedListNode<E> {
        private LinkedListNode<E> prev;
        private LinkedListNode<E> next;
        private E item;
        
        /**
         * Luo linkitetyn listan solmun annetulla alkiolla.
         * Solmun linkit edelliseen ja seuraavaan solmuun ovat alkuun null-arvoisia.
         * @param item Elementti joka tässä solmussa sijaitsee.
         */
        public LinkedListNode(E item) {
            this.item = item;
        }

        /**
         * Palauttaa edellisen solmun.
         * @return Edellinen solmu.
         */
        public LinkedListNode<E> getPrev() {
            return prev;
        }

        /**
         * Asettaa tästä solmusta linkin edelliseen solmun.
         * @param prev Edellinen solmu.
         */
        public void setPrev(LinkedListNode<E> prev) {
            this.prev = prev;
        }

        /**
         * Palauttaa seuraavan solmum.
         * @return Seuraava solmu.
         */
        public LinkedListNode<E> getNext() {
            return next;
        }

        /**
         * Asettaa tästä solmusta linkin seuraavaan solmuun.
         * @param next Seuraava solmu.
         */
        public void setNext(LinkedListNode<E> next) {
            this.next = next;
        }

        /**
         * Palauttaa tässä solmussa sijaitsevan alkion.
         * @return Alkio
         */
        public E getItem() {
            return item;
        }

        /**
         * Asettaa tähän solmuun uuden alkion.
         * @param item Uusi alkio.
         */
        public void setItem(E item) {
            this.item = item;
        }
    }
    
    /**
     * Osoitin linkitetyn listan ensimmäiseen solmuun.
     */
    private LinkedListNode<E> first = null;
    
    /**
     * Ylläpidetään linkitetyn listan kokoa muuttujassa, jotta size() operaatio
     * saadaan O(1) aikavaativuuteen. 
     */
    private int itemsCount = 0;
    
    /**
     * Luokan sisäinen metodi, joka palauttaa annetussa indeksissä olevan solmun.
     * Listaa käydään läpi alusta siihen indeksiin missä solmu on, eli aikavaativuus O(n)
     * Metodi palauttaa null liian pienille tai suurille indekseille, joten kutsujan
     * on tarkistettava joko indeksi etukäteen tai getNoden paluuarvo. 
     * 
     * @param i Listan indeksi
     * @return Kyseisessä indeksissä oleva solmu, tai null jos i &lt; 0 || i &gt;= size() 
     */
    private LinkedListNode<E> getNode(int i) {
        if (i < 0 || i >= size()) {
            return null;
        }
        
        LinkedListNode<E> node = this.first;
        for (int j = 0; j < i; j++) {
            node = node.getNext();
        }
        return node;
    }
    
    /**
     * Palauttaa linkitetyn listan koon. 
     * Listan kokoa ylläpidetään erillisessä muuttujassa, eli koko listaa ei tarvitse
     * käydä läpi. Aikavaativuus O(1)
     * @return Listan koko
     */
    @Override
    public int size() {
        return itemsCount;
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
     * @throws IndexOutOfBoundsException Jos i &lt; 0 || i &gt;= size()
     */
    @Override
    public boolean addAll(int i, Collection<? extends E> clctn) {
        if (i < 0 || i > size()) {
            throw new IndexOutOfBoundsException();
        } else if (clctn.isEmpty()) {
            return false;
        }
        
        LinkedListNode<E> next = getNode(i);
        for (E e : clctn) {
            add(next, e);
        }
        
        return true;
    }

    /**
     * Tyhjentää listan.
     */
    @Override
    public void clear() {
        this.first = null;
        this.itemsCount = 0;
    }

    /**
     * Palauttaa annetussa indeksissä sijaitsevan alkion.
     * 
     * @param i Indeksi
     * @return Alkio
     * @throws IndexOutOfBoundsException Jos i &lt; 0 || i &gt;= size()
     */
    @Override
    public E get(int i) {
        if (i < 0 || i >= size()) {
            throw new IndexOutOfBoundsException();
        }
        
        return getNode(i).getItem();
    }

    /**
     * Asettaa uuden alkion annettuun indeksiin.
     * @param i Indeksi
     * @param e Uusi alkio
     * @return Palauttaa samassa indeksissä olleen vanhan alkion.
     * @throws IndexOutOfBoundsException Jos i &lt; 0 || i &gt;= size()
     */
    @Override
    public E set(int i, E e) {
        if (i < 0 || i >= size()) {
            throw new IndexOutOfBoundsException();
        }
        
        LinkedListNode<E> node = getNode(i);
        E prev = node.getItem();
        node.setItem(e);
        return prev;
    }

    /**
     * Lisää uuden alkion annettuun listan indeksiin. 
     * Indeksistä eteenpäin olevia alkioita siirretään yksi eteenpäin. 
     * Oikea node pitää etsiä käymällä listaa läpi alusta indeksiin asti, 
     * aikavaativuus O(n)
     * 
     * @param i Indeksi, mihin alkio lisätään.
     * @param e Lisättävä alkio
     * @throws IndexOutOfBoundsException Jos i &lt; 0 || i &gt;= size()
     */
    @Override
    public void add(int i, E e) {
        if (i < 0 || i > size()) {
            throw new IndexOutOfBoundsException();
        } else {
            add(getNode(i), e);
        }
    }
    
    /**
     * Lisää annetun solmun kohdalle uuden alkion.
     * Parametrina annetusta solmusta tulee uuden solmun seuraava solmu, ja
     * vastaavasti kaikki siitä eteenpäin olevat solmut siirtyy yhden solmun eteenpäin.
     * 
     * @param next Seuraava solmu
     * @param e Uusi alkio
     * @return Palauttaa juuri luodun solmun
     */
    private LinkedListNode add(LinkedListNode next, E e) {
        if (next == null) {
            add(e);
            return first.getPrev();
        }
        
        LinkedListNode<E> node = new LinkedListNode<>(e);
        next.getPrev().setNext(node);
        node.setPrev(next.getPrev());
        next.setPrev(node);
        node.setNext(next);
        
        if (next == first) {
            first = node;
        }
        
        itemsCount++;
        return node;
    }

    /**
     * Poista annetussa indeksissä oleva alkio.
     * Poistettava alkio pitää etsiä käymällä listaa läpi ensimmäisestä solmusta alkaen,
     * joten aikavaativuus on O(n)
     * 
     * @param i Poistettavan alkion indeksi.
     * @return Poistettu alkio.
     * @throws IndexOutOfBoundsException Jos i &lt; 0 || i &gt;= size()
     */
    @Override
    public E remove(int i) {
        if (i < 0 || i >= size()) {
            throw new ArrayIndexOutOfBoundsException("");
        } 
        
        LinkedListNode<E> node = getNode(i);
        return remove(node);
    }
    
    /**
     * Poista annettu solmu listasta.
     * Kaikki tarvittavat linkit löytyy annetusta solmusta, eli 
     * aikavaativuus on O(1)
     * 
     * @param node Solmu, joka poistetaan.
     * @return Alkio, joka poistetussa solmussa sijaitsi.
     */
    private E remove(LinkedListNode<E> node) {
        node.getPrev().setNext(node.getNext());
        node.getNext().setPrev(node.getPrev());
        itemsCount--;

        if (node == first) {
            if (itemsCount == 0) {
                first = null;
            } else {
                first = first.getNext();
            }
        }

        return node.getItem();
    }

    /**
     * Palauttaa listan läpikäyntiä varten ListIterator-olion, alkaen annetusta indeksistä.
     * Kaikki iteraattorin valinnaiset metodit, kuten set() ja remove() ovat tuettuja.
     * @param i Indeksi, johon iteraattori osoittaa
     * @return Iteraattori
     */
    @Override
    public ListIterator<E> listIterator(int i) {
        return new LinkedListListIterator(i);   
    }
    
    /**
     * Sisäinen luokka, joka toteuttaa ListIterator-rajapinnan. 
     */
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
            next = next.getNext();
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
