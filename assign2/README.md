# Secure Java Chat Application

This is a secure multi-client chat server with user registration, authentication via tokens, chat rooms, and basic AI integration.

---

## Requirements

- Java 21 or higher
- Ollama with gemma3 model

---

## How to Run

1. Compile the project:
   ```bash
   javac Server.java Client.java Room.java User.java OllamaClient.java AIRoom.java
   ```
   For the AI rooms to work, you must also get the gemma3 model and start it:
   ```bash
   ollama pull gemma3
   ollama serve
   ```

2. Start the server:
   ```bash
   java Server
   ```

3. Start a client:
   ```bash
   java Client
   ```

To simulate multiple clients:
- run `Client` from different directories (duplicate without the tokens)


---

## Commands Summary

| Command         | Description                                |
|-----------------|--------------------------------------------|
| REGISTER        | `REGISTER <username> <password>`          |
| LOGIN           | `LOGIN <username> <password>`             |
| RECONNECT       | automatic with token                      |
| CREATE          | `CREATE <roomname>`                       |
| CREATE_AI       | `CREATE_AI <roomname>`                    |
| JOIN            | `JOIN <existing_room>`                    |
| .LEAVE / ESC    | Leave the current room                    |
| .LIST_USERS     | Show users in current room                |
| LOGOUT          | Logout and delete stored token            |
| QUIT            | Exit client                               |
