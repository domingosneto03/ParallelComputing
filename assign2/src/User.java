import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.locks.*;


// helper singleton which takes care of all things user account related
public class User implements Serializable{
    private static final long serialVersionUID = 1L;
    private final String username;
    private final String passwordHash;
    private final String salt;
    private String authToken;
    private Instant tokenExpiry;


    public User(String username, String passwordHash, String salt) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.salt = salt;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Instant getTokenExpiry() {
        return tokenExpiry;
    }

    public void setTokenExpiry(Instant tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
    }

    public boolean isTokenValid(String token) {
        return this.authToken != null &&
                this.authToken.equals(token) &&
                Instant.now().isBefore(this.tokenExpiry);
    }

    public String getUsername() {
        return username;
    }

    public boolean verifyPassword(String password) {
        String attemptHash = hashPassword(password, this.salt);
        return this.passwordHash.equals(attemptHash);
    }
    
    // Static methods for user management
    private static final Map<String, User> users = new HashMap<>();
    private static final ReentrantReadWriteLock usersLock = new ReentrantReadWriteLock();
    private static final String USER_DATA_FILE = "users.dat";
    
    public static boolean register(String username, String password) {
        usersLock.writeLock().lock();
        try {
            if (users.containsKey(username)) {
                return false;
            }
            
            String salt = generateSalt();
            String passwordHash = hashPassword(password, salt);
            User newUser = new User(username, passwordHash, salt);
            users.put(username, newUser);
            saveUserData();
            return true;
        } finally {
            usersLock.writeLock().unlock();
        }
    }
    
    public static boolean authenticate(String username, String password) {
        usersLock.readLock().lock();
        try {
            User user = users.get(username);
            return user != null && user.verifyPassword(password);
        } finally {
            usersLock.readLock().unlock();
        }
    }

    public static User getUser(String username) {
        usersLock.readLock().lock();
        try {
            return users.get(username);
        } finally {
            usersLock.readLock().unlock();
        }
    }

    public static User getUserByToken(String token) {
        usersLock.readLock().lock();
        try {
            for (User user : users.values()) {
                if (user.isTokenValid(token)) {
                    return user;
                }
            }
            return null;
        } finally {
            usersLock.readLock().unlock();
        }
    }
    
    public static void loadUserData() {
        if (!new File(USER_DATA_FILE).exists()) {
            return;
        }
        
        usersLock.writeLock().lock();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_DATA_FILE))) {
            @SuppressWarnings("unchecked")
            Map<String, User> loadedUsers = (Map<String, User>) ois.readObject();
            users.clear();
            users.putAll(loadedUsers);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading user data: " + e.getMessage());
        } finally {
            usersLock.writeLock().unlock();
        }
    }
    
    public static void saveUserData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.err.println("Error saving user data: " + e.getMessage());
        }
    }

    // Utils
    private static String generateSalt() {
        byte[] salt = new byte[16];
        new Random().nextBytes(salt);
        return bytesToHex(salt);
    }

    private static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(hexToBytes(salt));
            byte[] hashedPassword = md.digest(password.getBytes());
            return bytesToHex(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not available", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static byte[] hexToBytes(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}