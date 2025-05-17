import java.net.*;
import java.io.*;

public class Server {
    private final int port;
    private ServerSocket serverSocket;
    private boolean isRunning;
    
    public Server(int port) {
        this.port = port;
        this.isRunning = false;
        User.loadUserData(); // Load user data on startup
    }
    
    public void start() {
        isRunning = true;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            
            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                Thread.startVirtualThread(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
    
    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            
            out.println("Welcome to the chat server!");
            out.println("Commands: REGISTER, LOGIN, QUIT");
            
            boolean authenticated = false;
            String username = null;
            
            while (!authenticated) {
                String input = in.readLine();
                if (input == null || input.equalsIgnoreCase("QUIT")) {
                    break;
                }
                
                String[] parts = input.split("\\s+", 3);
                String command = parts[0].toUpperCase();
                
                switch (command) {
                    case "REGISTER":
                        if (parts.length < 3) {
                            out.println("ERROR Usage: REGISTER <username> <password>");
                            break;
                        }
                        username = parts[1];
                        String password = parts[2];
                        if (User.register(username, password)) {
                            out.println("SUCCESS User registered");
                        } else {
                            out.println("ERROR Username already exists");
                        }
                        break;
                        
                    case "LOGIN":
                        if (parts.length < 3) {
                            out.println("ERROR Usage: LOGIN <username> <password>");
                            break;
                        }
                        username = parts[1];
                        password = parts[2];
                        if (User.authenticate(username, password)) {
                            out.println("SUCCESS Authentication successful");
                            authenticated = true;
                        } else {
                            out.println("ERROR Invalid credentials");
                        }
                        break;
                        
                    default:
                        out.println("ERROR Unknown command");
                        break;
                }
            }
            
            if (authenticated) {
                out.println("AUTHENTICATED Welcome, " + username + "!");
                // Room selection and chat functionality would go here
            }
            
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
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 12345;
        Server server = new Server(port);
        server.start();
    }
}