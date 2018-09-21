package com.edii.spc.game;

/**
 * Game-luokan olio edustaa yhtä peliä. Pelin sisälle luodaan pelikenttä, joka pelaajan tulee ratkaista. 
 */
public class Game {
    /**
     * Pelikenttä, jota tässä pelissä pelataan.
     */
    private final GameField gameField;
    
    /**
     * Konstruktori, joka luo paramterina annetun kokonaisluvun kokoisen satunnaisilla painoilla täytetyn pelikentän (size*size). 
     * 
     * @param size Pelikentän koko.
     */
    public Game(int size) {
        this.gameField = GameField.generateRandomField(size);
    }
    
    /**
     * Hakee pelikentän koon, jota tässä pelissä pelataan. 
     * 
     * @return Palauttaa pelikentän koon. 
     */
    public int getSize() {
        return gameField.getSize();
    }
    
    /**
     * Hakee pelikentän, jota tässä pelissä pelataan. 
     * 
     * @return Palauttaa pelikentän GameField-objektina. 
     */
    public GameField getGameField() {
        return gameField;
    }
}
