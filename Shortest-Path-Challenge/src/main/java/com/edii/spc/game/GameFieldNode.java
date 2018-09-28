package com.edii.spc.game;

import com.edii.spc.datastructures.OwnSet;
import java.util.Set;

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
     * @param x X-koordinaatti
     * @param y Y-koordinaatti
     */
    public GameFieldNode(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Hae solmusta vasemmalle lähtevä kaari.
     * 
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
     * Hae solmusta oikealle lähtevä kaari.
     * 
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
     * Hae solmusta ylös lähtevä kaari.
     * 
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
     * Hae solmusta alas lähtevä kaari.
     * 
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
     * Hae solmun x-koordinaatti.
     * 
     * @return Palauttaa solmun x-koordinaatin.
     */
    public int getX() {
        return x;
    }
    
    /**
     * Hae solmun y-koordinaatti.
     * 
     * @return Palauttaa solmun y-koordinaatin.
     */
    public int getY() {
        return y;
    }
    
    /**
     * Hakee kaikki solmusta lähetevät kaaret sisältävän joukon. 
     * 
     * Kaaret eivät ole missään tietyssä järjestyksessä ja ne voidaan käydä läpi esim. foreach-silmukalla.
     * 
     * @return Palauttaa kaikki solmusta lähtevät kaaret joukkona. 
     */
    public Set<GameFieldEdge> getEdges() {
        Set<GameFieldEdge> out = new OwnSet<>();
        if (getLeftEdge() != null) {
            out.add(getLeftEdge());
        }
        if (getUpEdge() != null) {
            out.add(getUpEdge());
        }
        if (getRightEdge() != null) {
            out.add(getRightEdge());
        }
        if (getDownEdge() != null) {
            out.add(getDownEdge());
        }
        return out;
    }
    
    /**
     * Hae kaari joka lähtee tästä solmusta annettuun solmuun. 
     * 
     * @param to Solmu johon lähtevä kaari haetaan.
     * @return Kaari joka lähtee tästä solmusta annettuun solmuun, tai null jos sellaista kaarta ei ole.
     */
    public GameFieldEdge getEdgeTo(GameFieldNode to) {
        for (GameFieldEdge edge : getEdges()) {
            if (edge.getNodes().getSecond().equals(to)) {
                return edge;
            }
        }
        return null;
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
