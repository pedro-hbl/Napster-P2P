package P2P;
import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;



public class Peer {
	static Boolean control = true;
    public static void main(String[] args) throws Exception {
        DatagramSocket peerSocket = new DatagramSocket();
        
        Scanner scan = new Scanner(System.in);
        while(true && control) {     
        	// FORMATO: 'ação', 'pasta dos arquivos', 'etc..' --> E.G: join, IP, porta,
        	// C:/Desktop/pasta
        	System.out.println("Oi peer! Digite a acao que deseja fazer (join, leave, etc) e as informa"
        		+ "coes necessarias separadas por virgulas.\nE.G: join, IP, porta, C:/Desktop/pasta");
        	String actionString = scan.nextLine().trim();

        	// Conexão com o servidor (UDP)
        	serverConnection(actionString, peerSocket);

        }
    }

    public static void serverConnection(String actionString, DatagramSocket peerSocket) throws Exception {
        String infoArray[] = actionString.split(", ");
        
        int serverPort = 10098;

        // IP e porta passados pelo peer
        InetAddress IPAdress = InetAddress.getByName(infoArray[1].trim());
        int port = Integer.parseInt(infoArray[2].trim());

        // Enviar para o servidor a ação e quais arquivos o peer possui
        byte[] sendData = new byte[1024];
        if (infoArray[0].toUpperCase().trim().equals("JOIN")) sendData = (infoArray[0] + ", " + infoArray[2] + ", " + readFiles(infoArray[3])).getBytes();
        else if (infoArray[0].toUpperCase().trim().equals("SEARCH")) sendData = (infoArray[1]).getBytes();
        else sendData = actionString.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAdress, serverPort); // port ou serverPort?
        peerSocket.send(sendPacket);

        // Pacote de recebimento (recebido pelo peer, vindo do servidor)
        byte[] recBuffer = new byte[1024];
        DatagramPacket recPack = new DatagramPacket(recBuffer, recBuffer.length);
        peerSocket.receive(recPack);
        String info = new String(recPack.getData(), recPack.getOffset(), recPack.getLength());
        System.out.println(info + "\n");

        if (info.equals("JOIN_OK"))
            System.out.println("Sou peer " + IPAdress.getHostAddress() + ":" + port + " com arquivos: " + readFiles(infoArray[3]));

        
        if(info.equals("LEAVE_OK")) {
        	// Fechar a socket
        	System.out.println();
        	peerSocket.close();
        	control=false;
        }
        	
        
       
    }

    // Função para ler todos os arquivos da pasta do peer e armazenar em uma String
    public static String readFiles(String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        String fileListString = "";

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    fileListString += file.getName() + " ";
                }
            }
        }
        return fileListString;
    }
}
