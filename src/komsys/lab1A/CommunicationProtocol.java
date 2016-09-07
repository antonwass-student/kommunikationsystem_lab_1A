package komsys.lab1A;

/**
 * Created by Anton on 2016-09-07.
 */
public class CommunicationProtocol {
    public static String Hello(){
        return "HELLO";
    }

    public static String Ok(){
        return "OK";
    }

    public static String Busy(){
        return "BUSY";
    }

    public static String Guess(int number){
        return "GUESS " + number;
    }

    public static String Ready(){
        return "READY";
    }

    public static String Start(){
        return "START";
    }
}
