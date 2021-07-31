package ru.kir.server.decoders;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.RandomAccessFile;

/**
 * Ожидается путь к файлу, который клиент хочет сохранить на сервере и файл сохраняется на сервере
 * */

public class ServerFileDecoder extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String pathToFile) throws Exception {
        RandomAccessFile fileToRead = new RandomAccessFile(pathToFile, "r");
        RandomAccessFile fileToWrite = new RandomAccessFile("D:/JavaProject/cloud-storage/cloud-server/src/main/resources/testServer.txt", "rw");

// todo: подумать над буфером и его объёмом + предусмотреть варианты при существовании файла

        byte[] buffer = new byte[(int) fileToRead.length()];

        int read;
        while ((read = fileToRead.read(buffer)) != -1) {
            fileToWrite.write(buffer, 0, read);
        }

        fileToRead.close();
        fileToWrite.close();
    }
}
