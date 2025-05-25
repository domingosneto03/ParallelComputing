import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class AIRoom extends Room {
    private final List<String> conversation = new ArrayList<>();
    private final OllamaClient ollama;
    private final ReentrantLock convoLock = new ReentrantLock();

    public AIRoom(String name, OllamaClient ollama) {
        super(name);
        this.ollama = ollama;
    }

    @Override
    public void broadcast(String message) {
        convoLock.lock();
        try {
            conversation.add(message);
            super.broadcast(message);

            if (message.contains("!ai ")) {
                String prompt = message.substring(message.indexOf("!ai ") + 4);
                StringBuilder context = new StringBuilder();
                context.append("[Conversation context]:");
                for (String msg : conversation) {
                    if (msg.contains("!ai ")) {continue;} // remove ai call from context
                    context.append(msg).append("\n");
                }
                context.append("\n[Prompt you should answer to]: ").append(prompt);
                String aiResponse = ollama.getCompletion(context.toString());
                String botMsg = "Bot: " + aiResponse;
                conversation.add(botMsg);
                super.broadcast(botMsg);
            }
        } finally {
            convoLock.unlock();
        }
    }
}