package com.company;


import java.io.*;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.Scanner;

class ClientHandler implements Runnable{

    private Socket client;
    public ClientHandler(Socket client){
        this.client = client;
    }

    PrintWriter writer;
    OutputStream out;
    BufferedReader reader;
    boolean communicationDone = false;
    @Override
    public void run() {
        //Streams
        try{
            out = client.getOutputStream();
            writer = new PrintWriter(out);

            InputStream in = client.getInputStream();
            reader = new BufferedReader((new InputStreamReader(in)));
            PrintStream sOut = new PrintStream(client.getOutputStream());

            String text = null;
            Scanner scan= new Scanner(System.in);

            while ((text = reader.readLine()) != null) {

                if(text.equals("POS"))
                    Server.objMap.put("POS",this);
                //setSockets(text);
                communicate(text);
                System.out.println("Client: " + text);

                //String temp = text.substring(0, text.length() - 5);
                //System.out.println(text);
                if( text.equals("SSH_SUCCESS:8000") )
                    is_SSH_Success(text);
            }
            writer.close();
            reader.close();
            client.close();
        }catch(Exception e){

        }
    }

    /*
    private void setSockets(String soc){
        if( soc.equals("CLIENT@@@") ) {
            Server.CLIENT = client;
            System.out.println(Server.CLIENT);
        }
        if( soc.equals("POS@@@") ) {
            Server.POS = client;
            System.out.println(Server.POS);
        }

    }
*/

    private void communicate(String text){

        if(text.equals("SSH_ME")) {
            ClientHandler ch = Server.objMap.get("POS");//Server.objList.get(0);
            PrintWriter wr = ch.writer;
            wr.write("SSH2SERVER"+ "\n");
            wr.flush();
            communicationDone = true;
        }

    }

    private void is_SSH_Success(String text){

        if( (text.substring(0, text.length() - 5)).equals("SSH_SUCCESS")){
            ClientHandler ch = Server.objList.get(1);
            PrintWriter wr = ch.writer;
            wr.write("SSHto"+text.substring(11, text.length())+"\n");
            wr.flush();
        }
    }
}