import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    //
    private ServerSocket serverSocket;
    //default init
    public Server() {
        try {
            serverSocket = new ServerSocket(8080);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    //
    public void startServer(){
        try{
            while(!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");

                //once client connect to server
                //create new thread
                Thread thread = new Thread();
                thread.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {

    }
}
