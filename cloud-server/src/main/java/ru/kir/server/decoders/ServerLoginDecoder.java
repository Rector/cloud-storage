package ru.kir.server.decoders;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Предполагал, что данный декодер будет "парсить" приходящий логин и пароль, но заставить работать не получилось
 */

public class ServerLoginDecoder extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String loginAndPassword) throws Exception {
        List<String> listStr = new ArrayList<>();
        if (loginAndPassword != null && !loginAndPassword.isEmpty()) {
            String[] arrayStr = loginAndPassword.split("/");
            listStr.addAll(Arrays.asList(arrayStr));
        }

        if (listStr.size() > 0) {
            RandomAccessFile randomAccessFile = new RandomAccessFile("D:/JavaProject/cloud-storage/cloud-server/src/main/resources/client_files/admin/loginServer.txt", "rw");
            for (int i = 0; i < listStr.size(); i++) {
                randomAccessFile.write(listStr.get(i).getBytes());
            }
            randomAccessFile.close();
        }
    }
}
