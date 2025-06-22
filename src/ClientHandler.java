import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler implements Runnable {

    // Thread-safe list for active clients
    public static List<ClientHandler> clientList = Collections.synchronizedList(new ArrayList<>());

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String clientUsername;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // First message from client is their username
            this.clientUsername = in.readLine();
            System.out.println("Client connected: " + clientUsername);

            clientList.add(this);
            sendAllUser("Server: " + clientUsername + " has joined the chat.");

        } catch (IOException e) {
            closeAll(socket, in, out);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = in.readLine();
                if (messageFromClient == null) break; // Client closed connection
                sendAllUser(clientUsername + ": " + messageFromClient);
            } catch (IOException e) {
                break;
            }
        }
        removeClientHandler();
    }

    public void sendAllUser(String message) {
        synchronized (clientList) {
            for (ClientHandler client : clientList) {
                try {
                    if (!client.clientUsername.equals(this.clientUsername)) {
                        BufferedWriter writer = client.out;
                        writer.write(message);
                        writer.newLine();
                        writer.flush();
                    }
                } catch (IOException e) {
                    client.removeClientHandler();
                }
            }
        }
    }

    public void removeClientHandler() {
        clientList.remove(this);
        sendAllUser("Server: " + clientUsername + " has left the chat.");
        closeAll(socket, in, out);
        System.out.println("Client disconnected: " + clientUsername);
    }

    public void closeAll(Socket socket, BufferedReader in, BufferedWriter out) {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
