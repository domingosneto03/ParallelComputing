import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.*;

public class Client {
    private static String authToken = null;
    private static String tokenFile = ".tokens/token_default.txt";

    public static void main(String[] args) {

        if (args.length > 0 && !args[0].isBlank()) {
            tokenFile = ".tokens/token_" + args[0] + ".txt";
        }

        try (BufferedReader tokenReader = new BufferedReader(new FileReader(tokenFile))) {
            authToken = tokenReader.readLine();
        } catch (IOException ignored) {}

        System.setProperty("javax.net.ssl.trustStore", "certs/server_keystore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "cpd_g12");

        try {
            SSLSocketFactory sslFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            try (SSLSocket socket = (SSLSocket) sslFactory.createSocket("localhost", 12345);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

                System.out.println("SSL/TLS Protocol: " + socket.getSession().getProtocol());
                // Thread to read messages from server

                if (authToken != null) {
                    out.println("RECONNECT " + authToken);
                }

                new Thread(() -> {
                    try {
                        String serverMsg;
                        while ((serverMsg = in.readLine()) != null) {
                            System.out.println("[Server] " + serverMsg);
                            if (serverMsg.startsWith("TOKEN ")) {
                                authToken = serverMsg.substring(6).trim();
                                try {
                                    File tokenPath = new File(tokenFile);
                                    tokenPath.getParentFile().mkdirs(); // Ensure .tokens/ exists
                                    try (PrintWriter tokenWriter = new PrintWriter(tokenPath)) {
                                        tokenWriter.println(authToken);
                                    }
                                } catch (IOException e) {
                                    System.out.println("⚠ Failed to save token: " + e.getMessage());
                                }
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Connection closed.");
                    }
                }).start();

                // Loop to send user input to server
                String userMsg;
                while ((userMsg = userInput.readLine()) != null) {
                    if (userMsg.equalsIgnoreCase("LOGOUT")) {
                        new File(tokenFile).delete();
                    }
                    out.println(userMsg);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ SSL Connection failed: " + e.getMessage());
        }
    }
}

// inside main method of Client.java
/*
        try (Socket socket = new Socket("localhost", 12345);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            // Thread to read messages from server
            new Thread(() -> {
                try {
                    String serverMsg;
                    while ((serverMsg = in.readLine()) != null) {
                        System.out.println("[Server] " + serverMsg);
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                }
            }).start();

            // Loop to send user input to server
            String userMsg;
            while ((userMsg = userInput.readLine()) != null) {
                out.println(userMsg);
            }

        } catch (IOException e) {
            System.out.println("❌ Could not connect: " + e.getMessage());
        }
    }
*/