package ru.kir.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import ru.kir.client.handlers.ClientDownloadFileHandler;
import ru.kir.common.decoders_and_encoders.ClientDownloadJsonDecoder;
import ru.kir.common.decoders_and_encoders.ClientDownloadJsonEncoder;
import ru.kir.common.decoders_and_encoders.ClientUploadJsonEncoder;
import ru.kir.common.dto.FileNameDto;
import ru.kir.common.dto.FullFileDto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import static ru.kir.common.ParametersForFileTransfer.*;

public class WorkingWithFiles {
    private byte[] bigBuffer = new byte[MAX_FRAME_LENGTHS];

    /**
     * Скачивание файла с сервера
     */
    public void downloadFileFromServer(String host, int port, String pathDownloadFile) {
        NioEventLoopGroup workingGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workingGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new LengthFieldBasedFrameDecoder(MAX_FRAME_LENGTHS, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTHS, LENGTH_ADJUSTMENT, INITIAL_BYTES_TO_STRIP),
                                    new LengthFieldPrepender(LENGTH_FIELD_LENGTHS),
                                    new ByteArrayDecoder(),
                                    new ByteArrayEncoder(),
                                    new ClientDownloadJsonDecoder(),
                                    new ClientDownloadJsonEncoder(),
                                    new ClientDownloadFileHandler()
                            );
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

            FileNameDto fileNameDto = new FileNameDto();
            fileNameDto.setFileName(pathDownloadFile);
            channelFuture.channel().writeAndFlush(fileNameDto).sync();

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workingGroup.shutdownGracefully();
        }
    }

    /**
     * Передача файла на сервер
     */

    public void uploadFileToServer(String host, int port, String pathUploadFile) {
        NioEventLoopGroup workingGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workingGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new LengthFieldBasedFrameDecoder(MAX_FRAME_LENGTHS, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTHS, LENGTH_ADJUSTMENT, INITIAL_BYTES_TO_STRIP),
                                    new LengthFieldPrepender(LENGTH_FIELD_LENGTHS),
                                    new ByteArrayDecoder(),
                                    new ByteArrayEncoder(),
                                    new ClientUploadJsonEncoder()
                            );
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

            try (RandomAccessFile randomAccessFile = new RandomAccessFile(pathUploadFile, "r")) {
                while (true) {
                    FullFileDto fullFileDto = new FullFileDto();
                    fullFileDto.setFileName(SAVE_FILE_ON_SERVER);
                    fullFileDto.setStartPosition(randomAccessFile.getFilePointer());
                    int read = randomAccessFile.read(bigBuffer);

                    if (read == 0 || read == -1) {
                        break;
                    }

                    if (read < bigBuffer.length) {
                        byte[] smallBuffer = new byte[read];
                        System.arraycopy(bigBuffer, 0, smallBuffer, 0, smallBuffer.length);
                        fullFileDto.setFileInBytes(smallBuffer);
                        channelFuture.channel().writeAndFlush(fullFileDto).sync();
                        break;
                    } else {
                        fullFileDto.setFileInBytes(bigBuffer);
                        channelFuture.channel().writeAndFlush(fullFileDto).sync();
                    }
                }
            }

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            workingGroup.shutdownGracefully();
        }
    }

}
