package ru.kir.common.decoders_and_encoders;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import ru.kir.common.dto.FileNameDto;

import java.util.List;

public class ServerDownloadJsonDecoder extends MessageToMessageDecoder<byte[]> {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void decode(ChannelHandlerContext ctx, byte[] file, List<Object> out) throws Exception {
        FileNameDto fileNameDto = objectMapper.readValue(file, FileNameDto.class);
        out.add(fileNameDto);
    }

}
