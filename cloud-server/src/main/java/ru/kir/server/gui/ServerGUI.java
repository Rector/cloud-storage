package ru.kir.server.gui;

import ru.kir.server.core.ServerCore;
import ru.kir.server.core.ServerWatch;

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

    private final ServerCore SERVER_CORE = new ServerCore();
    private final ServerWatch SERVER_WATCH = new ServerWatch();

    private final JButton BTN_START = new JButton("Start");
    private final JButton BTN_STOP = new JButton("Stop");
    private final JPanel PANEL = new JPanel(new GridLayout(1, 2));
    private final JTextArea LOG_AREA = new JTextArea();

    /**
     * Настраивааются параметры графического интерфейса сервера
     */

    private ServerGUI() {
        new Thread(SERVER_WATCH::watchPackage).start();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle(WINDOW_TITLE);

// todo: подумать, нужно ли, чтобы окно сервера было всегда поверх других окон
//        setAlwaysOnTop(true);

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
            System.out.println("Server started");
            new Thread(SERVER_WATCH::watchPackage).start();
            SERVER_CORE.start();
        }

        if (action.equals(BTN_STOP)) {
            System.out.println("Server stopped");
            SERVER_WATCH.setCheckIsActive(false);
            SERVER_CORE.stop();
        }
    }

}
