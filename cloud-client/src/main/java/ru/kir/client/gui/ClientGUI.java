package ru.kir.client.gui;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGUI extends JFrame implements ActionListener {
    private final String HOST = "localhost";
    private final int PORT = 8800;

    private final int WIDTH = 400;
    private final int HEIGHT = 300;

    private final JTextArea LOG_AREA = new JTextArea();
    private final JPanel PANEL_LOG_IN = new JPanel(new GridLayout(1, 3));
    private final JPanel PANEL_LOGOUT = new JPanel(new GridLayout(1,2));

    private final String WINDOW_TITLE = "Client Cloud Storage";

    private final JTextField TF_LOGIN = new JTextField("Admin");
    private final JPasswordField TF_PASSWORD = new JPasswordField("100");
    private final JButton BTN_LOG_IN = new JButton("Log in");

    private final JButton BTN_LOGOUT = new JButton("Logout");
    private final JButton BTN_SEND = new JButton("Send");

/**
 * Настраивааются параметры графического интерфейса клиента
 * */

    private ClientGUI(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
// todo: подумать, нужно ли, чтобы окно клиента было всегда поверх других окон
//        setAlwaysOnTop(true);
        setTitle(WINDOW_TITLE);

        JScrollPane scrollLogArea = new JScrollPane(LOG_AREA);
        LOG_AREA.setLineWrap(true);
        LOG_AREA.setWrapStyleWord(true);
        LOG_AREA.setEditable(false);

        BTN_LOG_IN.addActionListener(this);
        BTN_SEND.addActionListener(this);

        PANEL_LOG_IN.add(TF_LOGIN);
        PANEL_LOG_IN.add(TF_PASSWORD);
        PANEL_LOG_IN.add(BTN_LOG_IN);

        PANEL_LOGOUT.add(BTN_LOGOUT);
        PANEL_LOGOUT.add(BTN_SEND);
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
        if(action == BTN_SEND){
            sendFile();
        } else if(action == BTN_LOG_IN){
            PANEL_LOG_IN.setVisible(false);
            PANEL_LOGOUT.setVisible(true);
        } else if(action == BTN_LOGOUT){
            PANEL_LOGOUT.setVisible(false);
            PANEL_LOG_IN.setVisible(true);
        }
    }

/**
 * Путь к файлу передаётся на сервер
 * */

    private void sendFile(){
        NioEventLoopGroup workingGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workingGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new StringDecoder(), new StringEncoder());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(HOST, PORT).sync();

            String pathToFile = "D:/JavaProject/cloud-storage/cloud-client/src/main/resources/testClient.txt";
            channelFuture.channel().writeAndFlush(pathToFile).sync();

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workingGroup.shutdownGracefully();
        }

    }

}
