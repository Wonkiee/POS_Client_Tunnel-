

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {
    
    static String ipVM1 = "104.210.54.211";
    static String ipVM2 = "104.42.125.172";
    static int PORT = 8000;

    public static void main(String args[]) throws Exception
	{
		Socket sk=new Socket("127.0.0.1",2345);
		
		//Socket sk=new Socket(ipVM1,2345);
		BufferedReader sin=new BufferedReader(new InputStreamReader(sk.getInputStream()));
		PrintStream sout=new PrintStream(sk.getOutputStream());
		BufferedReader stdin=new BufferedReader(new InputStreamReader(System.in));
		String s;
		boolean SSHSuccessFromPos = false;
		boolean SSHSuccessFromClient = false;
		while (true){
			
			
			if(SSHSuccessFromPos){
				System.out.print("Client : ");
				s = "SSH_SUCCESS:"+PORT;
			}
			else{
				System.out.print("Client : ");
				s = stdin.readLine();
			}

			if ( s.equalsIgnoreCase("BYE") ){
             	System.out.println("Connection ended by client");
 			   	break;
            }
			sout.println(s);
           
			s = sin.readLine();
			System.out.print("Server : "+s+"\n");
			if( s.equals("SSH2SERVER") ){
				SSHSuccessFromPos = callSSH(PORT, ipVM1);
				//break;
			}

			if( s.equals("SSHto:8000") ){
				SSHSuccessFromClient = callSSH(PORT,ipVM2);
			}
  			
		}

		sk.close();
		sin.close();
		sout.close();
 		stdin.close();
	}

	
	  private static boolean callSSH(int port, String ip){

        String cmd="";
        cmd = "gnome-terminal -e 'sh -c \"ssh rajithaVM1@104.210.54.211 -i VM1 -R 6000:localhost:4000; exec bash\"'";
        
        
        ProcessBuilder processBuilder = new ProcessBuilder();
        // Run a shell command
        processBuilder.command("bash", "-c", cmd);


        try {

            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                //System.out.println("Success!");
                //System.out.println(output);
                //System.exit(0);
                return true;
            } else {
            	return false;
            }

        } catch (Exception e){
        	System.out.println(e);
        	return false;
        }
        
    }
}
