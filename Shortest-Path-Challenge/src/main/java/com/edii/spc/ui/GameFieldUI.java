package com.edii.spc.ui;

import com.edii.spc.game.Game;
import com.edii.spc.game.GameFieldEdge;
import com.edii.spc.game.GameFieldNode;
import com.edii.spc.game.GameFieldPath;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.List;
import java.util.Set;
import javax.swing.JPanel;

/**
 * Pelialue, jolla graafinen käyttöliittymä renderöi pelin tilanteen.
 *
 * @see SwingUI
 */
public class GameFieldUI extends JPanel {
    /**
     * Kuuntelija, jolla voi seurata pelaajan sen hetkisen polun muuttumista.
     */
    public interface PathChangedListener {
        public void pathChanged(GameFieldPath path);
    }
    
    private PathChangedListener listener;
    private final Game game;
    private final GameFieldPath userPath;
    private GameFieldPath shortestPath;
    private boolean gameOver = false;

    /**
     * Ainoa konstruktori pelialueen luomiseen. Saa parametrinaan pelin, jonka
     * avulla määräytyy minkä kokoinen pelialue tulee näyttää ja mikä pelin
     * tilanne on.
     *
     * @param peli Peli, joka esitetään tällä pelialueella
     */
    public GameFieldUI(Game peli) {
        super();
        this.game = peli;
        this.userPath = new GameFieldPath(peli.getGameField().getStart());
        this.shortestPath = new GameFieldPath(peli.getGameField().getStart());
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (!gameOver) {
                    int x = Math.round(((float) peli.getSize() - 1) * ((float) me.getX()) / ((float) getWidth()));
                    int y = Math.round(((float) peli.getSize() - 1) * ((float) me.getY()) / ((float) getHeight()));
                    nodeClicked(x, y);
                }
            }
        });
    }

    /**
     * Tapahtuma, joka liipaistaan kun pelaaja klikkaa pelikentällä olevaa solmua. 
     * 
     * @param x Klikatun solmun x-koordinaatti.
     * @param y Klikatun solmun y-koordinaatti.
     */
    private void nodeClicked(int x, int y) {
        GameFieldNode clickedNode = this.game.getGameField().getNode(x, y);
        if (this.userPath.getNodes().contains(clickedNode)) {
            while (!this.userPath.getEndNode().equals(clickedNode)) {
                this.userPath.removeLastEdge();
                pathChanged();
            }
        } else {
            GameFieldEdge newEdge = this.userPath.getEndNode().getEdgeTo(clickedNode);
            if (newEdge != null) {
                this.userPath.addEdge(newEdge);
                pathChanged();
            }
        }
        this.refresh();
    }
    
    /**
     * Aseta lyhin polku.
     * 
     * Lyhin polku lasketaan muualla pelin logiikassa, tässä polku kerrotaan
     * käyttöliittymälle, jotta se osaa piirtää sen näkyviin.
     * 
     * @param path Lyhin polku
     */
    public void setShortestPath(GameFieldPath path) {
        this.shortestPath = path;
        refresh();
    }
    
    /**
     * Ilmoittaa pelin olevan ohi.
     * 
     * Kun peli on ohi, ei pelaaja voi enää muuttaa valitsemaansa polkua.
     * 
     * @param gameOver true jos peli on ohi.
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    
    /**
     * Hae pelaajan tämänhetkinen hetkinen polku.
     * 
     * @return Palauttaa pelaajan valitseman polun.
     */
    public GameFieldPath getUserPath() {
        return this.userPath;
    }
    
    /**
     * Tapahtuma, joka liipaistaan kun pelaajan polku muuttuu.
     */
    private void pathChanged() {
        if (listener != null) {
            listener.pathChanged(userPath);
        }
    }
    
    /**
     * Päivitä pelialue vastaamaan nykytilannetta.
     */
    public void refresh() {
        this.repaint();
        this.revalidate();
    }
    
    /**
     * Piirrä pelialue.
     * @param g Javan awt-kirjaston Graphics-olio.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        
        int width = getWidth();
        int height = getHeight();
        int cellWidth = getWidth() / game.getSize();
        int cellHeight = getHeight() / game.getSize();
        
        int fontHeight = height / game.getSize() / 3;
        int fontWidth = fontHeight / 3;
        
        g.setFont(new Font("Arial", Font.BOLD, fontHeight));
        
        Set<GameFieldNode> userPathNodes = userPath.getNodes();
        List<GameFieldEdge> shortestPathEdges = shortestPath.getEdges();
        List<GameFieldEdge> userPathEdges = userPath.getEdges();
        
        for (int y = 0; y < game.getSize(); y++) {
            for (int x = 0; x < game.getSize(); x++) {
                GameFieldNode node = game.getGameField().getNode(x, y);
                
                if (x < game.getSize() - 1) {
                    Shape line = new Line2D.Double(cellWidth * x + 2 * cellWidth / 3,
                            cellHeight * y + cellHeight / 2,
                            cellWidth * (x + 1) + cellWidth / 3,
                            cellHeight * y + cellHeight / 2
                    );
                    
                    GameFieldEdge edge = node.getRightEdge();
                    
                    if (shortestPathEdges.contains(edge) || shortestPathEdges.contains(edge.inverse())) {
                        g2.setPaint(new Color(0x0000FF));
                        g2.setStroke(new BasicStroke(30));
                        g2.draw(line);
                    }
                    
                    if (userPathEdges.contains(edge) || userPathEdges.contains(edge.inverse())) {
                        g2.setPaint(new Color(0x88EE88));
                        g2.setStroke(new BasicStroke(10));
                        g2.draw(line);
                    }
                    
                    g2.setPaint(new Color(0x000000));
                    g2.setStroke(new BasicStroke(1));
                    g2.draw(line);
                    
                    char[] w = Integer.toString(edge.getWeight()).toCharArray();
                    
                    g.drawChars(w, 0, w.length, cellWidth * (x + 1) - (w.length * fontWidth), cellHeight * y + cellHeight / 2 + 2 * fontHeight / 5);
                }
                if (y < game.getSize() - 1) {
                    Shape line = new Line2D.Double(cellWidth * x + cellWidth / 2,
                            cellHeight * y + 2 * cellHeight / 3,
                            cellWidth * x + cellWidth / 2,
                            cellHeight * (y + 1) + cellHeight / 3
                    );
                    
                    GameFieldEdge edge = node.getDownEdge();
                    
                    if (shortestPathEdges.contains(edge) || shortestPathEdges.contains(edge.inverse())) {
                        g2.setPaint(new Color(0x0000FF));
                        g2.setStroke(new BasicStroke(30));
                        g2.draw(line);
                    }
                    
                    if (userPathEdges.contains(edge) || userPathEdges.contains(edge.inverse())) {
                        g2.setPaint(new Color(0x88EE88));
                        g2.setStroke(new BasicStroke(10));
                        g2.draw(line);
                    }
                    
                    g2.setPaint(new Color(0x000000));
                    g2.setStroke(new BasicStroke(1));
                    g2.draw(line);
                    
                    char[] w = Integer.toString(edge.getWeight()).toCharArray();
                    
                    g.drawChars(w, 0, w.length, cellWidth * x + cellWidth / 2 - (w.length * fontWidth), cellHeight * (y + 1) + 2 * fontHeight / 5);
                }
                
                
                Shape nodeCircle = new Ellipse2D.Double(cellWidth * x + cellWidth / 3, cellHeight * y + cellHeight / 3, cellWidth / 3, cellHeight / 3);
                
                int nodeColor = 0;
                if (userPath.getEndNode().equals(node)) {
                    nodeColor = 0x00FF00;
                } else if (userPathNodes.contains(node)) {
                    nodeColor = 0x88EE88;
                } else {
                    nodeColor = 0xFFFFFF;
                }
                
                g2.setPaint(new Color(nodeColor));
                g2.fill(nodeCircle);
                g2.setPaint(new Color(0x000000));
                g2.draw(nodeCircle);
            }
        }
    }
}