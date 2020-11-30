package com.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Client {
    private final SocketChannel sc;
    ByteBuffer _bf;

    Client(SocketChannel socket) {
        sc = socket;
        _bf = ByteBuffer.allocateDirect(32);
    }

    public SocketChannel getSc() {
        return sc;
    }

    public int getBytes() throws IOException {
        _bf.clear();
        return sc.read(_bf);
    }

    public String getMsg() throws IOException {
        _bf.clear();
        sc.read(_bf);
        CharBuffer cb = StandardCharsets.UTF_8.decode(_bf);

        return cb.toString();
    }

    public int send(String msg) throws IOException {
        //_bf = ByteBuffer.allocate(32);
        _bf.clear();
        _bf.put(msg.getBytes());
        _bf.flip();

        return sc.write(_bf);

    }


    @Override
    protected void finalize() throws Throwable {
        System.out.println("sdfsdfsd");
        super.finalize();
    }
}
