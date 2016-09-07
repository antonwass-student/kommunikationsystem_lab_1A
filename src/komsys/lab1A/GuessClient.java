package komsys.lab1A;

import java.io.IOException;
import java.net.*;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by Anton on 2016-09-07.
 */
public class GuessClient {
    private Scanner scanner = new Scanner(System.in);
    private DatagramSocket socket = null;
    private InetAddress address = null;
    private int port = -1;

    public void start(){
        address = getDestinationAddress();
        port = getDestinationPort();

        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            System.out.println("Could not create socket...");
            return;
        }

        play();
    }

    private void play(){
        boolean playing = true;


        send(CommunicationProtocol.Hello());
        System.out.println("Poking server...");

        while(playing){

            String message = receive(5000);

            switch(message){
                case "READY":
                    System.out.println("Server is ready. Guess a number!");
                    scanner.next();
                    send(CommunicationProtocol.Guess(readIntSafe()));
                    break;
                case "OK":
                    System.out.println("Server is available.");
                    send(CommunicationProtocol.Start());
                    break;
                case "HI":
                    System.out.println("Higher");
                    System.out.println("Guess:");
                    scanner.next();
                    send(CommunicationProtocol.Guess(readIntSafe()));
                    break;
                case "LO":
                    System.out.println("Lower");
                    System.out.println("Guess:");
                    scanner.next();
                    send(CommunicationProtocol.Guess(readIntSafe()));
                    break;
                case "CORRECT":
                    System.out.println("Correct!");
                    playing = false;
                    break;
                case "BUSY":
                    System.out.println("Server is busy!");
                    playing = false;
                    break;
                case "STO":
                    System.out.println("Request timed out. Exiting program.");
                    playing = false;
                    break;
                default:
                    System.out.println("Could not interpret message...");
                    break;
            }
        }
    }

    private void send(String message){
        byte[] data = message.getBytes();
        DatagramPacket pkt = new DatagramPacket(data, 0, data.length);
        pkt.setAddress(address);
        pkt.setPort(port);
        try {
            socket.send(pkt);
        } catch (IOException e) {
            System.out.println("Could not send");
        }
    }

    private String receive(int timeout){
        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, 0 , data.length);
        try {
            socket.setSoTimeout(timeout);
        } catch (SocketException e) {
            System.out.println("UDP Error...");
            return "";
        }

        try {
            socket.receive(packet);
        } catch (IOException e) {
            System.out.println("Request timed out...");
            return "STO";
        }

        return new String(packet.getData(), 0, packet.getLength());
    }

    private int readIntSafe(){
        int number = 0;
        boolean done = false;
        while(!done){
            try{
                number = scanner.nextInt();
                done = true;
            }catch(InputMismatchException ime){
                System.out.println("Please enter an integer.");
                scanner.next();
            }

        }

        return number;
    }

    private int getDestinationPort(){
        int p = -1;

        while(p < 1024){
            System.out.println("Port:");
            p = readIntSafe();
        }

        return p;
    }

    private InetAddress getDestinationAddress(){
        InetAddress a = null;
        while(a == null){
            System.out.println("Address:");
            try {
                a = InetAddress.getByName(scanner.nextLine());
            } catch (UnknownHostException e) {
                System.out.println("Unknown host.");
            }
        }

        return a;
    }

    public static void main(String[] args) {
        GuessClient client = new GuessClient();
        client.start();
    }
}
