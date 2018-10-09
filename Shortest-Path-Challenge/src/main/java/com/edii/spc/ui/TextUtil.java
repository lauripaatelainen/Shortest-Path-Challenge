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
     * Täytemerkkiä lisätään merkkijonon oikealle puolelle. Jos merkkijono on jo
     * entuudestaan yhtä pitkä tai lyhyempi kuin size, palautetaan merkkijono
     * sellaisenaan.
     *
     * Esim. rightPad("str", 5, 'x') = "strxx" rightPad("str", 8, '-') =
     * "str-----" rightPad("str", 3, '+') = "str" rightPad("str", 0, 'z') =
     * "str" rightPad("str", -100, '.') = "str"
     *
     * @param string Merkkijono, jota täytetään.
     * @param size Pituus, jonka pituinen tuloksen pitää vähintään olla.
     * @param padChar Täytemerkki.
     * @return Palauttaa täytetyn merkkijonon.
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
     * @param gameField Pelikenttä, joka tulostetaan konsoliin.
     */
    public static void printGameField(GameField gameField) {
        int size = gameField.getSize();
        int padCount = 6;
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                printGameFieldIntermediateRow(gameField, padCount);
                printGameFieldVerticalRow(gameField, i, padCount);
                printGameFieldIntermediateRow(gameField, padCount);
            }

            printGameFieldHorizontalRow(gameField, i, padCount);
        }
        System.out.println("");
    }

    private static void printGameFieldHorizontalRow(GameField gameField, int row, int padCount) {
        for (int i = 0; i < gameField.getSize(); i++) {
            if (i != 0) {
                int leftWeight = gameField.getNode(i, row).getLeftEdge().getWeight();
                System.out.printf(rightPad(Integer.toString(leftWeight), 3, '-'));
            }

            if (i != gameField.getSize() - 1) {
                System.out.printf("*--");
            } else {
                System.out.printf("*");
            }
        }
        System.out.println("");
    }

    private static void printGameFieldVerticalRow(GameField gameField, int row, int padCount) {
        for (int i = 0; i < gameField.getSize(); i++) {
            int upWeight = gameField.getNode(i, row).getUpEdge().getWeight();
            System.out.printf(rightPad(Integer.toString(upWeight), padCount, ' '));
        }
        System.out.println("");
    }

    private static void printGameFieldIntermediateRow(GameField gameField, int padCount) {
        for (int j = 0; j < gameField.getSize(); j++) {
            System.out.printf(rightPad("|", padCount, ' '));
        }
        System.out.println("");
    }

    /**
     * Tulostaa annetun polun konsoliin.
     *
     * @param path Polku, joka tulostetaan.
     */
    public static void printPath(GameFieldPath path) {
        for (GameFieldEdge edge : path.getEdges()) {
            System.out.printf("(%d, %d) -[%d]-> ", edge.getNodes().getFirst().getX(), edge.getNodes().getFirst().getY(), edge.getWeight());
        }

        System.out.printf("(%d, %d)\n", path.getEndNode().getX(), path.getEndNode().getY(), path.getWeight());
    }
}
