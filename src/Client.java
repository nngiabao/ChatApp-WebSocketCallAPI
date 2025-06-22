import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.*;
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
            this.username = username;
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
        }).start();
    }
    //send msg
    public void sendMessage() {
        try {
            // Send your username as the first line
            out.write(username);
            out.newLine();
            out.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                out.write( messageToSend); // Only send
                out.newLine();
                out.flush();
            }
        } catch (IOException e) {
            closeAll(socket, in, out);
        }
    }


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
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        try {
            Socket socket = new Socket("localhost", 1234); // Replace with server IP if needed
            Client client = new Client(socket, username);
            client.receiveMessage(); // Listen to messages from server
            client.sendMessage();    // Send messages to server
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
