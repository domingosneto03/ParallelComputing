import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Room {
    private final String name;
    private final List<PrintWriter> clients = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();

    public Room(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addClient(PrintWriter out) {
        lock.lock();
        try {
            clients.add(out);
            broadcast("[System]: A new user has entered the room.");
        } finally {
            lock.unlock();
        }
    }

    public void removeClient(PrintWriter out) {
        lock.lock();
        try {
            clients.remove(out);
            broadcast("[System]: A user has left the room.");
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
