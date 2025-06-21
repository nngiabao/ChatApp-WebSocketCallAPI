import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.*;
import java.net.*;

public class Client {
    private Socket socket;
    private String username;
    private BufferedReader in;
    private BufferedWriter out;

    public Client(Socket socket,String username){
        try{
            this.socket = socket;

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //send thread msg
    public void sendMessage(){
        try{
            out.write(username);
            out.newLine();
            out.flush();
            //while connecting
            while(socket.isConnected()){
                String messageFromServer = in.readLine();
                System.out.println(messageFromServer);
                out.write(""+messageFromServer);
                out.newLine();
                out.flush();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    //receive thread
    public void receiveMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()){
                    try{
                        String messageFromServer = in.readLine();
                        System.out.println(messageFromServer);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    //

    //close everything when shutdown
    public void closeAll(Socket socket,BufferedReader read,BufferedWriter write){
        try{
            if(!socket.isConnected()){
                socket.close();
            }
            if(read!=null){
                read.close();
            }
            if(write!=null){
                write.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();
        //then open socket
        Socket socket = new Socket();
        //

    }
}
