package com.edii.spc.game;

import com.edii.spc.datastructures.Pair;
import java.util.Objects;

/**
 * Kahta solmua yhdistävä kaari, jolla on paino. 
 */
public class GameFieldEdge {
    /**
     * Solmupari, joita kaari yhdistää.
     */
    private final Pair<GameFieldNode> nodes;
    /**
     * Kaaren paino. 
     */
    private int weight;
    
    /** Luokan ainoa konstruktori, jolla luodaan annetut parit yhdistävä kaari annetulla painolla. 
     * 
     * @param nodes Solmupari
     * @param weight Paino
     */
    public GameFieldEdge(Pair<GameFieldNode> nodes, int weight) {
        this.nodes = nodes;
        setWeight(weight);
    }
    
    /**
     * Hakee solmuparin, joita kaari yhdistää. 
     * 
     * @return Palauttaa solmuparin, joita kaari yhdistää.
     */
    public Pair<GameFieldNode> getNodes() {
        return this.nodes;
    }
    
    /**
     * Hakee kaaren painon.
     * 
     * @return Palauttaa kaaren painon.
     */
    public int getWeight() {
        return this.weight;
    }
    
    /**
     * Asettaa kaarelle uuden painon.
     * 
     * @param weight Kaaren uusi paino.
     */
    public final void setWeight(int weight) {
        if (weight < 0) {
            throw new IllegalArgumentException();
        }
        this.weight = weight;
    }
    
    /**
     * Palauttaa kaaren käänteiseen suuntaan.
     * 
     * @return Uusi kaari, jonka solmuparissa solmut ovat käänteisessä järjestyksessä.
     */
    public GameFieldEdge inverse() {
        return new GameFieldEdge(this.getNodes().inverse(), this.getWeight());
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
        final GameFieldEdge other = (GameFieldEdge) obj;
        if (this.weight != other.weight) {
            return false;
        }
        if (!Objects.equals(this.nodes, other.nodes)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.nodes);
        hash = 59 * hash + this.weight;
        return hash;
    }
}
