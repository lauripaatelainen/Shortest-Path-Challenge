package com.edii.spc.game;

import com.edii.spc.datastructures.OwnList;
import com.edii.spc.datastructures.OwnSet;
import java.util.List;
import java.util.Set;

/**
 * Järjestetty jono kaaria, jotka muodostavat yhtenäisen polun kahden solmun välille. 
 */
public class GameFieldPath {
    private final GameFieldNode startNode;
    private List<GameFieldEdge> edges = new OwnList<>();
    
    /**
     * Luo tyhjän polun.
     * 
     * @param start Polun alkusolmu.
     */
    public GameFieldPath(GameFieldNode start) {
        this.startNode = start;
    }
    
    /**
     * Hae solmu, josta polku alkaa.
     * 
     * @return Palauttaa polun alkusolmun.
     */
    public GameFieldNode getStartNode() {
        return this.startNode;
    }
    
    /**
     * Hae solmu, johon polku päättyy.
     * 
     * @return Palauttaa polun loppusolmun.
     */
    public GameFieldNode getEndNode() {
        if (getLength() == 0) {
            return getStartNode();
        }
        return edges.get(edges.size() - 1).getNodes().getSecond();
    }
    
    /**
     * Lisää kaaren polkuun.
     * 
     * @throws IllegalArgumentException Heittää IllegalArgumentExceptionin jos uuden kaaren alkusolmu ei ole polun viimeinen solmu. 
     * @param edge Lisättävä kaari.
     */
    public void addEdge(GameFieldEdge edge) {
        if (edge == null || !edge.getNodes().getFirst().equals(getEndNode())) {
            throw new IllegalArgumentException();
        }
        
        edges.add(edge);
    }
    
    /**
     * Poista polun viimeinen kaari.
     */
    public void removeLastEdge() {
        edges.remove(edges.size() - 1);
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
     * Laske polun kokonaispaino.
     * 
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
     * Tekee polusta käänteiseen suuntaan kulkevan polun.
     * 
     * @return Palauttaa polun käänteisessä suunnassa.
     */
    public GameFieldPath reverse() {
        GameFieldPath path = new GameFieldPath(getEndNode());
        for (int i = edges.size() - 1; i >= 0; i--) {
            path.addEdge(edges.get(i).inverse());
        }
        return path;
    }
    
    /**
     * Hae polun pituus. 
     * 
     * @return Palauttaa polun pituuden reunojen määränä
     */
    public int getLength() {
        return edges.size();
    }
    
    /**
     * Hakee kaikki polun solmut joukkona. 
     * 
     * Tietorakenne ei palaudu missään ennalta määritellyssä järjestyksessä. Rakenteen voi käydä läpi iteraattorilla tai foreach-silmukalla.
     * 
     * @return Palauttaa kaikki polun solmut joukkona
     */
    public Set<GameFieldNode> getNodes() {
        Set<GameFieldNode> set = new OwnSet<>();
        for (GameFieldEdge edge : edges) {
            set.add(edge.getNodes().getFirst());
        }
        set.add(getEndNode());
        return set;
    }
}
