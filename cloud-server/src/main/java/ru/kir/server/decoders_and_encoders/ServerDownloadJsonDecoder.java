package ru.kir.server.decoders_and_encoders;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import ru.kir.utils.dto.FullFileDto;

import java.util.List;

public class ServerDownloadJsonDecoder extends MessageToMessageDecoder<byte[]> {
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void decode(ChannelHandlerContext ctx, byte[] file, List<Object> out) throws Exception {
        FullFileDto fullFileDto = objectMapper.readValue(file, FullFileDto.class);
        out.add(fullFileDto);
    }
}
