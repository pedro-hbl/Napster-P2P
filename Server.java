package P2P;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.DatagramSocket;
import java.util.*;

public class Server  {
	public static int Users=10;
	public static int count=0;
	public static int arquivo = 30;
    private String[] peer;
    public static Thread[] listThread = new Thread[Users]; //lista de threads para checkalive
	public static int[][] PeersValidos= new int[Users][2];
	public static String[][] listFiles = new String[arquivo][2];
    
    // TODO: threads
    public static class ServerThread extends Thread {
    	private int myIndex = count;
    	
        public ServerThread(DatagramSocket serverSocket) throws Exception{
        	//Boolean conectado = false;
        	
        	try {
        		
        		
        		// Recebimento de pacote
                byte[] recBuffer = new byte[1024];
                DatagramPacket recPack = new DatagramPacket(recBuffer, recBuffer.length);
            	serverSocket.receive(recPack); // bloqueia até receber um pacote
            	
                // Tratar a mensagem recebida
                String req = new String(recPack.getData(), recPack.getOffset(), recPack.getLength());
                String action = req.split(",")[0].toUpperCase();

                // Tratar o join
                if (action.equals("JOIN")) {
                	//conectado = true;
                	
                	PeersValidos[count][0]=  Integer.parseInt(req.split(", ")[2]); //recebe porta            
                	PeersValidos[count][1]=  1; //indica que é valido
                		
                    reqJoin(req, serverSocket, recPack);
                }
                // Tratar o Leave
                else if(action.equals("LEAVE")) {   
                	PeersValidos[myIndex][1]=  0; //indica que é invalido
                	reqLeave(req, serverSocket, recPack);
                	
                }
                // Tratar o Update
                else if(action.equals("UPDATE")) {
                	
                }
                else if(action.equals("SEARCH")) {
                	
                	
                	reqSearch(req, serverSocket, recPack);
                	
                }
            	
        	} catch(Exception e) {
        		System.out.println(e);
        	}

        }

        public void run() {

        	
        }
    }

    public static void main(String[] args) {

        try {
            DatagramSocket serverSocket = new DatagramSocket(10098);
   
            
            while (true) {
                System.out.println("Esperando mensagem...");
                ServerThread thread = new ServerThread(serverSocket);
                //listThread[count]=thread;
                count++;
                //thread.sleep(30000);
                //checkAlive();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
   
    
    
    
    /*private static void checkAlive() throws Exception {
    	for (int i=0; i<count; i++) {
    		if(PeersValidos[i][1]==1) {
    			//se for valido
    			reqAlive(listThread[i]);
    		}
        	// Resposta do servidor ao peer	
            
    	}
		// TODO Auto-generated method stub
		
	}*/


	public static void reqLeave(String req, DatagramSocket ds, DatagramPacket dp) throws Exception {
    	// Resposta do servidor ao peer
        InetAddress IPAddress = dp.getAddress();
        int port = dp.getPort();
        byte[] sendBuf = new byte[1024];
        String response = "LEAVE_OK";
        sendBuf = response.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, IPAddress, port);
        ds.send(sendPacket);	
    }
    
    public static void reqUpdate(String req, DatagramSocket dk, DatagramPacket dp) {
    
    }
    
    
    
    public static void reqSearch(String req, DatagramSocket dk, DatagramPacket dp) {
    	String response;
    	// Resposta do servidor ao peer
        InetAddress IPAddress = dp.getAddress();
        int port = dp.getPort();
        byte[] sendBuf = new byte[1024];
        response = listFiles;
        sendBuf = response.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, IPAddress, port);
        ds.send(sendPacket);	
    	
    }
    
    public static void reqJoin(String req, DatagramSocket ds, DatagramPacket dp) throws Exception {

        // Informações do peer
        String filesString = req.split(",")[1].trim(); // TODO: armazená-los em um array (usar split de novo);
        System.out.println(filesString);

        // Resposta do servidor ao peer
        InetAddress IPAddress = dp.getAddress();
        int port = dp.getPort();
        byte[] sendBuf = new byte[1024];
        String response = "JOIN_OK";
        sendBuf = response.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, IPAddress, port);
        ds.send(sendPacket);

        // Após mensagem ser enviada
        System.out.println("Mensagem enviada...");
    }
    
    

}
