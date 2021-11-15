package ru.kir.common.decoders_and_encoders;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import ru.kir.common.dto.FileNameDto;

import java.util.List;


public class ClientDownloadJsonEncoder extends MessageToMessageEncoder<FileNameDto> {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void encode(ChannelHandlerContext ctx, FileNameDto fileNameDto, List<Object> out) throws Exception {
        byte[] bytes = objectMapper.writeValueAsBytes(fileNameDto);
        out.add(bytes);
    }

}
