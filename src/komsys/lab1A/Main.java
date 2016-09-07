package komsys.lab1A;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        InetAddress addr = null;

        int port = -1;
        while(addr == null){
            System.out.println("Address:");
            try {
                addr = InetAddress.getByName(scanner.nextLine());
            } catch (UnknownHostException e) {
                System.out.println("Unknown host.");
            }
        }

        while(port < 1024){
            System.out.println("Port:");
            scanner.next();

            try{
                port = scanner.nextInt();
            }catch(InputMismatchException ime){
                System.out.println("That was not an integer.");
            }
        }

        try {
            DatagramSocket socket = new DatagramSocket(port, addr);
        } catch (SocketException e) {
            System.out.println("Could not bind to specified port/address.");
        }

    }
}
