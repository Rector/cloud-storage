package ru.kir.client.gui;

import ru.kir.client.WorkingWithFiles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ru.kir.utils.ParametersForFileTransfer.PATH_DOWNLOAD_FILE;
import static ru.kir.utils.ParametersForFileTransfer.PATH_UPLOAD_FILE;

public class ClientGUI extends JFrame implements ActionListener {
    private final String HOST = "localhost";
    private final int PORT = 8800;

    private final int WIDTH = 400;
    private final int HEIGHT = 300;

    WorkingWithFiles workingWithFiles;

    private final JTextArea LOG_AREA = new JTextArea();
    private final JPanel PANEL_LOG_IN = new JPanel(new GridLayout(1, 3));
    private final JPanel PANEL_LOGOUT = new JPanel(new GridLayout(1, 3));

    private final String WINDOW_TITLE = "Client Cloud Storage";

    private final JTextField TF_LOGIN = new JTextField("Admin");
    private final JPasswordField TF_PASSWORD = new JPasswordField("100");
    private final JButton BTN_LOG_IN = new JButton("Log in");

    private final JButton BTN_LOGOUT = new JButton("Logout");
    private final JButton BTN_DOWNLOAD_FILE = new JButton("Download file");
    private final JButton BTN_UPLOAD_FILE = new JButton("Upload file");

    /**
     * Настраивааются параметры графического интерфейса клиента
     */

    private ClientGUI() {
        workingWithFiles = new WorkingWithFiles();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setAlwaysOnTop(true);
        setTitle(WINDOW_TITLE);

        JScrollPane scrollLogArea = new JScrollPane(LOG_AREA);
        LOG_AREA.setLineWrap(true);
        LOG_AREA.setWrapStyleWord(true);
        LOG_AREA.setEditable(false);

        BTN_LOG_IN.addActionListener(this);
        BTN_DOWNLOAD_FILE.addActionListener(this);
        BTN_UPLOAD_FILE.addActionListener(this);
        BTN_LOGOUT.addActionListener(this);

        PANEL_LOG_IN.add(TF_LOGIN);
        PANEL_LOG_IN.add(TF_PASSWORD);
        PANEL_LOG_IN.add(BTN_LOG_IN);

        PANEL_LOGOUT.add(BTN_LOGOUT);
        PANEL_LOGOUT.add(BTN_DOWNLOAD_FILE);
        PANEL_LOGOUT.add(BTN_UPLOAD_FILE);
        PANEL_LOGOUT.setVisible(false);

        add(scrollLogArea, BorderLayout.CENTER);
        add(PANEL_LOG_IN, BorderLayout.NORTH);
        add(PANEL_LOGOUT, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClientGUI::new);
    }

    /**
     * Задаётся поведение клиента при нажатии на кнопки графического интерфейса
     **/

    @Override
    public void actionPerformed(ActionEvent e) {
        Object action = e.getSource();
        if (action.equals(BTN_DOWNLOAD_FILE)) {
            workingWithFiles.downloadFileFromServer(HOST, PORT, PATH_DOWNLOAD_FILE);
        } else if (action.equals(BTN_UPLOAD_FILE)) {
            workingWithFiles.uploadFileToServer(HOST, PORT, PATH_UPLOAD_FILE);
        } else if (action.equals(BTN_LOG_IN)) {
            PANEL_LOG_IN.setVisible(false);
            PANEL_LOGOUT.setVisible(true);
        } else if (action.equals(BTN_LOGOUT)) {
            PANEL_LOGOUT.setVisible(false);
            PANEL_LOG_IN.setVisible(true);
        }
    }

}
