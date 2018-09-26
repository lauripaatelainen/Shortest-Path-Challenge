package com.edii.spc.game;

import com.edii.spc.datastructures.OwnList;
import java.util.List;

/**
 * Järjestetty jono kaaria, jotka muodostavat yhtenäisen polun kahden solmun välille. 
 */
public class GameFieldPath {
    List<GameFieldEdge> edges = new OwnList<>();
    
    /**
     * Luo tyhjän polun
     */
    public GameFieldPath() {
        
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
     * 
     * Heittää IllegalArgumentExceptionin jos uuden kaaren alkusolmu ei ole polun viimeinen solmu. 
     * 
     * @param edge Lisättävä kaari.
     */
    public void addEdge(GameFieldEdge edge) {
        if (edges.size() > 0 && !edge.getNodes().getFirst().equals(getEndNode())) {
            throw new IllegalArgumentException();
        }
        
        edges.add(edge);
    }
    
    /**
     * Palauttaa kaaret, joista polku koostuu listana.
     * 
     * @return Lista, jossa polun kaaret ovat järjestyksessä. 
     */
    public List<GameFieldEdge> getEdges() {
        return edges;
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
    
    /**
     * @return Palauttaa polun käänteisessä suunnassa.
     */
    public GameFieldPath reverse() {
        GameFieldPath path = new GameFieldPath();
        for (int i = edges.size() - 1; i >= 0; i--) {
            path.addEdge(edges.get(i).inverse());
        }
        return path;
    }
}
