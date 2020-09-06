package it.maranzana.socket.server;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ChatHandler implements Runnable, Observer {
    private Socket client;
    private BufferedReader in;
    private BufferedWriter writer;

    public ChatHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        String user = Thread.currentThread().getName()+ ":"+ client.getPort();
        try{
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            System.out.println("User connected with name: " + user);
            String userInput;

            while ((userInput = in.readLine()) != null) {
                userInput=userInput.replaceAll("[^A-Za-z0-9 ]", "");
                TcpChatService.getInstance().get().setChanged();
                TcpChatService.getInstance().get().notifyObservers(user + " Wrote > " + userInput);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            System.out.println("User disconnected: " + user);
        }
    }

    @Override
    public void update(Observable o, Object message) {
        try{
            String userInput = (String) message;

            if(userInput.indexOf(Thread.currentThread().getName()+ ":"+ client.getPort() + " Wrote > ")<0)
                writer.write(userInput);
                writer.newLine();
                writer.flush();

        } catch (Exception ex) {
            System.out.println("update:Exception in Thread Run: " + ex);
        }
    }

}

