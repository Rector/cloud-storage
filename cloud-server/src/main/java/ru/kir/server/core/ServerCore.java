package ru.kir.server.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import ru.kir.server.WorkWithDB;
import ru.kir.server.handlers.ServerUploadFileHandler;
import ru.kir.server.decoders_and_encoders.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

import static ru.kir.utils.ParametersForFileTransfer.*;

public class ServerCore {
    private Statement statement;

    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workingGroup;

    private final String HOST = "localhost";
    private final int PORT = 8800;

    private boolean checkStartServer = false;


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

            statement = WorkWithDB.getStatement();
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
                                socketChannel.pipeline().addLast(
                                        new LengthFieldBasedFrameDecoder(MAX_FRAME_LENGTHS, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTHS, LENGTH_ADJUSTMENT, INITIAL_BYTES_TO_STRIP),
                                        new LengthFieldPrepender(LENGTH_FIELD_LENGTHS),
                                        new ByteArrayDecoder(),
                                        new ByteArrayEncoder(),
                                        new ServerUploadJsonDecoder(),
                                        new ServerUploadJsonEncoder(),
                                        new ServerUploadFileHandler()
//                                        new ServerDownloadJsonDecoder(),
//                                        new ServerDownloadFileHandler()
                                );
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
            WorkWithDB.closeConnectionDB();
            checkStartServer = false;
        }
    }

}
