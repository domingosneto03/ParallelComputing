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

            String json = "{\"model\":\"gemma3\",\"prompt\":\"" +
                    prompt.replace("\"", "\\\"").replace("\n", "\\n") + "\"}";

            System.out.println("Sending JSON: " + json);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode != 200) {
                return "[Bot error: HTTP " + responseCode + "]";
            }

            // parse streaming responses
            StringBuilder botResponse = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {

                    if (line.contains("\"response\":\"")) {
                        int start = line.indexOf("\"response\":\"") + 12;
                        int end = line.indexOf("\"", start);
                        if (end > start) {
                            String chunk = line.substring(start, end).replace("\\n", "\n");
                            botResponse.append(chunk);
                        }
                    }
                }
            }

            return botResponse.length() > 0 ? botResponse.toString()
                    : "[Bot error: Empty response]";
        } catch (Exception e) {
            return "[Bot error: " + e.getMessage() + "]";
        }
    }
}