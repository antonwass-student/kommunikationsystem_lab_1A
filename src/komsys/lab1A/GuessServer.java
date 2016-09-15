package komsys.lab1A;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Anton on 2016-09-07.
 */
public class GuessServer {
    private static final int PORT = 1234;
    private static final int MAXBUFFER = 1024;

    private DatagramSocket socket;

    private ArrayList<GameSession> gameSessions;

    public GuessServer(){
        socket = null;
        gameSessions = new ArrayList<>();
    }

    private void Game() throws IOException{
        recive();
    }

    public void ProcessMessage(String message, int port, InetAddress addr){

        String[] args = message.split(" ");

        cleanFinishedSessions(); //Remove sessions that are finished.

        GameSession gs = getGameSession(port,addr); //Get active gamesession with client if there is one

        switch(args[0]){
            case "HELLO":
                if(gs == null){
                    gameSessions.add(new GameSession(port,addr));
                    send("OK",port,addr);

                }else{
                    send("ERROR", port, addr);
                }
                break;
            case "START":
                if(gs == null){
                    send("ERROR", port, addr);
                }else{
                    send(gs.start(), port, addr);
                }

                break;
            case "GUESS":
                if(gs == null){
                    send("ERROR", port, addr);
                }else{
                    try{
                        int number = Integer.parseInt(args[1]);
                        send(gs.guess(number), port, addr);
                    }catch(Exception e){
                        send("ERROR", port, addr);
                    }
                }
                break;
            default:
                send("ERROR", port, addr);
                break;
        }
    }

    private void cleanFinishedSessions(){
        ArrayList<GameSession> toRemove = new ArrayList<>();

        for(GameSession gs : gameSessions){
            if(gs.isFinished())
                toRemove.add(gs);
        }

        gameSessions.removeAll(toRemove);

    }

    private GameSession getGameSession(int port, InetAddress addr){
        for (GameSession gs: gameSessions) {
            if(gs.compare(port,addr)) return gs;
        }
        return null;
    }

    private void send(String message, int port, InetAddress address){
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data,data.length,address,port);
        try{
            socket.send(packet);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void recive() throws IOException{
        while(true){
            byte[] buffer = new byte[MAXBUFFER];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try{
                System.out.println("Waiting for message...");
                socket.receive(packet);
                String message = new String(packet.getData(),0,packet.getLength());
                ProcessMessage(message,packet.getPort(),packet.getAddress());
            }catch(IOException e){
                System.out.println("Socket error");
            }
        }
    }


    private void start() throws IOException{
        try {
            socket = new DatagramSocket(PORT);
            Game();
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
