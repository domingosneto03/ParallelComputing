import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class OllamaClient {
    private final String host;
    private final int port;

    public OllamaClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getCompletion(String prompt) {
        try {
            URL url = new URL("http://" + host + ":" + port + "/api/generate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            String json = "{\"model\":\"llama3\",\"prompt\":\"" + prompt.replace("\"", "\\\"") + "\"}";
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
            }
            // Simple extraction of the "response" field
            String resp = response.toString();
            int idx = resp.lastIndexOf("\"response\":\"");
            if (idx != -1) {
                int start = idx + 12;
                int end = resp.indexOf("\"", start);
                if (end > start) {
                    return resp.substring(start, end).replace("\\n", "\n");
                }
            }
            return "[Bot error: Unexpected Ollama response]";
        } catch (Exception e) {
            return "[Bot error: " + e.getMessage() + "]";
        }
    }
}