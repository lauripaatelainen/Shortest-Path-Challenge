package com.edii.spc.ui;

import com.edii.spc.datastructures.OwnList;
import com.edii.spc.datastructures.OwnMap;
import com.edii.spc.game.Game;
import com.edii.spc.game.GameFieldEdge;
import com.edii.spc.game.GameFieldNode;
import com.edii.spc.game.GameFieldPath;
import com.edii.spc.game.solvers.Solver;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;

/**
 * Pelialue, jolla graafinen käyttöliittymä renderöi pelin tilanteen.
 *
 * @see SwingUI
 */
public class GameFieldUI extends JPanel {
    private static final int PATH_COLOR = 0x000000;
    private static final int NODE_EDGE_COLOR = 0x000000;
    private static final int EMPTY_NODE_COLOR = 0xFFFFFF;
    
    private static final int USER_PATH_COLOR = 0x88FF88;
    private static final int USER_PATH_END_NODE_COLOR = 0x00FF00;
    private static final int USER_PATH_NODE_COLOR = 0x88FF88;
    
    private static final int[] SHORTEST_PATH_COLORS = {
        0xFF4444, 0xFFFF00, 0xFF00FF
    };

    /**
     * Kuuntelija, jolla voi seurata pelaajan sen hetkisen polun muuttumista.
     */
    public interface PathChangedListener {

        public void pathChanged(GameFieldPath path);
    }

    private PathChangedListener listener;
    private final Game game;
    private final GameFieldPath userPath;
    private Map<Solver, GameFieldPath> shortestPaths = new OwnMap<>();
    private Map<Solver, Long> solverDurations = new OwnMap<>();
    private List<Solver> solvers = new OwnList<>();
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
     * Tapahtuma, joka liipaistaan kun pelaaja klikkaa pelikentällä olevaa
     * solmua.
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
     * @param paths Lyhin polku
     */
    public void setShortestPaths(Map<Solver, GameFieldPath> paths) {
        this.shortestPaths = paths;
        refresh();
    }

    public void setSolverDurations(Map<Solver, Long> durations) {
        this.solverDurations = durations;
        this.solvers.clear();
        this.solvers.addAll(durations.keySet());
        Collections.sort(this.solvers, new Comparator<Solver>() {
            @Override
            public int compare(Solver t, Solver t1) {
                return solverDurations.get(t).compareTo(solverDurations.get(t1));
            }
        });
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

    private int width = getWidth();
    private int height = getHeight();
    private int cellWidth = 1;
    private int cellHeight = 1;
    private int fontHeight = 1;
    private int fontWidth = 1;

    /**
     * Piirrä pelialue.
     *
     * @param g Javan awt-kirjaston Graphics-olio.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform originalTransform = g2.getTransform();

        width = getWidth();
        height = getHeight();
        cellWidth = width / game.getSize();
        cellHeight = height / game.getSize();

        fontHeight = height / game.getSize() / 3;
        fontWidth = fontHeight / 3;

        g.setFont(new Font("Arial", Font.BOLD, fontHeight));

        paintShortestPaths(g2);
        paintUserPath(g2);
        paintGameField(g2);
    }

    private void paintShortestPaths(Graphics2D g2) {
        for (int i = 0; i < solvers.size(); i++) {
            g2.setPaint(new Color(SHORTEST_PATH_COLORS[i % SHORTEST_PATH_COLORS.length]));
            g2.setStroke(new BasicStroke(10 + 10 * (solvers.size() - i)));
            paintPath(g2, shortestPaths.get(solvers.get(i)));
        }
    }

    private void paintUserPath(Graphics2D g2) {
        g2.setPaint(new Color(USER_PATH_COLOR));
        g2.setStroke(new BasicStroke(10));
        paintPath(g2, userPath);
    }
    
    private void paintPath(Graphics2D g2, GameFieldPath path) {
        for (GameFieldEdge edge : path.getEdges()) {
            Shape line = new Line2D.Double(cellWidth * edge.getNodes().getFirst().getX()  + cellWidth / 2,
                cellHeight * edge.getNodes().getFirst().getY() + cellHeight / 2,
                cellWidth * edge.getNodes().getSecond().getX() + cellWidth / 2,
                cellHeight * edge.getNodes().getSecond().getY() + cellHeight / 2
            );
            g2.draw(line);
        }
    }

    private void paintGameField(Graphics2D g2) {
        Set<GameFieldNode> userPathNodes = userPath.getNodes();
        
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

                    g2.setPaint(new Color(0x000000));
                    g2.setStroke(new BasicStroke(1));
                    g2.draw(line);

                    char[] w = Integer.toString(edge.getWeight()).toCharArray();

                    g2.drawChars(w, 0, w.length, cellWidth * (x + 1) - (w.length * fontWidth), cellHeight * y + cellHeight / 2 + 2 * fontHeight / 5);
                }
                if (y < game.getSize() - 1) {
                    Shape line = new Line2D.Double(cellWidth * x + cellWidth / 2,
                            cellHeight * y + 2 * cellHeight / 3,
                            cellWidth * x + cellWidth / 2,
                            cellHeight * (y + 1) + cellHeight / 3
                    );

                    GameFieldEdge edge = node.getDownEdge();

                    g2.setPaint(new Color(PATH_COLOR));
                    g2.setStroke(new BasicStroke(1));
                    g2.draw(line);

                    char[] w = Integer.toString(edge.getWeight()).toCharArray();

                    g2.drawChars(w, 0, w.length, cellWidth * x + cellWidth / 2 - (w.length * fontWidth), cellHeight * (y + 1) + 2 * fontHeight / 5);
                }

                Shape nodeCircle = new Ellipse2D.Double(cellWidth * x + cellWidth / 3, cellHeight * y + cellHeight / 3, cellWidth / 3, cellHeight / 3);

                int nodeColor;
                
                if (userPath.getEndNode().equals(node)) {
                    nodeColor = USER_PATH_END_NODE_COLOR;
                } else if (userPathNodes.contains(node)) {
                    nodeColor = USER_PATH_NODE_COLOR;
                } else {
                    nodeColor = EMPTY_NODE_COLOR;
                }
                
                g2.setPaint(new Color(nodeColor));
                g2.fill(nodeCircle);
                g2.setPaint(new Color(NODE_EDGE_COLOR));
                g2.draw(nodeCircle);
            }
        }
    }
}
