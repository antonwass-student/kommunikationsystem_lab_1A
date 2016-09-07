package komsys.lab1A;

import java.net.InetAddress;
import java.util.Random;

/**
 * Created by chris on 2016-09-07.
 */
public class GameSession{
    private InetAddress address;
    private int port;
    private boolean finished = false;
    private boolean started = false;

    private int theNumber;
    private int prevGuessedNumber = 0;

    public GameSession(int port, InetAddress address){
        this.port = port;
        this.address = address;
    }

    public String prevGuess(){
        return guess(prevGuessedNumber);
    }

    public boolean isFinished(){
        return finished;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String start(){
        Random r =  new Random();
        theNumber = r.nextInt(100);
        started = true;
        return CommunicationProtocol.Ready();
    }

    public String guess(int number){
        prevGuessedNumber = number;
        if(number == theNumber){
            finished = true;
            return CommunicationProtocol.Correct();
        }else if(number < theNumber){
            return CommunicationProtocol.Higher();
        }else{
            return CommunicationProtocol.Lower();
        }
    }

    public boolean compare(int port, InetAddress addr){
        if(this.port == port && this.address.equals(addr)){
            return true;
        }else{
            return false;
        }
    }

}
