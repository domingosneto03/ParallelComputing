import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class Room {
    private final String name;
    private final List<PrintWriter> clients = new ArrayList<>();
    private final Map<PrintWriter, String> clientNames = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    public Room(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addClient(PrintWriter out, String username) {
        lock.lock();
        try {
            clients.add(out);
            clientNames.put(out, username);
            broadcast(username + " has entered the room.");
            if (clients.size() == 1) {
                out.println("You are the only one in this room.");
            }
        } finally {
            lock.unlock();
        }
    }

    public void removeClient(String username) {
        lock.lock();
        try {
            PrintWriter toRemove = null;
            for (Map.Entry<PrintWriter, String> entry : clientNames.entrySet()) {
                if (entry.getValue().equals(username)) {
                    toRemove = entry.getKey();
                    break;
                }
            }
            if (toRemove != null) {
                clients.remove(toRemove);
                clientNames.remove(toRemove);
                broadcast(username + " has left the room.");
            }
            if (clients.size() == 1) {
                clients.get(0).println("You are now alone in this room.");
            }
        } finally {
            lock.unlock();
        }
    }
    public void listUsers(PrintWriter out) {
        lock.lock();
        try {
            out.println("[Users in room '" + name + "']:");
            for (String user : clientNames.values()) {
                out.println("- " + user);
            }
        } finally {
            lock.unlock();
        }
    }

    public void broadcast(String message) {
        lock.lock();
        try {
            for (PrintWriter client : clients) {
                client.println(message);
            }
        } finally {
            lock.unlock();
        }
    }
}
