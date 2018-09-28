package com.edii.spc.ui;

import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Dialogi uuden pelin luontiin. Kysyy käyttäjältä vain pelin koon.
 */
public final class NewGameDialog extends JDialog {

    /**
     * Käyttäjän syöttämä pelin koko.
     */
    private int result = -1;

    /**
     * Konstruktori dialogin luontiin.
     *
     * @param owner Dialogin omistava käyttöliittymäkomponentti
     */
    public NewGameDialog(Frame owner) {
        super(owner);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        this.setLocationByPlatform(true);

        JPanel row = new JPanel();
        row.setLayout(new FlowLayout(FlowLayout.CENTER));
        row.add(new JLabel("Kentän koko: "));
        JTextField size = new JTextField();
        size.setColumns(5);
        size.setText("3");
        row.add(size);

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Peruuta");

        JPanel btnRow = new JPanel();
        btnRow.setLayout(new FlowLayout(FlowLayout.CENTER));
        btnRow.add(okButton);
        btnRow.add(cancelButton);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sizeStr = size.getText();
                try {
                    int size = Integer.parseInt(sizeStr);
                    if (size < 2 || size >= 100) {
                        throw new Exception();
                    } else {
                        result = size;
                        dispose();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(NewGameDialog.this, "Virheellinen koko!", "Virhe", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                dispose();
            }
        });

        this.add(row);
        this.add(btnRow);
        this.pack();
    }

    /**
     * Näyttää dialogin ja palautuu vasta kun dialogi suljetaan.
     *
     * @return Palauttaa käyttäjän antaman pelikentän koon tai -1 jos ruutu
     * suljettiin antamatta kelvollista kokoa.
     */
    public int showDialog() {
        setVisible(true);
        return result;
    }
}