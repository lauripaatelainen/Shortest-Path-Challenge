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
     */
    public GameFieldPath solve(GameField field);
}
