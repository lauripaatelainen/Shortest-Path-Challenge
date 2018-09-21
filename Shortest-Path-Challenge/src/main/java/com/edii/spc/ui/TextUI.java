package com.edii.spc.ui;

import com.edii.spc.game.Game;
import com.edii.spc.game.GameField;

/**
 * Väliaikainen tekstikäyttöliittymä, jossa ei ole juuri mitään toimintoja. 
 * Tullaan korvaamaan graafisella käyttöliittymällä projektin edetessä. 
 */
public class TextUI {
    /**
     * Tulostaa annetun pelikentän tekstimuotoisena. 
     * 
     * @param game 
     */
    private static void printGame(Game game) {
        GameField gameField = game.getGameField();
        int size = game.getSize();
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                for (int j = 0; j < size; j++) {
                    System.out.printf(Util.rightPad("|", 6, ' '));
                }
                System.out.println("");
                
                for (int j = 0; j < size; j++) {
                    int upWeight = gameField.getNode(j, i).getUpEdge().getWeight();
                    System.out.printf(Util.rightPad(Integer.toString(upWeight), 6, ' '));
                }
                System.out.println("");
                
                for (int j = 0; j < size; j++) {
                    System.out.printf(Util.rightPad("|", 6, ' '));
                }
                System.out.println("");
            }
            
            for (int j = 0; j < size; j++) {
                if (j != 0) {
                    int leftWeight = gameField.getNode(j, i).getLeftEdge().getWeight();
                    System.out.printf(Util.rightPad(Integer.toString(leftWeight), 3, '-'));
                }
                
                if (j != size - 1) {
                    System.out.printf("*--");
                } else {
                    System.out.printf("*");
                }
            }
            System.out.println("");
        }
        System.out.println("");
    }
    
    public static void main(String[] args) {
        
        for (int i = 1; i < 10; i++) {
            Game game = new Game(i);
            printGame(game);
        }
    }
}
