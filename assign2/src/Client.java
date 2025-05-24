import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {

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
            }
        } catch (IOException e) {
            System.out.println("❌ SSL Connection failed: " + e.getMessage());
        }
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

         */
    }


}
