import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.*;
import java.net.*;

public class ClientHandler implements Runnable {
    public ArrayList<ClientHandler> clientList = new ArrayList<>();
    private Socket socket;
    //read and write buffer for sending and receive msg
    private BufferedReader in;
    private BufferedWriter out;
    //
    private String clientUsername;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.out = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
            this.in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            //this is the first line when client enter is his username
            this.clientUsername = in.readLine();
            //
            System.out.println("Client connected: " + clientUsername);
            //

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //remove client handler => shutdown
    public void removeClientHandler() {
        clientList.remove(this);
        sendAllUser("Sever: " + clientUsername + "has left");
    }

    //close this socket
    public void closeAll(Socket socket, BufferedReader in, BufferedWriter out) {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }//
    }

    //send all user
    public void sendAllUser(String msg) {
        //just like send msg but for all user
        //=> using loop
        for (ClientHandler client : clientList) {
            try {
                //because we dont we to send to ourself
                if (client.clientUsername != this.clientUsername) {
                    out = client.out;
                    out.write(msg);
                    out.newLine();
                    out.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //send msg
    //close clientHandler
    //close everything

    //when this thread running
    @Override
    public void run() {
        String messageFromCLient;
        while (socket.isConnected()) {
            try {
                //read msg from clients
                messageFromCLient = in.readLine();

            } catch (Exception e) {
                closeAll(socket, in, out);
                break;
            }
        }
    }
}
