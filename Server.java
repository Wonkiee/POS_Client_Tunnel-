package com.company;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {

    static int PORT = 2345;
    public static Socket POS,CLIENT;
    public static ArrayList<ClientHandler> objList = new ArrayList<ClientHandler>();
    public static HashMap<String,ClientHandler> objMap = new HashMap<>();

    public static void main(String[] args) {

        ServerSocket server;
        ClientHandler obj;
        try{
            server = new ServerSocket(PORT);
            System.out.println("Server Started");

            while (true) {
                try {
                    Socket client = server.accept();
                    obj = new ClientHandler(client);
                    Thread t = new Thread(obj);
                    objList.add(obj);
                    //System.out.println(objList.get(1));
                    t.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }


    }



}