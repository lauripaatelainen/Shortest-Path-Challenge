package com.edii.spc.ui;

import com.edii.spc.game.Game;
import com.edii.spc.game.GameField;
import com.edii.spc.game.GameFieldEdge;
import com.edii.spc.game.GameFieldNode;
import com.edii.spc.game.GameFieldPath;
import com.edii.spc.game.solvers.DijkstraSolver;
import java.util.List;

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
    
    public static void printPath(GameFieldPath path) {
        System.out.printf("Shortest path weight: %d\n", path.getWeight());
        List<GameFieldEdge> edges = path.getEdges();
        for (GameFieldEdge edge : edges) {
            GameFieldNode first = edge.getNodes().getFirst();
            System.out.printf("(%d, %d) --[%d]-> ", first.getX(), first.getY(), edge.getWeight());
        }
        System.out.printf("(%d, %d)\n", path.getEndNode().getX(), path.getEndNode().getY());
    }
    
    public static void main(String[] args) {
        DijkstraSolver dijkstra = new DijkstraSolver();
        
        for (int i = 2; i < 10; i++) {
            Game game = new Game(i);
            printGame(game);
            GameFieldPath path = dijkstra.solve(game.getGameField());
            printPath(path);
        }
    }
}
