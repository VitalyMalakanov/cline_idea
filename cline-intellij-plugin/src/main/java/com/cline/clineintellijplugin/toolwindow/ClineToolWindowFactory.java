package com.cline.clineintellijplugin.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClineToolWindowFactory implements ToolWindowFactory {

    /**
     * Create the tool window content.
     *
     * @param project    current project
     * @param toolWindow current tool window
     */
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Message area for displaying conversation
        JTextArea messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Input field for user messages
        JTextField inputField = new JTextField();
        mainPanel.add(inputField, BorderLayout.SOUTH);

        // Add ActionListener to the input field
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = inputField.getText();
                if (!text.trim().isEmpty()) {
                    // Append user's message to messageArea
                    messageArea.append("You: " + text + "\n");

                    // Placeholder for sending to AI and receiving response
                    // Simulate AI processing and response
                    messageArea.append("AI: Processing your message...\n");
                    // Simulate a delay or actual processing here if it were real
                    // For now, just a placeholder response
                    messageArea.append("AI: This is a simulated response to: \"" + text + "\"\n");

                    inputField.setText(""); // Clear the input field
                }
            }
        });

        // Get the content manager from the tool window
        ContentManager contentManager = toolWindow.getContentManager();

        // Create content and add it to the tool window
        Content content = contentManager.getFactory().createContent(mainPanel, "", false);
        contentManager.addContent(content);
    }

    /**
     * Initialize the tool window.
     *
     * @param toolWindow current tool window
     */
    @Override
    public void init(@NotNull ToolWindow toolWindow) {
        // Can be left empty for now
    }

    /**
     * Check if the tool window should be available.
     *
     * @param project current project
     * @return true if available, false otherwise
     */
    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        return true; // Available for all projects
    }
}
