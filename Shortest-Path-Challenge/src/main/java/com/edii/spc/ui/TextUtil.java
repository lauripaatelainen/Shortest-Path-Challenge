package com.edii.spc.ui;

import com.edii.spc.game.GameField;
import com.edii.spc.game.GameFieldEdge;
import com.edii.spc.game.GameFieldPath;

/**
 * Tekstikäyttöliittymän apufunktioita sisältävä luokka. 
 */
public class TextUtil {
    /**
     * Täyttää annetun merkkijonon täytemerkillä annetun pituiseksi. 
     * Täytemerkkiä lisätään merkkijonon oikealle puolelle. Jos merkkijono on
     * jo entuudestaan yhtä pitkä tai lyhyempi kuin size, palautetaan merkkijono
     * sellaisenaan.
     * 
     * Esim.
     * rightPad("str", 5, 'x') = "strxx"
     * rightPad("str", 8, '-') = "str-----"
     * rightPad("str", 3, '+') = "str"
     * rightPad("str", 0, 'z') = "str"
     * rightPad("str", -100, '.') = "str"
     * 
     * @param string
     * @param size
     * @param padChar
     * @return 
     */
    public static String rightPad(String string, int size, char padChar) {
        while (string.length() < size) {
            string += padChar;
        }
        return string;
    }
    
    
    /**
     * Tulostaa annetun pelikentän tekstimuotoisena. 
     * 
     * @param game 
     */
    public static void printGameField(GameField gameField) {
        int size = gameField.getSize();
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                for (int j = 0; j < size; j++) {
                    System.out.printf(rightPad("|", 6, ' '));
                }
                System.out.println("");
                
                for (int j = 0; j < size; j++) {
                    int upWeight = gameField.getNode(j, i).getUpEdge().getWeight();
                    System.out.printf(rightPad(Integer.toString(upWeight), 6, ' '));
                }
                System.out.println("");
                
                for (int j = 0; j < size; j++) {
                    System.out.printf(rightPad("|", 6, ' '));
                }
                System.out.println("");
            }
            
            for (int j = 0; j < size; j++) {
                if (j != 0) {
                    int leftWeight = gameField.getNode(j, i).getLeftEdge().getWeight();
                    System.out.printf(rightPad(Integer.toString(leftWeight), 3, '-'));
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
    
    public static void printPath(GameFieldPath path) {
        for (GameFieldEdge edge : path.getEdges()) {
            System.out.printf("(%d, %d) -[%d]-> ", edge.getNodes().getFirst().getX(), edge.getNodes().getFirst().getY(), edge.getWeight());
        }
        
        System.out.printf("(%d, %d)\n", path.getEndNode().getX(), path.getEndNode().getY(), path.getWeight());
    }
}