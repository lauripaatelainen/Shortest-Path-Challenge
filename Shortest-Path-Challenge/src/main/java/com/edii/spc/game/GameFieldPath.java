package com.edii.spc.game;

import com.edii.spc.datastructures.OwnArrayList;
import java.util.List;

/**
 * Järjestetty jono kaaria, jotka muodostavat yhtenäisen polun kahden solmun välille. 
 */
public class GameFieldPath {
    List<GameFieldEdge> edges = new OwnArrayList<>();
    
    /**
     * Luo polun yhdellä kaarella. 
     * 
     * @param edge Kaari josta polku alkaa.
     */
    public GameFieldPath(GameFieldEdge edge) {
        edges.add(edge);
    }
    
    /**
     * @return Palauttaa polun alkusolmun.
     */
    public GameFieldNode getStartNode() {
        return edges.get(0).getNodes().getFirst();
    }
    
    /**
     * @return Palauttaa polun loppusolmun.
     */
    public GameFieldNode getEndNode() {
        return edges.get(edges.size() - 1).getNodes().getSecond();
    }
    
    /**
     * Lisää kaaren polkuun.
     * @param edge Lisättävä kaari.
     */
    public void addEdge(GameFieldEdge edge) {
        edges.add(edge);
    }
    
    /**
     * @return Palauttaa polun kokonaispainon, eli kaarien yhteenlasketun painon.
     */
    public int getWeight() {
        int out = 0;
        for (GameFieldEdge edge : edges) {
            out += edge.getWeight();
        }
        return out;
    }
}
