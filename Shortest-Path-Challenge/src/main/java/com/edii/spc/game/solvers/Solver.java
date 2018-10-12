package com.edii.spc.game.solvers;

import com.edii.spc.game.GameField;
import com.edii.spc.game.GameFieldPath;

/**
 * Rajapinta pelikentän lyhimmän polun löytävälle ratkaisijalle. 
 * 
 */
public interface Solver {
    /**
     * Ratkaisee pelikentän lyhimmän polun. 
     * 
     * @param field Ratkaistava pelikenttä
     * @return Palauttaa lyhimmän polun. 
     * @throws InterruptedException jos ratkaisu keskeytetään ennen kuin se valmistuu
     */
    public GameFieldPath solve(GameField field) throws InterruptedException;
    
    /**
     * Keskeytä ratkaisu ennen valmistumista.
     * Aiheuttaa samaan aikaan ajossa olevan solve()-operaation päätymään InterruptedException -virheesen.
     */
    public void interrupt();
}
