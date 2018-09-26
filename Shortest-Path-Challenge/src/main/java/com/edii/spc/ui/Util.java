package com.edii.spc.ui;

/**
 * Tekstikäyttöliittymän apufunktioita sisältävä luokka. 
 */
public class Util {
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
     * @param string Merkkijono, jota täytetään
     * @param size Kuinka pitkäksi merkkijono täytetään
     * @param padChar Täytemerkki
     * @return Palauttaa alkuperäisen merkkijonon täytemerkillä täytettynä tarvittavan pitkäksi.
     */
    public static String rightPad(String string, int size, char padChar) {
        while (string.length() < size) {
            string += padChar;
        }
        return string;
    }
}
