import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    private final int port;
    private SSLServerSocket serverSocket;
    private boolean isRunning;
    private static final Map<String, Room> rooms = new HashMap<>();
    private static final ReentrantLock roomsLock = new ReentrantLock();

    private static final String KEYSTORE_PATH = "certs/server_keystore.jks";
    private static final String KEYSTORE_PASSWORD = "cpd_g12";

    public Server(int port) {
        this.port = port;
        this.isRunning = false;
        User.loadUserData(); // Load user data on startup
    }
    
    public void start() {
        isRunning = true;
        try {
            System.setProperty("javax.net.ssl.keyStore", KEYSTORE_PATH);
            System.setProperty("javax.net.ssl.keyStorePassword", KEYSTORE_PASSWORD);

            SSLServerSocketFactory sslFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

            try (SSLServerSocket serverSocket = (SSLServerSocket) sslFactory.createServerSocket(port)) {
                System.out.println("Server started on port " + port);

                while (isRunning) {
                    SSLSocket clientSocket = (SSLSocket) serverSocket.accept();
                    Thread.startVirtualThread(() -> handleClient(clientSocket));
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
    
    private void handleClient(SSLSocket clientSocket) {
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
                Room currentRoom = null;
                out.println("Available rooms: " + getRoomNames());
                out.println("Use: CREATE <room> or JOIN <room> to enter a room.");

                String input;
                while ((input = in.readLine()) != null) {
                    if (input.toUpperCase().startsWith("CREATE ")) {
                        String roomName = input.substring(7).trim();
                        currentRoom = getOrCreateRoom(roomName);
                        currentRoom.addClient(out);
                        out.println("Created and joined room: " + roomName);
                    } else if (input.toUpperCase().startsWith("JOIN ")) {
                        String roomName = input.substring(5).trim();
                        currentRoom = getOrCreateRoom(roomName);
                        currentRoom.addClient(out);
                        out.println("Joined room: " + roomName);
                    } else if (currentRoom != null) {
                        var message = input;
                        if (input.startsWith("^[")) {
                            message = input.substring(2);
                        }
                        currentRoom.broadcast(username + ": " + message);
                    } else {
                        out.println("ERROR Unknown command. Use CREATE or JOIN first.");
                    }
                }

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

    public static List<String> getRoomNames() {
        roomsLock.lock();
        try {
            return new ArrayList<>(rooms.keySet());
        } finally {
            roomsLock.unlock();
        }
    }

    public static Room getOrCreateRoom(String name) {
        roomsLock.lock();
        try {
            return rooms.computeIfAbsent(name, Room::new);
        } finally {
            roomsLock.unlock();
        }
    }


    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 12345;
        Server server = new Server(port);
        server.start();
    }
}