# Cline IntelliJ Plugin - IPC Design

## 1. Overview

This document outlines the Inter-Process Communication (IPC) strategy for the Cline IntelliJ Plugin. The plugin will adopt a hybrid architecture where the IntelliJ Plugin (written in Java/Kotlin) acts as the user interface and IDE interaction layer, while the core Cline logic, including AI model interaction and potentially complex operations like browser automation, will reside in the existing Node.js application (referred to as "Cline Core").

## 2. Chosen Approach: Hybrid Architecture

*   **IntelliJ Plugin (Java/Kotlin):** Manages the Tool Window UI, editor interactions, file system operations via IntelliJ APIs, and execution of terminal commands. It will be responsible for initiating communication with the Cline Core.
*   **Cline Core (Node.js):** Contains the primary AI logic, Model Context Protocol (MCP) interactions, and other functionalities as per the original VS Code extension. It will run as a separate process managed by the IntelliJ Plugin.

## 3. IPC Mechanism

*   **Method:** Local Sockets (TCP/IP on `localhost`).
    *   **Rationale:** Local sockets are chosen over alternatives like HTTP or standard I/O pipes for the following reasons:
        *   **Performance:** Generally offers lower latency than HTTP for local communication.
        *   **Bidirectional Communication:** Sockets inherently support full-duplex communication, which is suitable for the request/response and event-driven nature of Cline's interactions.
        *   **Security:** Communication is confined to the local machine, reducing external exposure compared to an HTTP server open on a port (though still important to manage port allocation).
        *   **Robustness:** Well-established and reliable for IPC.
*   **Alternative Considered:** HTTP Server. While viable, it adds a slight overhead and complexity of managing HTTP requests/responses for what is essentially local IPC. Standard I/O could be too simplistic for complex message structures.

## 4. Communication Protocol

*   **Format:** JSON-based messages.
    *   **Rationale:** JSON is lightweight, human-readable, and widely supported across both Java (with libraries like Jackson or Gson) and Node.js (native support).
*   **Message Structure (Conceptual Examples):**

    Each message could have a `type` field to determine its purpose and a `payload` field for the data.

    **Example: Request from Plugin to Core (e.g., process a user query)**
    ```json
    {
      "type": "USER_QUERY",
      "payload": {
        "queryText": "How do I refactor this function?",
        "activeFilePath": "/path/to/current/file.java",
        "projectContext": {
          // Relevant project details
        }
      },
      "messageId": "uuid-1234-abcd"
    }
    ```

    **Example: Response from Core to Plugin (e.g., AI response or request for action)**
    ```json
    {
      "type": "AI_RESPONSE",
      "payload": {
        "displayText": "The AI suggests the following...",
        "actions": [
          { "type": "EDIT_FILE", "filePath": "...", "changes": "..." },
          { "type": "RUN_COMMAND", "command": "..." }
        ]
      },
      "inReplyTo": "uuid-1234-abcd"
    }
    ```

    **Example: Notification from Core to Plugin (e.g., progress update)**
    ```json
    {
      "type": "PROGRESS_UPDATE",
      "payload": {
        "statusText": "Analyzing context...",
        "percentage": 30
      },
      "messageId": "uuid-5678-efgh"
    }
    ```
*   **Error Handling:** Errors should also be communicated via JSON messages, perhaps with a `type: "ERROR_RESPONSE"`.

## 5. Cline Core Management

*   The IntelliJ plugin will be responsible for starting and stopping the Cline Core Node.js process.
*   The port for the local socket communication will need to be managed (e.g., find an available port or use a fixed one with clear documentation).

## 6. Future Considerations
*   Schema definition for messages (e.g., using JSON Schema) for validation.
*   More complex state management between plugin and core.
