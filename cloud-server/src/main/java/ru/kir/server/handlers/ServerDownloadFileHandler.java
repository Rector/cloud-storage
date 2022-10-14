package ru.kir.server.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.kir.common.dto.FileNameDto;
import ru.kir.common.dto.FullFileDto;

import java.io.RandomAccessFile;

import static ru.kir.common.ParametersForFileTransfer.MAX_FRAME_LENGTHS;

public class ServerDownloadFileHandler extends SimpleChannelInboundHandler<FileNameDto> {
    private byte[] bigBuffer = new byte[MAX_FRAME_LENGTHS];

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FileNameDto fileNameDto) throws Exception {
        String fileName = fileNameDto.getFileName();

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "r")) {
            while (true) {
                FullFileDto fullFileDto = new FullFileDto();
                fullFileDto.setFileName(fileName);
                fullFileDto.setStartPosition(randomAccessFile.getFilePointer());
                int read = randomAccessFile.read(bigBuffer);

                if (read == 0 || read == -1) {
                    break;
                }

                if (read < bigBuffer.length) {
                    byte[] smallBuffer = new byte[read];
                    System.arraycopy(bigBuffer, 0, smallBuffer, 0, smallBuffer.length);
                    fullFileDto.setFileInBytes(smallBuffer);
                    ctx.writeAndFlush(fullFileDto);
                    break;
                } else {
                    fullFileDto.setFileInBytes(bigBuffer);
                    ctx.writeAndFlush(fullFileDto);
                }
            }
        }
    }

}
