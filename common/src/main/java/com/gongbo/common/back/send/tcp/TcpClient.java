package com.gongbo.common.back.send.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TcpClient {

    public static void send(String host, int port, String content) throws IOException {
        try ( SocketChannel channel = SocketChannel.open() )
        {
            channel.configureBlocking(true);
            channel.connect(new InetSocketAddress(host, port));
            byte[] data = content.getBytes();
            ByteBuffer buf = ByteBuffer.allocate(data.length);
            buf.put(data);
            buf.flip();
            while ( buf.hasRemaining() )
                channel.write(buf);
        }
    }
}
