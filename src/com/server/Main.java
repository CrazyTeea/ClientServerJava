package com.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {


    public static void main(String[] args) throws IOException {

        CopyOnWriteArrayList<Client> clients = new CopyOnWriteArrayList<>();

        Selector selector = null;
        ServerSocket serverSocket = null;

        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocket = serverSocketChannel.socket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(4004);
            serverSocket.bind(inetSocketAddress);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            System.err.println("Unable to setup environment");
            System.exit(-1);
        }
        new Thread(new Worker(selector, clients)).start();

        while (true) {

            int count = selector.select();
            // нечего обрабатывать
            if (count == 0) {
                continue;
            }
            Set<SelectionKey> keySet = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keySet.iterator();

            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if (selectionKey.isAcceptable()) {
                    final Socket socket = serverSocket.accept();
                    SocketChannel channel = socket.getChannel();

                    if (channel != null) {
                        channel.configureBlocking(false);
                        System.out.println("Новый клиент");
                        channel.register(selector, SelectionKey.OP_READ);
                        clients.add(new Client(channel));
                    }

                    //
                }
                clients.removeIf((socketChannel) -> !socketChannel.getSc().isOpen());


            }


        }


    }
}
