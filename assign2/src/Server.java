import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class Server {
    private final int port;
    private ServerSocket serverSocket;
    private boolean isRunning;
    
    // Data structures for rooms and users
    // Will need proper synchronization
    
    public Server(int port) {
        this.port = port;
        this.isRunning = false;
    }
    
    public void start() {
        isRunning = true;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            
            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                // Create a new virtual thread for each client
                Thread.startVirtualThread(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
    
    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            
            // Authentication logic
            
            // After authentication, room selection and chat handling
            
        } catch (IOException e) {
            System.err.println("Client handling error: " + e.getMessage());
        }
    }
    
    public void stop() {
        isRunning = false;
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error stopping server: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        int port = 12345; // Default port
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        
        Server server = new Server(port);
        server.start();
    }
}