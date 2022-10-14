package ru.kir.server.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.kir.common.dto.FullFileDto;

import java.io.RandomAccessFile;

public class ServerUploadFileHandler extends SimpleChannelInboundHandler<FullFileDto> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullFileDto fullFileDto) throws Exception {
        String fileName = fullFileDto.getFileName();
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "rw")) {
            randomAccessFile.seek(fullFileDto.getStartPosition());
            randomAccessFile.write(fullFileDto.getFileInBytes());
        }
    }

}
