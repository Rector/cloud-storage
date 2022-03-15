package ru.kir.server.gui;

import ru.kir.server.core.ServerCore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerGUI extends JFrame implements ActionListener {
    private final int POS_X = 800;
    private final int POS_Y = 200;
    private final int WIDTH = 600;
    private final int HEIGHT = 300;
    private final String WINDOW_TITLE = "Server Cloud Storage";

    private ServerCore serverCore;


    private final JButton BTN_START = new JButton("Start");
    private final JButton BTN_STOP = new JButton("Stop");
    private final JPanel PANEL = new JPanel(new GridLayout(1, 2));
    private final JTextArea LOG_AREA = new JTextArea();

    /**
     * Настраиваются параметры графического интерфейса сервера
     */

    private ServerGUI() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle(WINDOW_TITLE);

        setAlwaysOnTop(true);

        LOG_AREA.setEditable(false);
        LOG_AREA.setLineWrap(true);
        JScrollPane scrollLogArea = new JScrollPane(LOG_AREA);

        BTN_START.addActionListener(this);
        BTN_STOP.addActionListener(this);

        PANEL.add(BTN_START);
        PANEL.add(BTN_STOP);
        add(PANEL, BorderLayout.NORTH);
        add(scrollLogArea, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ServerGUI::new);
    }

    /**
     * Задаётся поведение сервера при нажатии на кнопки графического интерфейса
     */

    @Override
    public void actionPerformed(ActionEvent e) {
        Object action = e.getSource();
        if (action.equals(BTN_START)) {
            if(serverCore == null){
                serverCore = new ServerCore();
                serverCore.start();
            }
        } else if (action.equals(BTN_STOP)) {
            if(serverCore != null && serverCore.isAlive()){
                serverCore.stopServer();
                serverCore = null;
            }
        }
    }

}
