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

            //get the output stream of teh socket
            out = client.getOutputStream();
            writer = new PrintWriter(out);

            //get inputs from socket
            InputStream in = client.getInputStream();

            //reads characters from the input
            reader = new BufferedReader((new InputStreamReader(in)));
            PrintStream sOut = new PrintStream(client.getOutputStream());

            String text = null;

            while ((text = reader.readLine()) != null) {
                
                //Error Handling
                if(!text.equals("SSH2SERVER") && !text.equals("POS") && !text.equals("SSH_ME") && !text.equals("SSH_SUCCESS:8000")){
			        writer.write("Wrong Command"+ "\n");
                	writer.flush();
		        }
		        System.out.println("Client: " + text);
		      
                //Store POS and CLient objects
                if(text.equals("POS"))
			         Server.objMap.put("POS",this);
                
                if(text.equals("SSH_ME")){
        			Server.objMap.put("SSH_ME",this);
        			communicate(text);
        		}
			
                if( text.equals("SSH_SUCCESS:8000") )
                    is_SSH_Success(text);
		    
            }
            writer.close();
            reader.close();
            client.close();
        }catch(Exception e){

        }
    }

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

    private void communicate(String text){

        //if(text.equals("SSH_ME")) {
            ClientHandler ch = Server.objMap.get("POS");//Server.objList.get(0);
            PrintWriter wr = ch.writer;
            wr.write("SSH2SERVER"+ "\n");
            wr.flush();
            communicationDone = true;
       // }

    }

    private void is_SSH_Success(String text){

        if( (text.substring(0, text.length() - 5)).equals("SSH_SUCCESS")){
        //if( text.equals("SSH_SUCCESS:8000") ){
            ClientHandler ch = Server.objMap.get("SSH_ME");//Server.objList.get(1);
            PrintWriter wr = ch.writer;
            wr.write("SSHto"+text.substring(11, text.length())+"\n");
            wr.flush();

        }
    }
}