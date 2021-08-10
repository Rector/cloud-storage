package ru.kir.server.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import ru.kir.server.decoders.ServerFileDecoder;
import ru.kir.server.decoders.ServerLoginDecoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class ServerCore {
    private Connection connection;
    private Statement statement;

    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workingGroup;

    private final String HOST = "localhost";
    private final int PORT = 8800;

    private boolean checkStartServer = false;

    private void openConnectionDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeConnectionDB() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Создание директории каждому пользователю
     */

    private void createDirectories() {
        try {
            Path path = null;
            ResultSet resultSet = statement.executeQuery("SELECT login_fld FROM users_tbl");
            while (resultSet.next()) {
                path = Paths.get("D:/JavaProject/cloud-storage/cloud-server/src/main/resources/client_files/" + resultSet.getString("login_fld").toLowerCase());
                if (!Files.isDirectory(path)) {
                    Files.createDirectory(path);
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Устанавливается соединение с базой данных и сервер начинает "слушать" порт 8800 и ожидать приёма файла от клиента
     */

    public void start() {
        if (!checkStartServer) {
            checkStartServer = true;

            openConnectionDB();
            createDirectories();

            try {
                bossGroup = new NioEventLoopGroup(1);
                workingGroup = new NioEventLoopGroup();

                ServerBootstrap serverBootstrap = new ServerBootstrap();
                serverBootstrap.group(bossGroup, workingGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                socketChannel.pipeline().addLast(new ServerLoginDecoder(), new ServerFileDecoder());
                            }
                        });
                ChannelFuture channelFuture = serverBootstrap.bind(PORT).sync();

                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                bossGroup.shutdownGracefully();
                workingGroup.shutdownGracefully();
            }
        }

    }

    /**
     * Предполагается закрытие потоков и завершение соединения с базой данных
     */

    public void stop() {
        if (checkStartServer) {
            bossGroup.shutdownGracefully();
            workingGroup.shutdownGracefully();
            closeConnectionDB();
            checkStartServer = false;
        }
    }

}
