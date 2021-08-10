package ru.kir.server.decoders;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.RandomAccessFile;

/**
 * Ожидается получение данных от клиента. Полученные данные сохраняются в файл на сервере
 */

// todo: подумать над буфером и его объёмом + предусмотреть варианты при существовании файла

public class ServerFileDecoder extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        RandomAccessFile fileToWrite = new RandomAccessFile("D:/JavaProject/cloud-storage/cloud-server/src/main/resources/client_files/admin/testServer.txt", "rw");
        byte[] buffer = ByteBufUtil.getBytes(byteBuf);
        fileToWrite.write(buffer);
        fileToWrite.close();
    }
}
