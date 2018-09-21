package com.edii.spc.game;

import com.edii.spc.datastructures.Pair;
import java.util.Random;

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
        if (size < 1) {
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
                /* Jos ei ylin rivi, arvotaan painoarvo ylöspäin menevälle kaarelle */
                if (i != 0) {
                    int weight = random.nextInt(MAX_VALUE);
                    gameField.getNode(j, i).getUpEdge().setWeight(weight);
                    gameField.getNode(j, i).getUpEdge().getNodes().getSecond().getDownEdge().setWeight(weight);
                }
                
                /* Jos ei vasemmanpuoleisin rivi, arvotaan painoarvo vasemmalle menevälle kaarelle */
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
}
