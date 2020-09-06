package it.maranzana.socket.server;

import java.net.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class TcpChatService {

    private static AtomicReference<PublicChannel> chat;
    static int portNumber = 10000;

    public static AtomicReference<PublicChannel> getInstance(){
        return chat;
    }

    public static void main(String[] args){
        ExecutorService executor = null;
        chat = new AtomicReference<>(new PublicChannel());
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            executor = Executors.newCachedThreadPool();
            System.out.println("Service Listening on port " + portNumber);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                if(clientSocket!=null) {
                    ChatHandler worker = new ChatHandler(clientSocket);
                    getInstance().get().addObserver(worker);
                    executor.submit(worker);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
