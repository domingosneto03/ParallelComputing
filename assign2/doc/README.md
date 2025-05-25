# Secure Java Chat Application

This is a secure multi-client chat server with user registration, authentication via tokens, chat rooms, and basic AI integration.

---

## Requirements

- Java 21 or higher
- SSL/TLS keystore: configured at `certs/server_keystore.jks`

---

## How to Run

### Option 1: Using the Terminal

1. Compile the project:
   ```bash
   javac Server.java Client.java Room.java User.java OllamaClient.java AIRoom.java
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
- Option A: run `Client` from different directories
- Option B: run `Client` with a unique argument per instance:
   ```bash
   java Client client1
   java Client client2
   ```

---

### Option 2: Using IntelliJ

1. Import the project into IntelliJ.

2. Create Run/Debug Configurations:
   - One for `Server`
   - One or more for `Client`

3. For multiple clients in the same directory, set different Program arguments:
   - `client1`
   - `client2`
   - etc.


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
