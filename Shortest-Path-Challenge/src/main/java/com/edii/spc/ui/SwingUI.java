package com.edii.spc.ui;

import com.edii.spc.game.Game;
import com.edii.spc.game.GameFieldPath;
import com.edii.spc.game.solvers.DijkstraSolver;
import com.edii.spc.game.solvers.Solver;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
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
    private static final int GAME_DURATION = 30;
    
    private Game game;
    private boolean gameOver;
    private JButton newGameButton;
    private JLabel infoText;
    private GameFieldUI gameArea;
    
    private JPanel gameButtonRow;
    private JLabel pathWeightText;
    private JLabel timeLeftText;
    private JButton acceptButton;
    
    private boolean timerThreadRunning = false;
    private Thread timerThread = null;
    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            timerThreadRunning = true;
            
            int timeLeft = GAME_DURATION;
            while (!gameOver && timeLeft > 0) {
                timeLeftUpdated(timeLeft);
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
                timeLeft--;
            }
            if (!gameOver) {
                gameFinished();
            }
            timerThreadRunning = false;
        }
    };
    
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
        startTimer();
    }
    
    /**
     * Ajastimen käynnistys. 
     * 
     * Laskee sekunteja alaspäin.
     */
    private void startTimer() {
        if (timerThreadRunning) {
            timerThread.interrupt();
        }
        timerThread = new Thread(timerRunnable);
        timerThread.start();
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
        Solver solver = new DijkstraSolver();
        GameFieldPath path = solver.solve(game.getGameField());
        gameArea.setShortestPath(path);
        gameArea.setGameOver(true);
        
        GameFieldPath userPath = gameArea.getUserPath();
        String notificationText;
        if (!userPath.getEndNode().equals(game.getGameField().getFinish())) {
            notificationText = "Peli ohi! Lyhimmän polun pituus olisi ollut " + path.getWeight() + ". Sinun polku ei saavuttanut maalia.";
        } else if (userPath.getWeight() != path.getWeight()) {
            notificationText = "Peli ohi! Lyhimmän polun pituus olisi ollut " + path.getWeight() + ". Sinun polku on pidempi, pituus " + userPath.getWeight() + ".";
        } else {
            notificationText = "Onneksi olkoon, löysit lyhimmän polun!";
        }
        
        JOptionPane.showMessageDialog(this, notificationText);
    }
    
    /**
     * Tapahtuma, joka liipaistaan kun pelaaja hyväksyy sen hetkisen polun.
     */
    private void acceptClicked() {
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