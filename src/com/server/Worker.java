package com.server;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.concurrent.CopyOnWriteArrayList;

public class Worker implements Runnable {
    private final CopyOnWriteArrayList<Client> _clients;

    private final Selector _selector;

    Worker(Selector selector, CopyOnWriteArrayList<Client> clients) {
        _clients = clients;
        _selector = selector;
    }

    @Override
    public void run() {

        while (true) {


            for (Client client : _clients) {
                try {

                    int n = client.getBytes();

                    if (n == 0) {
                        //System.out.println(n);
                        continue;

                    }

                    String bytes = client.getMsg().replaceAll("\\s", "");

                    System.out.println(bytes);

                    if (bytes.contains("stop") || !client.getSc().isConnected()) {
                        System.out.println("Клиент гавнюк");
                        _clients.remove(client);
                    }

                    System.out.println("Отправляю ответ");
                    System.out.println("Отправлено" + client.send(bytes));
                    System.out.println("Готово");

                    // System.out.println("лул");
                } catch (IOException e) {
                    _clients.remove(client);
                    System.out.println(e.getMessage());
                }


            }

            //  System.out.println(_clients.toString());

        }

    }
}
