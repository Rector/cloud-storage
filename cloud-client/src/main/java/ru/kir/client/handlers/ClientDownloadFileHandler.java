package ru.kir.client.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.kir.utils.dto.FullFileDto;

import java.io.RandomAccessFile;

import static ru.kir.utils.ParametersForFileTransfer.SAVE_FILE_ON_CLIENT;

public class ClientDownloadFileHandler extends SimpleChannelInboundHandler<FullFileDto> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullFileDto fullFileDto) throws Exception {
        String fileName = SAVE_FILE_ON_CLIENT;
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "rw")){
            randomAccessFile.seek(fullFileDto.getStartPosition());
            randomAccessFile.write(fullFileDto.getFileInBytes());
        }
    }
}
