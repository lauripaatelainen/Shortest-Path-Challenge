package com.edii.spc.ui;

import com.edii.spc.datastructures.OwnList;
import com.edii.spc.datastructures.OwnMap;
import com.edii.spc.game.Game;
import com.edii.spc.game.GameFieldPath;
import com.edii.spc.game.solvers.AStarSolver;
import com.edii.spc.game.solvers.BellmanFordSolver;
import com.edii.spc.game.solvers.DijkstraSolver;
import com.edii.spc.game.solvers.Solver;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Graafinen käyttöliittymä, joka on toteutettu javan Swing-kirjastolla.
 */
public class SwingUI extends JFrame implements GameFieldUI.PathChangedListener {
    private static final int GAME_DURATION = 10;
    
    private final List<Solver> solvers = new OwnList<>();
    {
        solvers.add(new BellmanFordSolver());
        solvers.add(new DijkstraSolver());
        solvers.add(new AStarSolver());
    }

    private Game game;
    private boolean gameOver;
    private JButton newGameButton;
    private JLabel infoText;
    private GameFieldUI gameArea;
    
    private JPanel gameButtonRow;
    private JLabel pathWeightText;
    private JLabel timeLeftText;
    private JButton acceptButton;
    
    private GameTimer currentGameTimer;
    
    private static class GameTimer implements Runnable {
        private final Runnable onTimeChange;
        private final Runnable onFinish;

        private volatile boolean canceled = false;
        private volatile boolean interrupted = false;
        private volatile boolean running = false;
        private int timeLeft;
        
        public GameTimer(int time, Runnable onTimeChange, Runnable onFinish) {
            this.timeLeft = time;
            this.onTimeChange = onTimeChange;
            this.onFinish = onFinish;
        }

        @Override
        public void run() {
            running = true;
            
            while (!interrupted && timeLeft > 0) {
                onTimeChange.run();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
                
                timeLeft--;
            }
            onTimeChange.run();
            
            if (!canceled) {
                onFinish.run();
            }
            
            running = false;
        }
        
        public void interrupt() {
            this.interrupted = true;
        }
        
        public void cancel() {
            this.canceled = true;
            interrupt();
        }
        
        public boolean isRunning() {
            return this.running;
        }
    }
    
    /**
     * Oletuskonstruktori, jossa luodaan käyttöliittymä ja alustetaan toiminnot.
     */
    public SwingUI() {
        generateUI();
        initActions();
    }

    /**
     * Luo käyttöliittymäkomponentit.
     */
    private void generateUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        newGameButton = new JButton("Uusi peli");
        newGameButton.setAlignmentX(CENTER_ALIGNMENT);
        infoText = new JLabel("Aloita peli painamalla nappia");
        infoText.setAlignmentX(CENTER_ALIGNMENT);
        
        gameButtonRow = new JPanel();
        gameButtonRow.setLayout(new BoxLayout(gameButtonRow, BoxLayout.Y_AXIS));
        
        timeLeftText = new JLabel("timeLeftText");
        acceptButton = new JButton("Vahvista vastaus");
        
        gameButtonRow.add(timeLeftText);
        gameButtonRow.add(acceptButton);
        
        gameArea = null;
        add(newGameButton);
        add(infoText);
        setSize(new Dimension(800, 800));
        setTitle("Shortest-Path-Challenge");
        setLocationByPlatform(true);
        
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                refresh();
            }
        });
    }

    /**
     * Kytkee painikkeisiin toiminnot.
     */
    private void initActions() {
        newGameButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                NewGameDialog dialog = new NewGameDialog(SwingUI.this);
                int result = dialog.showDialog();
                if (result >= 2) {
                    newGame(result);
                }
            }
        });
        acceptButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                acceptClicked();
            }
        });
    }

    /**
     * Luo uuden pelin ja kytkee sen käyttöliittymään.
     *
     * @param size Pelikentän koko
     */
    public void newGame(int size) {
        game = new Game(size);
        gameOver = false;
        generateGameArea();
        startTimer(GAME_DURATION);
    }
    
    /**
     * Ajastimen käynnistys. 
     * 
     * Laskee sekunteja alaspäin.
     */
    private void startTimer(int time) {
        if (currentGameTimer != null && currentGameTimer.isRunning()) {
            currentGameTimer.cancel();
        }
        
        Runnable onTimeChange = new Runnable() {
            @Override
            public void run() {
                timeLeftUpdated(currentGameTimer.timeLeft);
            }
        };
        
        Runnable onFinish = new Runnable() {
            @Override
            public void run() {
                if (!gameOver) {
                    gameFinished();
                }
            }
        };
        
        currentGameTimer = new GameTimer(time, onTimeChange, onFinish);
        new Thread(currentGameTimer).start();
    }
    
    /**
     * Tapahtuma, joka liipaistaan kun jäljellä oleva peliaika muuttuu.
     * 
     * @param timeLeft Jäljellä oleva peliaika.
     */
    private void timeLeftUpdated(int timeLeft) {
        timeLeftText.setText("Aikaa jäljellä: " + timeLeft);
    }
    
    /**
     * Pelin päättyessä suoritettava metodi. 
     * 
     * Metodissa tarkistetaan onko pelaajan polku lyhin ja kerrotaan lopputulos.
     * 
     * Myös pisteidenlasku tehdään tässä. 
     */
    private void gameFinished() {
        gameOver = true;
        
        Map<Solver, GameFieldPath> shortestPaths = new OwnMap<>();
        Map<Solver, Long> solverDurations = new OwnMap<>();
        int shortestPathWeight = Integer.MAX_VALUE;
        for (Solver solver : solvers) {
            GameFieldPath path;
            try {
                long start = System.currentTimeMillis();
                path = solver.solve(game.getGameField());
                long end = System.currentTimeMillis();
                shortestPathWeight = Math.min(path.getWeight(), shortestPathWeight);
                shortestPaths.put(solver, path);
                solverDurations.put(solver, end - start);
                
            } catch (InterruptedException e) {
                path = null;
            }
        }
        
        gameArea.setShortestPaths(shortestPaths);
        gameArea.setSolverDurations(solverDurations);
        gameArea.setGameOver(true);
        
        GameFieldPath userPath = gameArea.getUserPath();
        String notificationText;
        if (!userPath.getEndNode().equals(game.getGameField().getFinish())) {
            notificationText = "Peli ohi! Lyhimmän polun pituus olisi ollut " + shortestPathWeight + ". Sinun polkusi ei saavuttanut maalia.";
        } else if (userPath.getWeight() != shortestPathWeight) {
            notificationText = "Peli ohi! Lyhimmän polun pituus olisi ollut " + shortestPathWeight + ". Sinun polkusi on pidempi, pituus " + userPath.getWeight() + ".";
        } else {
            notificationText = "Onneksi olkoon, löysit lyhimmän polun!";
        }
        
        JOptionPane.showMessageDialog(this, notificationText);
    }
    
    /**
     * Tapahtuma, joka liipaistaan kun pelaaja hyväksyy sen hetkisen polun.
     */
    private void acceptClicked() {
        if (currentGameTimer != null && currentGameTimer.isRunning()) {
            currentGameTimer.cancel();
        }
        
        gameFinished();
    }

    /**
     * Tapahtuma, joka laukaistaan kun pelaajan valitsema polku muuttuu.
     * 
     * @param path Uusi polku
     */
    @Override
    public void pathChanged(GameFieldPath path) {
        pathWeightText.setText("Polun pituus: " + path.getWeight());
    }
    
    /**
     * Luo pelialueen.
     */
    private void generateGameArea() {
        if (gameArea != null) {
            this.remove(gameArea);
            this.remove(gameButtonRow);
        }
        gameArea = new GameFieldUI(game);
        this.add(gameArea);
        this.add(gameButtonRow);
        
        revalidate();
        refresh();
    }
    
    /**
     * Onko peli ohi?
     * 
     * @return 
     */
    private boolean gameOver() {
        return false;
    }
    
    /**
     * Palauttaa tämän hetkisen pistemäärän.
     * 
     * Huom. Pisteidenlasku on vielä toteuttamatta.
     * 
     * @return Palauttaa tämän hetkisen pistemäärän kokonaislukuna.
     */
    private int score() {
        return 10;
    }

    /**
     * Päivittää muuttuvat käyttöliittymän komponentit, kuten pelialueen ja
     * infotekstin.
     */
    private void refresh() {
        if (gameOver && game != null) {
            infoText.setText("Peli ohi! Pisteet: " + score());
            gameOver = true;
        } else {
            infoText.setText("Pisteet: " + ((game != null) ? score() : 0));
        }
        if (gameArea != null) {
            gameArea.refresh();
        }
    }

    /**
     * Pääohjelma graafisen käyttöliittymän käynnistykseen.
     *
     * @param args Komentoriviltä saadut parametrit
     */
    public static void main(String[] args) {
        new SwingUI().setVisible(true);
    }
}