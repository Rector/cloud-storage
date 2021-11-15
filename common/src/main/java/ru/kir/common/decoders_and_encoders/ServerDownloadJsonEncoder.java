package ru.kir.common.decoders_and_encoders;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import ru.kir.common.dto.FullFileDto;

import java.util.List;

public class ServerDownloadJsonEncoder extends MessageToMessageEncoder<FullFileDto> {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void encode(ChannelHandlerContext ctx, FullFileDto fullFileDto, List<Object> out) throws Exception {
        byte[] bytes = objectMapper.writeValueAsBytes(fullFileDto);
        out.add(bytes);
    }
}
