package komsys.lab1A;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

/**
 * Created by Anton on 2016-09-07.
 */
public class GuessServer {
    private static final int PORT = 1234;
    private static final int MAXBUFFER = 1024;
    private InetAddress currentClientAddress;
    private int currentClientPort;

    private DatagramSocket socket;

    public GuessServer(){
        socket = null;
        currentClientAddress = null;
    }

    private void GameSession() throws IOException{
        try{
            System.out.println("Game session started.");
            if (recive().equals("HELLO")){
                Game();
            }
        }finally{

        }
    }

    private void Game() throws IOException{
        send("OK");
        if(recive().equals("START")){
            send("READY");

            Random r =  new Random();
            int number = r.nextInt(100);

            /*while (true) {

            }*/
        }



    }

    private void send(String message){
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data,data.length,currentClientAddress,currentClientPort);
        try{
            socket.send(packet);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private String recive() throws IOException{
        byte[] buffer = new byte[MAXBUFFER];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try{
            System.out.println("Waiting for message...");
            socket.receive(packet);
            if(currentClientAddress == null){
                currentClientAddress = packet.getAddress();
                currentClientPort = packet.getPort();
            }
            if(!packet.getAddress().equals(currentClientAddress)) {
                send("BUSY");
                return "";
            }

        }catch(IOException e){
            return "";
        }
        System.out.println("Package recived from "+ packet.getAddress());
        return new String(packet.getData(), 0, packet.getLength());
    }


    private void start() throws IOException{
        try {
            socket = new DatagramSocket(PORT);
            GameSession();
        }finally {
            socket.close();
            System.out.println("Server closed.");
        }
    }

    public static void main(String[] args) throws IOException{
        GuessServer server = new GuessServer();
        server.start();
    }


}
