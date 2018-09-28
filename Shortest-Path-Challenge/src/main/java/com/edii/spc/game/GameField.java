package com.edii.spc.game;

import com.edii.spc.datastructures.OwnSet;
import com.edii.spc.datastructures.Pair;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * GameField-objekti sisältää yhteen peliin liittyvän pelikentän.
 * 
 * Pelikenttä sisältää size*size määrän solmuja neliön muotoisessa ruudukossa ja
 * solmuja yhdistää pysty- ja vaakasuunnissa kulkevat kaaret. 
 */
public class GameField {
    private static final int MAX_VALUE = 10;
    private GameFieldNode[][] nodes;
    
    /**
     * Konstruktori, joka luo parametrina annetun kokoisen pelikentän. Myös 
     * solmut ja niitä yhdistävät kaaret luodaan automaattisesti. 
     * 
     * Kaikkien kaarien paino on aluksi 0.
     * 
     * @param size Pelikentän leveys/korkeus
     */
    public GameField(int size) {
        if (size <= 1) {
            throw new IllegalArgumentException();
        }
        nodes = new GameFieldNode[size][size];
        
        /* Konstruktorissa luodaan nodet valmiiksi painoilla 0 */
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                nodes[i][j] = new GameFieldNode(j, i);
                
                /* Jos ei ylin rivi, lisätään kaari ylös päin */
                if (i != 0) {
                    Pair<GameFieldNode> pair = new Pair<>(nodes[i][j], nodes[i - 1][j]);
                    GameFieldEdge edge = new GameFieldEdge(pair, 0);
                    nodes[i][j].setUpEdge(edge);
                    nodes[i - 1][j].setDownEdge(edge.inverse());
                }
                
                /* Jos ei vasemmanpuoleisin rivi, lisätään kaari vasemmalle */
                if (j != 0) {
                    Pair<GameFieldNode> pair = new Pair<>(nodes[i][j], nodes[i][j - 1]);
                    GameFieldEdge edge = new GameFieldEdge(pair, 0);
                    nodes[i][j].setLeftEdge(edge);
                    nodes[i][j - 1].setRightEdge(edge.inverse());
                }
            }
        }
    }
    
    /**
     * Luo satunnaisilla painoarvoilla täytetty pelikenttä. 
     *
     * @param size Pelikentän koko
     * @return Palauttaa luodun pelikentän.
     */
    public static GameField generateRandomField(int size) {
        Random random = new Random();
        GameField gameField = new GameField(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != 0) {
                    int weight = random.nextInt(MAX_VALUE);
                    gameField.getNode(j, i).getUpEdge().setWeight(weight);
                    gameField.getNode(j, i).getUpEdge().getNodes().getSecond().getDownEdge().setWeight(weight);
                }
                if (j != 0) {
                    int weight = random.nextInt(MAX_VALUE);
                    gameField.getNode(j, i).getLeftEdge().setWeight(weight);
                    gameField.getNode(j, i).getLeftEdge().getNodes().getSecond().getRightEdge().setWeight(weight);
                }
            }
        }
        return gameField;
    }
    
    /**
     * Hakee kaikki pelikentän solmut sisältävän joukon.
     * 
     * Tietorakenne ei palaudu missään ennalta määritellyssä järjestyksessä. Rakenteen voi käydä läpi iteraattorilla tai foreach-silmukalla.
     * 
     * @return Palauttaa kaikki pelikentän solmut sisältävän joukon..
     */
    public Set<GameFieldNode> getNodes() {
        Set<GameFieldNode> out = new OwnSet<>();
        
        for (int y = 0; y < getSize(); y++) {
            for (int x = 0; x < getSize(); x++) {
                out.add(getNode(x, y));
            }
        }
        
        return out;
    }
    
    /**
     * Hakee annetuissa koordinaateissa sijaitsevan solmun. 
     * 
     * @param x X-koordinaatti
     * @param y Y-koordinaatti
     * @return Palauttaa kyseisissä koordinaateissa sijaitsevan solmun
     */
    public GameFieldNode getNode(int x, int y) {
        return nodes[y][x];
    }
    
    /**
     * Kysyy pelikentän kokoa. 
     * 
     * @return Palauttaa pelikentän koon. 
     */
    public int getSize() {
        return nodes.length;
    }
    
    /**
     * Hae pelikentän alkusolmu.
     * 
     * Lyhyt muoto kutsulle GameField.getNode(0, 0)
     * 
     * @return Palauttaa alkusolmun.
     */
    public GameFieldNode getStart() {
        return getNode(0, 0);
    }
    
    /**
     * Hae pelikentän maalisolmu.
     * 
     * Lyhyt muoto kutsulle GameField.getNode(size - 1, size - 1)
     * 
     * @return Palauttaa maalisolmun.
     */
    public GameFieldNode getFinish() {
        return getNode(getSize() - 1, getSize() - 1);
    }
}
