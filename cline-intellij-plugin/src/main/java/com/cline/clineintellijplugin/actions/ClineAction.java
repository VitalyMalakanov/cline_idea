package com.cline.clineintellijplugin.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;

public class ClineAction extends AnAction {

    /**
     * Default constructor.
     * Sets the text, description, and icon for the action.
     */
    public ClineAction() {
        super("Open Cline AI", "Open the Cline AI Assistant tool window", AllIcons.Actions.Execute);
    }

    /**
     * Handles the action event.
     * Opens the Cline AI tool window when the action is performed.
     *
     * @param e The action event.
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        ToolWindow toolWindow = toolWindowManager.getToolWindow("ClineAIWindow"); // ID from plugin.xml

        if (toolWindow != null) {
            toolWindow.activate(null);
        }
    }

    /**
     * Updates the state of the action.
     * The action is enabled and visible only if a project is open.
     *
     * @param e The action event.
     */
    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);
    }
}
