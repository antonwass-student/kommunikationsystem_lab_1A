package komsys.lab1A;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by Anton on 2016-09-07.
 */
public class GuessClient {
    private Scanner scanner = new Scanner(System.in);
    private DatagramSocket socket = null;
    private InetAddress addr = null;
    private int port = -1;

    public GuessClient(){

    }

    public void start(){
        chooseAddress();
        connect();
    }

    private void chooseAddress(){
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
    }

    private void connect(){
        while(socket == null){
            try {
                socket = new DatagramSocket(port, addr);
            } catch (SocketException e) {
                System.out.println("Could not bind to specified port/address.");
            }
        }
    }

    public static void main(String[] args) {
        GuessClient client = new GuessClient();
        client.start();
    }


}
