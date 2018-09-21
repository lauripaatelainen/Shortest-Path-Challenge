package com.edii.spc.game;

import java.util.Objects;

/**
 * Solmu, jolla voi olla kaaret vasemmalle, oikealle, ylös ja alas. 
 * Solmulla on lisäksi x- ja y-koordinaatit, joita käytetään solmun yksilöimiseen. 
 */
public class GameFieldNode {
    /**
     * Solmun x-koordinaatti pelikentällä.
     */
    private final int x;
    /**
     * Solmun y-koordinaatti pelikentällä. 
     */
    private final int y;
    
    /**
     * Solmun vasen kaari, tai null jos solmu sijaitsee pelikentän vasemmassa reunassa. 
     */
    private GameFieldEdge leftEdge = null;
    
    /**
     * Solmun oikea kaari, tai null jos solmu sijaitsee pelikentän oikeassa reunassa.
     */
    private GameFieldEdge rightEdge = null;
    
    /**
     * Solmun yläkaari, tai null jos solmu sijaitsee pelikentän yläreunassa. 
     */
    private GameFieldEdge upEdge = null;
    
    /**
     * Solmun alakaari, tai null jos solmu sijaitsee pelikentän alareunassa. 
     */
    private GameFieldEdge downEdge = null;
    
    /**
     * Konstruktori, joka luo solmun annetuilla koordinaateilla.
     * Solmun kaaret tulee asettaa erikseen sille tarkoitetuilla settereillä. 
     * 
     * @param x
     * @param y 
     */
    public GameFieldNode(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return Palauttaa vasemman kaaren.
     */
    public GameFieldEdge getLeftEdge() {
        return leftEdge;
    }

    /**
     * Asettaa solmulle uuden vasemman kaaren.
     * 
     * @param leftEdge Uusi vasen kaari.
     */
    public void setLeftEdge(GameFieldEdge leftEdge) {
        this.leftEdge = leftEdge;
    }

    /**
     * @return Palauttaa oikean kaaren.
     */
    public GameFieldEdge getRightEdge() {
        return rightEdge;
    }

    /**
     * Asettaa solmulle uuden oikean kaaren.
     * 
     * @param rightEdge Uusi oikea kaari.
     */
    public void setRightEdge(GameFieldEdge rightEdge) {
        this.rightEdge = rightEdge;
    }

    /**
     * @return Palauttaa solmun yläkaaren.
     */
    public GameFieldEdge getUpEdge() {
        return upEdge;
    }

    /**
     * Asettaa solmulle uuden yläkaaren.
     * 
     * @param upEdge Uusi yläkaari.
     */
    public void setUpEdge(GameFieldEdge upEdge) {
        this.upEdge = upEdge;
    }

    /**
     * @return Palauttaa solmun alakaaren.
     */
    public GameFieldEdge getDownEdge() {
        return downEdge;
    }

    /**
     * * Asettaa solmulle uuden alakaaren.
     * 
     * @param downEdge Uusi alakaari.
     */
    public void setDownEdge(GameFieldEdge downEdge) {
        this.downEdge = downEdge;
    }
    
    /**
     * @return Palauttaa solmun x-koordinaatin.
     */
    public int getX() {
        return x;
    }
    
    /**
     * @return Palauttaa solmun y-koordinaatin.
     */
    public int getY() {
        return y;
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
        hash = 23 * hash + this.x;
        hash = 23 * hash + this.y;
/*        hash = 23 * hash + Objects.hashCode(this.leftEdge);
        hash = 23 * hash + Objects.hashCode(this.rightEdge);
        hash = 23 * hash + Objects.hashCode(this.upEdge);
        hash = 23 * hash + Objects.hashCode(this.downEdge);*/
        return hash;
    }
}
