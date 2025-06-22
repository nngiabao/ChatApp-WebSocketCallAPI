import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        System.out.println("Server started, waiting for clients...");
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("New client attempting to connect...");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (Exception e) {
            System.out.println("Server stopped or error occurred: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            Server server = new Server(serverSocket);
            server.startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
